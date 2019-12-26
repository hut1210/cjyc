package com.cjyc.common.system.service.impl;

import com.Pingxx.model.OrderModel;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.dto.customer.pingxx.ValidateSweepCodeDto;
import com.cjyc.common.model.dto.web.pingxx.WebOutOfStockDto;
import com.cjyc.common.model.dto.web.pingxx.WebPrePayDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.exception.CommonException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.cjyc.common.model.vo.customer.order.ValidateSweepCodePayVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.common.model.vo.web.task.TaskVo;
import com.cjyc.common.system.config.PingProperty;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.service.ICsTransactionService;
import com.cjyc.common.system.util.RedisLock;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: Hut
 * @Date: 2019/12/9 19:291
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CsPingPayServiceImpl implements ICsPingPayService {

    @Autowired
    private ICsTransactionService cStransactionService;

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IOrderCarDao orderCarDao;

    @Resource
    private RedisLock redisLock;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private IWaybillDao waybillDao;

    @Resource
    private ICarrierDao carrierDao;

    @Resource
    private ITaskDao taskDao;

    @Autowired
    private ICsSmsService csSmsService;

    @Override
    public Charge sweepDriveCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException {
        log.info("sweepDriveCode taskId ="+sweepCodeDto.getTaskId());
        ValidateSweepCodeDto validateSweepCodeDto = new ValidateSweepCodeDto();
        validateSweepCodeDto.setTaskId(sweepCodeDto.getTaskId());
        validateSweepCodeDto.setLoginId(sweepCodeDto.getLoginId());
        validateSweepCodeDto.setTaskCarIdList(sweepCodeDto.getTaskCarIdList());
        ResultVo<ValidateSweepCodePayVo> resultVo = validateCarPayState(validateSweepCodeDto,true);

        if(ResultEnum.SUCCESS.getCode() != resultVo.getCode()){
            throw new CommonException(resultVo.getMsg());
        }
        BigDecimal amount = resultVo.getData().getAmount();
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new CommonException(("无需支付"));
        }
        OrderModel om = new OrderModel();

        om.setClientIp(sweepCodeDto.getIp());
        om.setPingAppId(sweepCodeDto.getClientType()==ClientEnum.APP_DRIVER.code?PingProperty.driverAppId:PingProperty.userAppId);
        //创建Charge对象
        Charge charge = new Charge();
        try {
            List<String> tempList = sweepCodeDto.getTaskCarIdList();

            List<Long> taskCarIdList = convertToLongList(tempList);
            List<String> orderCarNosList = cStransactionService.getOrderCarNosByTaskCarIds(taskCarIdList);
            BigDecimal freightFee = cStransactionService.getAmountByOrderCarNos(orderCarNosList);
            om.setAmount(freightFee);
            om.setDriver_code(String.valueOf(sweepCodeDto.getLoginId()));
            om.setOrderCarIds(orderCarNosList);
            om.setTaskId(String.valueOf(sweepCodeDto.getTaskId()));
            om.setTaskCarIdList(sweepCodeDto.getTaskCarIdList());
            om.setChannel(sweepCodeDto.getChannel());
            if(sweepCodeDto.getClientType()==ClientEnum.APP_DRIVER.code){
                om.setSubject("司机端收款码功能!");
                om.setBody("司机端生成二维码！");
                om.setChargeType("2");
            }else{
                om.setSubject("业务员收款码功能!");
                om.setBody("业务员端生成二维码！");
                om.setChargeType("3");
            }

            om.setClientType(String.valueOf(sweepCodeDto.getClientType()));
            om.setDescription("韵车订单号："+om.getOrderNo());
            charge = createDriverCode(om);

            cStransactionService.saveTransactions(charge, "0");
        } catch (Exception e) {
            log.error("扫码支付异常",e);
        }
        return charge;
    }

    private List<Long> convertToLongList(List<String> tempList) {
        List<Long> list = new ArrayList<>();
        for(int i=0;i<tempList.size();i++){
            list.add(Long.valueOf(tempList.get(i)));
        }
        return list;
    }

    private Charge createDriverCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException{
        initPingApiKey();
        Map<String, Object> params = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        params.put("order_no", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis()); // 商户订单号, 必传
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", om.getPingAppId());
        params.put("app", app);
        params.put("channel", om.getChannel());// alipay_qr 支付宝扫码支付 /wx_pub_qr 微信扫码支付
        params.put("amount", om.getAmount()); // 订单总金额，单位：分, 必传
        params.put("client_ip", om.getClientIp()); // 客户端的 IP 地址 (IPv4 格式，要求商户上传真实的，渠道可能会判断), 必传
        params.put("currency", "cny"); // 仅支持人民币 cny, 必传
        params.put("subject", om.getSubject()); // 商品的标题, 必传
        params.put("body", om.getBody()); // 商品的描述信息, 必传
        params.put("description", om.getDescription()); // 备注：订单号
        Map<String, Object> meta = new HashMap<String,Object>();
        meta.put("chargeType", om.getChargeType());//1 预付款 2 司机出示二维码 3 业务员出示二维码 5后台预付 6出库付款
        //自定义存储字段
        meta.put("orderNo", om.getOrderNo());	//订单号
        meta.put("orderCarIds",om.getOrderCarIds());//订单Id
        meta.put("driver_code", om.getDriver_code());//司机Code
        meta.put("order_type", om.getOrder_type());
        meta.put("driver_name", om.getDriver_name());
        meta.put("driver_phone", om.getDriver_phone());
        meta.put("back_type", om.getBack_type());
        meta.put("taskId",om.getTaskId());
        meta.put("taskCarIdList",om.getTaskCarIdList());
        //当为微信支付是需要product_id
        if("wx_pub_qr".equals(om.getChannel())){
            Map<String, Object> extra  = new HashMap<String,Object>();
            extra.put("product_id", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis());
            params.put("extra", extra);
        }
        log.info("createDriverCode meta ="+meta.toString());
        params.put("metadata",meta);//自定义参数
        Charge charge = Charge.create(params); // 创建 Charge 对象 方法
        return charge;
    }

    private void initPingApiKey() throws FileNotFoundException {
        Pingpp.apiKey = PingProperty.apiKey;
        //Pingpp.overrideApiBase("https://sapi.pingxx.com");//此接口为ping++协助测试的接口 升级完成后注释掉 20181023 add
        System.setProperty("https.protocols", "TLSv1.2");//20181023 添加 (TLSv1.2升级配置)
        Pingpp.privateKeyPath = this.getClass().getClassLoader().getResource("your_rsa_private_key_pkcs.pem").getPath();

    }

    @Override
    public ResultVo<ValidateSweepCodePayVo> validateCarPayState(ValidateSweepCodeDto validateSweepCodeDto, boolean addLock) {

        log.info("validateCarPayState validateSweepCodeDto.getClientType()= "+validateSweepCodeDto.getClientType());
        if(validateSweepCodeDto.getClientType()==null){
            if(validateSweepCodeDto.getCode()==null){
                return BaseResultUtil.fail("缺少收车码");
            }
        }

        Long taskId = validateSweepCodeDto.getTaskId();

        if(taskId==null){
            return BaseResultUtil.fail("缺少参数taskId");
        }else{
            TaskVo taskVo = taskDao.findVoById(taskId);
            if(taskVo == null) {
                return BaseResultUtil.fail("任务不存在");
            }

            Waybill waybill = waybillDao.findByNo(taskVo.getWaybillNo());
            if(waybill == null ){
                return BaseResultUtil.fail("运单不存在");
            }
        }

        List<String> taskCarIdList = validateSweepCodeDto.getTaskCarIdList();

        List<String> orderCarNosList = new ArrayList<>();
        if(taskCarIdList!=null){
            orderCarNosList = cStransactionService.getOrderCarNosByTaskCarIds(convertToLongList(taskCarIdList));
        }
        List<com.cjyc.common.model.entity.Order> list = orderDao.findListByCarNos(orderCarNosList);
        if (CollectionUtils.isEmpty(list)) {
            return BaseResultUtil.fail("订单信息丢失");
        }
        if (list.size() > 1) {
            return BaseResultUtil.fail("仅支持同一订单内的车辆签收");
        }

        com.cjyc.common.model.entity.Order order = list.get(0);

        if(order.getBackContactPhone()!=null){
            if(!csSmsService.validateCaptcha(order.getBackContactPhone(),validateSweepCodeDto.getCode(), CaptchaTypeEnum.CONFIRM_RECEIPT,
            validateSweepCodeDto.getClientType().equals("4")?ClientEnum.APP_DRIVER:ClientEnum.APP_SALESMAN)){
                return BaseResultUtil.fail("验证码输入错误");
            }
        }

        int isNeedPay = 0; //0不需要支付，1支付
        BigDecimal amount = BigDecimal.ZERO;
        List<String> orderCarNos = Lists.newArrayList();
        Set<String> lockKeySet = Sets.newHashSet();
        try {
            List<OrderCar> carList = orderCarDao.findListByNos(orderCarNosList);
            if(CollectionUtils.isEmpty(carList)){
                return BaseResultUtil.fail("至少包含一辆车");
            }
            for (OrderCar orderCar : carList) {
                if (orderCar == null || orderCar.getNo() == null) {
                    return BaseResultUtil.fail("订单车辆信息丢失");
                }
                if (orderCar.getState() >= OrderCarStateEnum.SIGNED.code) {
                    return BaseResultUtil.fail("订单车辆{0}已签收过，请刷新后重试", orderCar.getNo());
                }

                if(addLock){
                    String lockKey = RedisKeys.getWlCollectPayLockKey(orderCar.getNo());
                    String value = redisUtils.get(lockKey);
                    if (value != null && !value.equals(validateSweepCodeDto.getTaskId().toString())) {
                        return BaseResultUtil.fail("订单车辆{0}正在支付中", orderCar.getNo());
                    }
                    if (value != null) {
                        redisUtils.delete(lockKey);
                    }
                    if (!redisLock.lock(lockKey, validateSweepCodeDto.getTaskId(), 1800000, 100, 300)) {
                        return BaseResultUtil.fail("锁定车辆失败");
                    }
                    lockKeySet.add(lockKey);
                }

                //是否需要支付
                if (PayModeEnum.PERIOD.code == order.getPayType()) {
                    //账期
                    isNeedPay = 0;
                } else if (PayModeEnum.PREPAY.code == order.getPayType()) {
                    //预付
                    if (PayStateEnum.PAID.code != orderCar.getWlPayState()) {
                        return BaseResultUtil.fail("支付车辆{0}支付状态异常，预付未支付", orderCar.getNo());
                    }
                    isNeedPay = 0;
                } else {
                    //时付
                    if (PayStateEnum.PAID.code == orderCar.getWlPayState()) {
                        return BaseResultUtil.fail("订单车辆{0}已支付过，请刷新后重试", orderCar.getNo());
                    }
                    isNeedPay = 1;
                    if(CustomerTypeEnum.COOPERATOR.code == order.getCustomerType()){
                        amount = amount.add(orderCar.getTotalFee());
                    }else{
                        amount = amount.add(orderCar.getTotalFee()).subtract(orderCar.getCouponOffsetFee());
                    }
                }
                orderCarNos.add(orderCar.getNo());
            }
            if(CollectionUtils.isEmpty(orderCarNos)){
                return BaseResultUtil.fail("至少包含一辆车编号");
            }
            if(amount.compareTo(BigDecimal.ZERO) <= 0){
                isNeedPay = 0;
            }

            ValidateSweepCodePayVo resVo = new ValidateSweepCodePayVo();
            resVo.setAmount(amount);
            resVo.setIsNeedPay(isNeedPay);
            resVo.setTaskId(taskId);
            resVo.setTaskCarIds(convertToLongList(taskCarIdList));
            return BaseResultUtil.success(resVo);
        } finally {
            if(addLock && isNeedPay == 0){
                //解锁
                redisLock.releaseLock(lockKeySet);
            }
        }

    }

    @Override
    public Charge sweepSalesCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException {
        sweepCodeDto.setClientType(ClientEnum.APP_SALESMAN.code);
        return sweepDriveCode(sweepCodeDto);
    }

    @Override
    public ResultVo allinpayToCarrier(Long waybillId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException {
        Transfer transfer = new Transfer();
        List<Long> waybillIdList = new ArrayList<>();
        waybillIdList.add(waybillId);
        List<Waybill> waybillList = waybillDao.findListByIds(waybillIdList);
        try{
            if(waybillList!=null&&waybillList.size()>0){
                Waybill waybill = waybillList.get(0);
                Long carrierId = waybill.getCarrierId();
                BaseCarrierVo baseCarrierVo = carrierDao.showCarrierById(carrierId);
                transfer = allinpayTransferDriverCreate(baseCarrierVo,waybill);
            }
        }catch (Exception e){
            return BaseResultUtil.fail("通联代付失败");
        }

        return BaseResultUtil.success("通联代付成功");
    }

    @Override
    public Charge prePay(WebPrePayDto prePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException {

        OrderModel om = new OrderModel();
        //创建Charge对象
        Charge charge = new Charge();
        try {
            om.setClientIp(prePayDto.getIp());
            om.setPingAppId(PingProperty.userAppId);
            BigDecimal wlFee = cStransactionService.getAmountByOrderNo(prePayDto.getOrderNo());
            om.setPingAppId(PingProperty.customerAppId);
            om.setClientIp(prePayDto.getIp());
            om.setChannel(prePayDto.getChannel());
            om.setOrderNo(prePayDto.getOrderNo());
            om.setUid(String.valueOf(prePayDto.getUid()));
            om.setAmount(wlFee);
            om.setSubject("后台收款码");
            om.setBody("订单预付款");
            om.setChargeType("5");
            om.setClientType(String.valueOf(ClientEnum.WEB_SERVER.code));
            // 备注：订单号
            om.setDescription("韵车订单号："+om.getOrderNo());

            charge = createDriverCode(om);

            cStransactionService.saveTransactions(charge, "0");
        } catch (Exception e) {
            log.error("后台出示二维码异常",e);
        }
        return charge;
    }

    @Override
    public Charge getOutOfStockQrCode(WebOutOfStockDto webOutOfStockDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException{
        log.info("webOutOfStockDto taskId ="+webOutOfStockDto.getTaskId());
        ValidateSweepCodeDto validateSweepCodeDto = new ValidateSweepCodeDto();
        validateSweepCodeDto.setTaskId(webOutOfStockDto.getTaskId());
        validateSweepCodeDto.setLoginId(webOutOfStockDto.getLoginId());
        validateSweepCodeDto.setTaskCarIdList(webOutOfStockDto.getTaskCarIdList());
        validateSweepCodeDto.setClientType("1");
        ResultVo<ValidateSweepCodePayVo> resultVo = validateCarPayState(validateSweepCodeDto,true);

        if(ResultEnum.SUCCESS.getCode() != resultVo.getCode()){
            throw new CommonException(resultVo.getMsg());
        }
        BigDecimal amount = resultVo.getData().getAmount();
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new CommonException(("无需支付"));
        }
        OrderModel om = new OrderModel();

        om.setClientIp(webOutOfStockDto.getIp());
        om.setPingAppId(PingProperty.userAppId);
        //创建Charge对象
        Charge charge = new Charge();
        try {
            List<String> tempList = webOutOfStockDto.getTaskCarIdList();

            List<Long> taskCarIdList = convertToLongList(tempList);
            List<String> orderCarNosList = cStransactionService.getOrderCarNosByTaskCarIds(taskCarIdList);
            BigDecimal freightFee = cStransactionService.getAmountByOrderCarNos(orderCarNosList);
            om.setAmount(freightFee);
            om.setDriver_code(String.valueOf(webOutOfStockDto.getLoginId()));
            om.setOrderCarIds(orderCarNosList);
            om.setTaskId(String.valueOf(webOutOfStockDto.getTaskId()));
            om.setTaskCarIdList(webOutOfStockDto.getTaskCarIdList());
            om.setChannel(webOutOfStockDto.getChannel());

            om.setSubject("确认出库收款码功能!");
            om.setBody("确认出库生成二维码！");
            om.setChargeType("6");//出库付款码

            om.setClientType(String.valueOf(ClientEnum.WEB_SERVER.code));
            om.setDescription("韵车订单号："+om.getOrderNo());
            charge = createDriverCode(om);

            cStransactionService.saveTransactions(charge, "0");
        } catch (Exception e) {
            log.error("扫码支付异常",e);
        }
        return charge;
    }

    public Transfer allinpayTransferDriverCreate(BaseCarrierVo baseCarrierVo,Waybill waybill) throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException {
        initPingApiKey();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> app = new HashMap<>();
        app.put("id", PingProperty.driverAppId);
        params.put("app", app);
        // 付款使用的商户内部订单号。 allinpay 限长20-40位不能重复的数字字母组合，必须以签约的通联的商户号开头（建议组合格式：通联商户号 + 时间戳 + 固定位数顺序流水号，不包含+号）
        params.put("order_no", PingProperty.businessCode + System.currentTimeMillis());

        // 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100,企业付款最小发送金额为1 元）
        //确认手续费
        BigDecimal amount = waybill.getFreightFee();
        BigDecimal src_amount = amount;
        params.put("amount", amount);
        // 目前支持 支付宝：alipay，银联：unionpay，微信公众号：wx_pub，通联：allinpay，京东：jdpay 余额：balance
        params.put("channel", "allinpay");
        params.put("type", "b2c");//付款类型，转账到个人用户为 b2c，转账到企业用户为 b2b（wx、wx_pub、wx_lite 和 balance 渠道的企业付款，仅支持 b2c）
        params.put("currency", "cny");
        params.put("description", "通联代付司机运费");

        Map<String, String> extra = new HashMap<String, String>();
        //1~100位，收款人姓名。必须
        extra.put("user_name", baseCarrierVo.getCardName());

        //1~32位，收款人银行卡号或者存折号。 必须
        extra.put("card_number", baseCarrierVo.getCardNo());
        //4位，开户银行编号，详情请参考通联代付银行编号说明。 必须
        extra.put("open_bank_code",baseCarrierVo.getBankName());

        params.put("extra", extra);

        Map<String, String> metadata = new HashMap<String,String>();
        metadata.put("type", "driverFreight");
        metadata.put("uid", String.valueOf(baseCarrierVo.getCarrierId()));
        metadata.put("order_detail_id", "");
        params.put("metadata", metadata);

        Transfer obj = Transfer.create(params);
        if(Pingpp.apiKey.contains("_test_")){//test模式调用查询相当于企业付款成功
            obj = transferRetrieve(obj.getId());
        }
        obj.setAmount(Integer.parseInt(src_amount.multiply(new BigDecimal(100)).toString()));
        return obj;
    }

    public Transfer transferRetrieve(String transferId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException{
        // 参数: transfer id
        Transfer obj = Transfer.retrieve(transferId);
        return obj;
    }
}
