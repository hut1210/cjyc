package com.cjyc.common.system.service.impl;

import com.Pingxx.model.OrderModel;
import com.Pingxx.model.OrderRefund;
import com.Pingxx.model.PingxxMetaData;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.dto.customer.pingxx.ValidateSweepCodeDto;
import com.cjyc.common.model.dto.web.pingxx.SalesPrePayDto;
import com.cjyc.common.model.dto.web.pingxx.WebOutOfStockDto;
import com.cjyc.common.model.dto.web.pingxx.WebPrePayDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.exception.CommonException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.BeanMapUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateSweepCodePayVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.common.model.vo.web.customer.ShowPartnerVo;
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
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.BigEndianConversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * @Author: Hut
 * @Date: 2020/03/07 15:51
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

    @Resource
    private ICustomerDao customerDao;

    @Resource
    private IExternalPaymentDao externalPaymentDao;

    @Resource
    private IOrderRefundDao orderRefundDao;

    @Resource
    private IConfigDao configDao;

    @Resource
    private IPaymentRecordDao paymentRecordDao;

    @Resource
    private IPaymentErrorLogDao paymentErrorLogDao;

    @Resource
    private ITradeBillDao tradeBillDao;

    private final Lock lock = new ReentrantLock();

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

        PingxxMetaData pingxxMetaData = new PingxxMetaData();
        om.setClientIp(sweepCodeDto.getIp());
        pingxxMetaData.setPingAppId(sweepCodeDto.getClientType()==ClientEnum.APP_DRIVER.code?PingProperty.driverAppId:PingProperty.userAppId);

        //创建Charge对象
        Charge charge = new Charge();
        //try {
        List<String> tempList = sweepCodeDto.getTaskCarIdList();

        List<Long> taskCarIdList = convertToLongList(tempList);
        List<String> orderCarNosList = cStransactionService.getOrderCarNosByTaskCarIds(taskCarIdList);

        List<com.cjyc.common.model.entity.Order> list = orderDao.findListByCarNos(orderCarNosList);
        com.cjyc.common.model.entity.Order order = list.get(0);

        BigDecimal freightFee = new BigDecimal("0");
        if(order!=null&&order.getCustomerType()==3){//合伙人
            freightFee = cStransactionService.getAmountByOrderCarNosToPartner(orderCarNosList);
        }else{//非合伙人
            freightFee = cStransactionService.getAmountByOrderCarNos(orderCarNosList);
        }

        log.info("客户扫码支付费用={}，车辆编号={}",freightFee,orderCarNosList.toString());
        om.setAmount(freightFee);
        pingxxMetaData.setLoginId(String.valueOf(sweepCodeDto.getLoginId()));
        pingxxMetaData.setOrderCarIds(orderCarNosList);
        pingxxMetaData.setChannel(sweepCodeDto.getChannel());
        pingxxMetaData.setTaskId(String.valueOf(sweepCodeDto.getTaskId()));
        pingxxMetaData.setTaskCarIdList(sweepCodeDto.getTaskCarIdList());

        if(sweepCodeDto.getClientType()==ClientEnum.APP_DRIVER.code){
            om.setSubject("司机端收款码功能!");
            om.setBody("司机端生成二维码！");
            pingxxMetaData.setLoginType(String.valueOf(UserTypeEnum.DRIVER.code));
            pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.DRIVER_COLLECT_QRCODE.getCode()));
        }else{
            om.setSubject("业务员收款码功能!");
            om.setBody("业务员端生成二维码！");
            pingxxMetaData.setLoginType(String.valueOf(UserTypeEnum.ADMIN.code));
            pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.SALESMAN_COLLECT_QRCODE.getCode()));
        }
        pingxxMetaData.setClientType(String.valueOf(sweepCodeDto.getClientType()));
        om.setPingxxMetaData(pingxxMetaData);
        om.setDescription("韵车订单号："+om.getPingxxMetaData().getOrderNo());

        charge = createDriverCode(om);

        cStransactionService.saveTransactions(charge, "0");
       /* } catch (Exception e) {
            log.error("扫码支付异常",e);
        }*/
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
        app.put("id", om.getPingxxMetaData().getPingAppId());
        params.put("app", app);
        params.put("channel", om.getPingxxMetaData().getChannel());// alipay_qr 支付宝扫码支付 /wx_pub_qr 微信扫码支付
        params.put("amount", om.getAmount()); // 订单总金额，单位：分, 必传
        params.put("client_ip", om.getClientIp()); // 客户端的 IP 地址 (IPv4 格式，要求商户上传真实的，渠道可能会判断), 必传
        params.put("currency", "cny"); // 仅支持人民币 cny, 必传
        params.put("subject", om.getSubject()); // 商品的标题, 必传
        params.put("body", om.getBody()); // 商品的描述信息, 必传
        params.put("description", om.getDescription()); // 备注：订单号

        Map<String, Object> meta = BeanMapUtil.beanToMap(om.getPingxxMetaData());
        params.put("metadata",meta);//自定义参数
        //当为微信支付是需要product_id
        if("wx_pub_qr".equals(om.getPingxxMetaData().getChannel())){
            Map<String, Object> extra  = new HashMap<String,Object>();
            extra.put("product_id", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis());
            params.put("extra", extra);
        }
        log.info("createDriverCode meta ="+meta.toString());
        params.put("metadata",meta);//自定义参数
        log.info("createDriverCode params ="+params.toString());
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

        log.info("validateCarPayState validateSweepCodeDto.getClientType()= "+validateSweepCodeDto.getClientType()+" validateSweepCodeDto.getCode()="+validateSweepCodeDto.getCode()
                +" addLock="+addLock);
        if(validateSweepCodeDto.getClientType()!=null){
            if(validateSweepCodeDto.getClientType().equals("4")||validateSweepCodeDto.getClientType().equals("2")){
                if(validateSweepCodeDto.getCode()==null){
                    return BaseResultUtil.fail("缺少收车码");
                }
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

        if(validateSweepCodeDto.getClientType()!=null){
            if(validateSweepCodeDto.getClientType().equals("4")||validateSweepCodeDto.getClientType().equals("2")){
                if(order.getBackContactPhone()!=null){
                    if(!csSmsService.validateCaptcha(order.getBackContactPhone(),validateSweepCodeDto.getCode(), CaptchaTypeEnum.CONFIRM_RECEIPT,
                            validateSweepCodeDto.getClientType().equals("4")?ClientEnum.APP_DRIVER:ClientEnum.APP_SALESMAN)){
                        return BaseResultUtil.fail("验证码输入错误");
                    }
                }
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
                    return BaseResultUtil.fail("订单车辆{0}已交付，请刷新后重试", orderCar.getNo());
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
                        isNeedPay = 0;
                    }else{
                        isNeedPay = 1;
                        if(CustomerTypeEnum.COOPERATOR.code == order.getCustomerType()){
                            amount = amount.add(orderCar.getTotalFee());
                        }else{
                            amount = amount.add(orderCar.getTotalFee()).subtract(orderCar.getCouponOffsetFee());
                        }

                        if(amount.compareTo(BigDecimal.ZERO) <= 0){
                            //金额验证
                            return BaseResultUtil.fail("获取金额失败");
                            //isNeedPay = 0;
                        }
                    }

                }
                orderCarNos.add(orderCar.getNo());
            }
            if(CollectionUtils.isEmpty(orderCarNos)){
                return BaseResultUtil.fail("至少包含一辆车编号");
            }
            if(amount.compareTo(BigDecimal.ZERO) <= 0){
                //金额验证
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
        Waybill waybill = waybillDao.selectById(waybillId);
        try{
            if(waybill != null){
                lock.lock();
                try{
                    TradeBill tradeBill = cStransactionService.getTradeBillByOrderNoAndType(String.valueOf(waybillId),ChargeTypeEnum.UNION_PAY.getCode());
                    if(tradeBill != null){
                        if(tradeBill.getState()==1){
                            log.error("运费正在支付中,请勿重复支付 waybillId = {}", waybillId);
                            return BaseResultUtil.fail("运费正在支付中,请勿重复支付");
                        }
                        if(tradeBill.getState()==2){
                            log.error("运费已支付完成,请勿重复支付 waybillId = {}", waybillId);
                            return BaseResultUtil.fail("运费已支付完成,请勿重复支付");
                        }
                    }

                    waybill = waybillDao.selectById(waybillId);
                    if(waybill.getFreightPayState()==2){
                        log.error("运费正在支付,请勿重复支付 waybillId = {}", waybillId);
                        return BaseResultUtil.fail("运费正在支付,请勿重复支付");
                    }
                    if(waybill.getFreightPayState()==1){
                        log.error("运费已支付完成,请勿重复支付 waybillId = {}", waybillId);
                        return BaseResultUtil.fail("运费已支付完成,请勿重复支付");
                    }
                    //TODO 再次校验

                    Long carrierId = waybill.getCarrierId();
                    BaseCarrierVo baseCarrierVo = carrierDao.showCarrierById(carrierId);
                    log.info("【通联代付支付运费】运单Id{},支付状态 state {}",waybillId,waybill.getFreightPayState());

                    //新增打款记录日志
                    savePaymentRecord(carrierId,waybillId);

                    Config config = configDao.getByItemKey("external_pay");
                    if(config!=null&&config.getState()==1){//对外支付模式
                        try{
                            log.info("【对外支付模式】运单Id {}",waybillId);
                            ExternalPayment ep = externalPaymentDao.getByWayBillId(waybillId);

                            if(ep==null){
                                //新增对外支付打款日志详情
                                ExternalPayment externalPayment = new ExternalPayment();
                                externalPayment.setCarrierId(carrierId);
                                externalPayment.setWaybillId(waybillId);
                                externalPaymentDao.insert(externalPayment);
                            }else{
                                log.error("【承运商对外打款日志新增失败】 waybillId = {} 已存在", waybillId);
                            }

                        }catch (Exception e){
                            log.error("【承运商对外打款日志新增异常】 waybillId = {}", waybillId);
                            log.error(e.getMessage(), e);
                        }
                    }else{//自动打款模式
                        log.info("【自动打款模式】运单Id {}",waybillId);
                        if(waybill != null && waybill.getFreightPayState()==0 && waybill.getFreightFee().compareTo(BigDecimal.ZERO)>0){
                            if(baseCarrierVo!=null){
                                if(baseCarrierVo.getSettleType()==0){
                                    if(baseCarrierVo.getCardName()!=null && baseCarrierVo.getCardNo()!=null
                                            && baseCarrierVo.getBankCode()!=null){
                                        BigDecimal fee = contrastAmount(carrierId);
                                        log.info("【自动打款模式】运单Id {},fee = {},要打款金额{}",waybillId,fee,waybill.getFreightFee());
                                        if(waybill.getFreightFee().compareTo(fee)<=0){
                                            Transfer transfer = allinpayTransferDriverCreate(baseCarrierVo,waybill);
                                            log.debug("【自动打款模式，通联代付支付运费】运单{}，支付运费，账单{}", waybill.getNo(), transfer);
                                            tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"2");
                                            cStransactionService.saveTransactions(transfer, "1");
                                        }

                                    }else{
                                        log.error("【自动打款模式，通联代付支付运费】收款人信息不全 waybillId = {}", waybillId);
                                        addPaymentErrorLog("auto allinpay 收款人信息不全 waybillId ="+waybillId);
                                        tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-1");//付款失败
                                        return BaseResultUtil.fail("通联代付失败,收款人信息不全");
                                    }
                                }else{
                                    log.error("【自动打款模式，通联代付支付运费】收款人为账期用户 waybillId = {}", waybillId);
                                    addPaymentErrorLog("auto allinpay 收款人为账期用户 waybillId = "+waybillId);
                                    tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-1");//付款失败
                                    return BaseResultUtil.fail("通联代付失败,收款人为账期用户");
                                }
                            }else{
                                log.error("【自动打款模式，通联代付支付运费】收款人不存在 waybillId = {}", waybillId);
                                addPaymentErrorLog("auto allinpay 收款人不存在 waybillId = "+waybillId);
                                tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-1");//付款失败
                                return BaseResultUtil.fail("通联代付失败,收款人不存在");
                            }

                        }else{
                            log.info("【自动打款模式，通联代付支付运费】运单{}，支付运费为0", waybill.getNo());
                            cStransactionService.updateWayBillPayStateNoPay(waybillId, System.currentTimeMillis());
                        }
                    }

                }finally {
                    lock.unlock();
                }

            }
        }catch (Exception e){
            log.error("【自动打款模式，通联代付支付运费】运单{}，支付运费支付失败", waybill.getNo());
            log.error(e.getMessage(), e);
            addPaymentErrorLog("【自动打款模式，通联代付支付运费】运单"+waybill.getNo()+"，支付运费支付失败");
            tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-1");//付款失败
            return BaseResultUtil.fail("通联代付失败");
        }

        return BaseResultUtil.success("通联代付成功");
    }

    /**
     * 对比承运商应得运费与已得运费
     */
    private BigDecimal contrastAmount(Long carrierId){

        BigDecimal wlFeeCount = cStransactionService.getWlFeeCount(carrierId);//应得费用

        BigDecimal receiveFeeCount = cStransactionService.getReceiveFeeCount(carrierId);//已得费用

        return wlFeeCount.subtract(receiveFeeCount);
    }

    private void savePaymentRecord(Long carrierId,Long waybillId){
        try{
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setCarrierId(carrierId);
            paymentRecord.setWaybillId(waybillId);
            paymentRecord.setType(0);
            paymentRecord.setCreateTime(System.currentTimeMillis());
            addPaymentRecord(paymentRecord);
        }catch (Exception e){
            log.error("【新增打款记录日志失败】 waybillId = {}", waybillId);
        }
    }

    /**
     * 新增打款记录
     * @param paymentRecord
     */
    private void addPaymentRecord(PaymentRecord paymentRecord){
        paymentRecordDao.insert(paymentRecord);
    }

    private void addPaymentErrorLog(String remark){
        PaymentErrorLog paymentErrorLog = new PaymentErrorLog();
        paymentErrorLog.setRemark(remark);
        paymentErrorLog.setCreateTime(System.currentTimeMillis());
        paymentErrorLogDao.insert(paymentErrorLog);
    }

    @Override
    public ResultVo allinpayToCarrierNew(Long waybillId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException {
        Waybill waybill = waybillDao.selectById(waybillId);
        try{
            if(waybill != null){
                lock.lock();
                try{
                    TradeBill tradeBill = cStransactionService.getTradeBillByOrderNoAndType(String.valueOf(waybillId),ChargeTypeEnum.UNION_PAY.getCode());
                    if(tradeBill != null){
                        if(tradeBill.getState()==1){
                            log.error("运费正在支付中,请勿重复支付 waybillId = {}", waybillId);
                            return BaseResultUtil.fail("运费正在支付中,请勿重复支付");
                        }
                        if(tradeBill.getState()==2){
                            log.error("运费已支付完成,请勿重复支付 waybillId = {}", waybillId);
                            return BaseResultUtil.fail("运费已支付完成,请勿重复支付");
                        }

                    }

                    waybill = waybillDao.selectById(waybillId);
                    if(waybill.getFreightPayState()==2){
                        log.error("运费正在支付,请勿重复支付 waybillId = {}", waybillId);
                        return BaseResultUtil.fail("运费正在支付,请勿重复支付");
                    }
                    if(waybill.getFreightPayState()==1){
                        log.error("运费已支付完成,请勿重复支付 waybillId = {}", waybillId);
                        return BaseResultUtil.fail("运费已支付完成,请勿重复支付");
                    }

                    //TODO 再次校验

                    Long carrierId = waybill.getCarrierId();
                    BaseCarrierVo baseCarrierVo = carrierDao.showCarrierById(carrierId);
                    log.info("【对外支付模式，通联代付支付运费】运单Id{},支付状态 state {}",waybillId,waybill.getFreightPayState());
                    if(waybill != null && waybill.getFreightPayState()==0 && waybill.getFreightFee().compareTo(BigDecimal.ZERO)>0){

                        if(baseCarrierVo!=null){
                            if(baseCarrierVo.getSettleType()==0){
                                if(baseCarrierVo.getCardName()!=null && baseCarrierVo.getCardNo()!=null
                                        && baseCarrierVo.getBankCode()!=null){
                                    BigDecimal fee = contrastAmount(carrierId);
                                    log.info("【自动打款模式】运单Id {},fee = {},要打款金额{}",waybillId,fee,waybill.getFreightFee());
                                    if(waybill.getFreightFee().compareTo(fee)<=0){
                                        Transfer transfer = allinpayTransferDriverCreate(baseCarrierVo,waybill);
                                        log.debug("【对外支付模式，通联代付支付运费】运单{}，支付运费，账单{}", waybill.getNo(), transfer);
                                        tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"2");//打款中
                                        cStransactionService.saveTransactions(transfer, "1");
                                    }

                                }else{
                                    log.error("【对外支付模式，通联代付支付运费】收款人信息不全 waybillId = {}", waybillId);
                                    addPaymentErrorLog("external allinpay 收款人信息不全 waybillId ="+waybillId);
                                    tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-2");//付款失败
                                    return BaseResultUtil.fail("通联代付失败,收款人信息不全");
                                }
                            }else{
                                log.error("【对外支付模式，通联代付支付运费】收款人为账期用户 waybillId = {}", waybillId);
                                addPaymentErrorLog("external allinpay 收款人为账期用户 waybillId = "+waybillId);
                                tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-2");//付款失败
                                return BaseResultUtil.fail("通联代付失败,收款人为账期用户");
                            }
                        }else{
                            log.error("【对外支付模式，通联代付支付运费】收款人不存在 waybillId = {}", waybillId);
                            addPaymentErrorLog("external allinpay 收款人不存在 waybillId = "+waybillId);
                            tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-2");//付款失败
                            return BaseResultUtil.fail("通联代付失败,收款人不存在");
                        }

                    }else{
                        log.debug("【对外支付模式，通联代付支付运费】运单{}，支付运费为0", waybill.getNo());
                        cStransactionService.updateWayBillPayStateNoPay(waybillId, System.currentTimeMillis());
                    }
                }finally {
                    lock.unlock();
                }

            }
        }catch (Exception e){
            log.error("【对外支付模式，通联代付支付运费】运单{}，支付运费支付失败", waybill.getNo());
            log.error(e.getMessage(), e);
            addPaymentErrorLog("external allinpay 运单"+waybill.getNo()+"，支付运费支付失败");
            tradeBillDao.updateWayBillPayState(waybillId,null, System.currentTimeMillis(),"-2");//付款失败
            return BaseResultUtil.fail("通联代付失败");
        }

        return BaseResultUtil.success("通联代付成功");
    }

    /**
     * 对比合伙人应得运费与已得运费
     */
    public BigDecimal contrastCooperatorAmount(Long customId){

        BigDecimal orderCount = cStransactionService.getCooperatorServiceFeeCount(customId);//总订单运费

        BigDecimal orderCarCount = cStransactionService.getCooperatorServiceFeeCarCount(customId);//总车辆运费

        BigDecimal deservedFee = orderCount.subtract(orderCarCount);//应得费用

        BigDecimal receiveOrderCount = cStransactionService.getCooperatorServiceReceiveFeeCount(customId);

        BigDecimal receiveOrderCarCount = cStransactionService.getCooperatorServiceReceiveCarFeeCount(customId);//

        BigDecimal receiverFee = receiveOrderCount.subtract(receiveOrderCarCount);

        return deservedFee.subtract(receiverFee);
    }

    @Override
    public ResultVo allinpayToCooperator(Long orderId) throws FileNotFoundException, RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException {
        log.info("完成订单（ID：{}），支付合伙人服务费", orderId);
        //支付校验
        Order order = null;
        try{
            order = orderDao.selectById(orderId);

            if(order!=null){
                lock.lock();
                try{
                    log.debug("【通联代付支付服务费】订单{}，准备支付服务费", order.getNo());
                    //TODO 什么时间什么情况下创建账单，查询最后一条的依据？？
                    TradeBill tradeBill = cStransactionService.getTradeBillByOrderNoAndType(order.getNo(),ChargeTypeEnum.UNION_PAY_PARTNER.getCode());
                    log.debug("【通联代付支付服务费】订单{}，支付服务费，账单内容{}", order.getNo(), tradeBill);
                    if(tradeBill != null){

                        if(tradeBill.getState()==1){
                            log.error("合伙人服务费正在支付中,请勿重复支付 orderId = {}", orderId);
                            return BaseResultUtil.fail("合伙人服务费正在支付中,请勿重复支付");
                        }
                        if(tradeBill.getState()==2){
                            log.error("合伙人服务费已支付完成,请勿重复支付 orderId = {}", orderId);
                            return BaseResultUtil.fail("合伙人服务费已支付完成,请勿重复支付");
                        }

                    }

                    order = orderDao.selectById(orderId);
                    if(order.getFlag()!=null){
                        if(order.getFlag().equals("1")){
                            log.error("合伙人服务费支付中,请勿重复支付 orderId = {}", orderId);
                            return BaseResultUtil.fail("合伙人服务费支付中,请勿重复支付");
                        }
                        if(order.getFlag().equals("2")){
                            log.error("合伙人服务费已支付完成,请勿重复支付 orderId = {}", orderId);
                            return BaseResultUtil.fail("合伙人服务费已支付完成,请勿重复支付");
                        }

                    }

                    Long customId = order.getCustomerId();
                    ShowPartnerVo showPartnerVo = customerDao.showPartner(customId);

                    BigDecimal wlFee = getFee(orderId);
                    log.info("支付合伙人服务费wlFee={},orderId ={}",wlFee,orderId);
                    //新增打款记录日志
                    savePaymentRecord(orderId);
                    //TODO 金额空指针
                    BigDecimal payableFee = order.getTotalFee().subtract(wlFee).add(order.getCouponOffsetFee());//给合伙人费用
                    log.info("支付合伙人服务费 payableFee={},orderId ={}",payableFee,orderId);
                    if(payableFee.compareTo(BigDecimal.ZERO)>0){
                        if(showPartnerVo!=null && showPartnerVo.getCardName()!=null && showPartnerVo.getCardNo()!=null
                        && showPartnerVo.getBankCode()!=null){
                            //TODO 再次校验
                            BigDecimal fee = contrastCooperatorAmount(customId);
                            log.info("支付合伙人服务费 payableFee.compareTo(fee)={},fee={}",payableFee.compareTo(fee),fee);
                            if(payableFee.compareTo(fee)<=0){
                                Transfer transfer = allinpayToCooperatorCreate(showPartnerVo,payableFee,order.getNo(),orderId);
                                cStransactionService.updateOrderFlag(order.getNo(),"1",System.currentTimeMillis());//付款中
                                cStransactionService.saveCooperatorTransactions(transfer, "1");
                            }

                        }else{
                            log.error("【通联代付支付合伙人费用】收款人信息不全 orderId = {}", orderId);
                            addPaymentErrorLog("【通联代付支付合伙人费用】收款人信息不全 orderId = "+orderId);
                            cStransactionService.updateOrderFlag(order.getNo(),"-2",System.currentTimeMillis());//付款失败
                            return BaseResultUtil.fail("通联代付支付合伙人费用失败,收款人信息不全");
                        }
                    }else{
                        log.info("【通联代付支付合伙人费用】为0,orderId = {}",orderId);
                    }

                }finally {
                    lock.unlock();
                }

            }else{
                addPaymentErrorLog("合伙人通联代付失败,订单"+orderId+"不存在");
                return BaseResultUtil.fail("合伙人通联代付失败,订单{}不存在", orderId);
            }
        }catch (Exception e){
            log.error("【通联代付支付服务费】订单{}，支付服务费失败", order.getNo());
            log.error(e.getMessage(), e);
            addPaymentErrorLog("allinpay 订单"+order.getNo()+"，支付服务费失败");
            cStransactionService.updateOrderFlag(order.getNo(),"-2",System.currentTimeMillis());//付款失败
            return BaseResultUtil.fail("合伙人通联代付异常");
        }

        return BaseResultUtil.success("合伙人通联代付成功");
    }

    private BigDecimal getFee(Long orderId){
        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(orderId);
        BigDecimal wlFee = new BigDecimal(0);
        for (int i=0;i<orderCarList.size();i++){
            OrderCar orderCar = orderCarList.get(i);
            if(orderCar!=null){
                if(orderCar.getPickFee()!=null){
                    wlFee = wlFee.add(orderCar.getPickFee());
                }
                if(orderCar.getTrunkFee()!=null){
                    wlFee = wlFee.add(orderCar.getTrunkFee());
                }
                if(orderCar.getBackFee()!=null){
                    wlFee = wlFee.add(orderCar.getBackFee());
                }
                if(orderCar.getAddInsuranceFee()!=null){
                    wlFee = wlFee.add(orderCar.getAddInsuranceFee());
                }
            }
        }

        return wlFee;
    }

    private void savePaymentRecord(Long orderId){
        try{
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setOrderId(orderId);
            paymentRecord.setType(2);
            paymentRecord.setCreateTime(System.currentTimeMillis());
            addPaymentRecord(paymentRecord);
        }catch (Exception e){
            log.error("新增合伙人打款日志详情失败 orderId = {}", orderId);
            log.error(e.getMessage(), e);
        }
    }

    private Transfer allinpayToCooperatorCreate(ShowPartnerVo showPartnerVo, BigDecimal payableFee, String orderNo, Long orderId) throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException, FileNotFoundException {
        initPingApiKey();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> app = new HashMap<>();
        app.put("id", PingProperty.customerAppId);
        params.put("app", app);
        // 付款使用的商户内部订单号。 allinpay 限长20-40位不能重复的数字字母组合，必须以签约的通联的商户号开头（建议组合格式：通联商户号 + 时间戳 + 固定位数顺序流水号，不包含+号）
        params.put("order_no", PingProperty.businessCode + System.currentTimeMillis());

        // 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100,企业付款最小发送金额为1 元）
        //确认手续费
        BigDecimal amount = payableFee;
        BigDecimal src_amount = amount;
        params.put("amount", amount);
        // 目前支持 支付宝：alipay，银联：unionpay，微信公众号：wx_pub，通联：allinpay，京东：jdpay 余额：balance
        params.put("channel", "allinpay");
        params.put("type", "b2c");//付款类型，转账到个人用户为 b2c，转账到企业用户为 b2b（wx、wx_pub、wx_lite 和 balance 渠道的企业付款，仅支持 b2c）
        params.put("currency", "cny");
        params.put("description", "通联代付合伙人费用");

        Map<String, String> extra = new HashMap<String, String>();
        //1~100位，收款人姓名。必须
        extra.put("user_name", showPartnerVo.getCardName());

        //1~32位，收款人银行卡号或者存折号。 必须
        extra.put("card_number", showPartnerVo.getCardNo());
        //4位，开户银行编号，详情请参考通联代付银行编号说明。 必须
        extra.put("open_bank_code",showPartnerVo.getBankCode());

        params.put("extra", extra);

        PingxxMetaData pingxxMetaData = new PingxxMetaData();
        pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.UNION_PAY_PARTNER.getCode()));
        pingxxMetaData.setLoginId(String.valueOf(showPartnerVo.getCustomerId()));
        pingxxMetaData.setOrderNo(orderNo);
        pingxxMetaData.setOrderId(String.valueOf(orderId));

        Map<String, Object> meta = BeanMapUtil.beanToMap(pingxxMetaData);
        params.put("metadata",meta);//自定义参数

        Transfer obj = Transfer.create(params);
        if(Pingpp.apiKey.contains("_test_")){//test模式调用查询相当于企业付款成功
            obj = transferRetrieve(obj.getId());
        }
        obj.setAmount(Integer.parseInt(src_amount.toString()));
        return obj;

    }

    @Override
    public Charge prePay(WebPrePayDto prePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException {

        OrderModel om = new OrderModel();
        //创建Charge对象
        Charge charge = new Charge();

        String orderNo = prePayDto.getOrderNo();
        TradeBill tradeBill = cStransactionService.getTradeBillByOrderNo(orderNo);
        if(tradeBill != null){
            com.cjyc.common.model.entity.Order o = orderDao.findByNo(orderNo);
            if(o!=null){
                if(o.getWlPayState()==2){
                    throw new CommonException("订单已支付完成","1");
                }
            }
        }/*else{
            log.info("webPrePay orderNo ="+orderNo);
            String lockKey =getRandomNoKey(orderNo);
            if (!redisLock.lock(lockKey, 1800000, 99, 200)) {
                throw new CommonException("订单正在支付中","1");
            }
        }*/
        try {
            om.setClientIp(prePayDto.getIp());
            BigDecimal wlFee = cStransactionService.getAmountByOrderNo(prePayDto.getOrderNo());
            om.setClientIp(prePayDto.getIp());
            om.setUid(String.valueOf(prePayDto.getUid()));
            om.setAmount(wlFee);
            om.setSubject(ChargeTypeEnum.WEB_PREPAY_QRCODE.getName());
            om.setBody("后台订单预付款");

            PingxxMetaData pingxxMetaData = new PingxxMetaData();
            pingxxMetaData.setPingAppId(PingProperty.userAppId);
            pingxxMetaData.setChannel(prePayDto.getChannel());
            pingxxMetaData.setOrderNo(prePayDto.getOrderNo());
            pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.WEB_PREPAY_QRCODE.getCode()));
            pingxxMetaData.setClientType(String.valueOf(ClientEnum.WEB_SERVER.code));
            pingxxMetaData.setLoginId(prePayDto.getUid());
            pingxxMetaData.setLoginType(String.valueOf(UserTypeEnum.ADMIN.code));

            om.setPingxxMetaData(pingxxMetaData);
            // 备注：订单号
            om.setDescription("韵车订单号："+om.getPingxxMetaData().getOrderNo());

            charge = createDriverCode(om);

            cStransactionService.saveWebPrePayTransactions(charge, "0");
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
        //创建Charge对象
        Charge charge = new Charge();
        try {
            List<String> tempList = webOutOfStockDto.getTaskCarIdList();

            List<Long> taskCarIdList = convertToLongList(tempList);
            List<String> orderCarNosList = cStransactionService.getOrderCarNosByTaskCarIds(taskCarIdList);
            BigDecimal freightFee = cStransactionService.getAmountByOrderCarNos(orderCarNosList);
            om.setAmount(freightFee);

            om.setSubject(ChargeTypeEnum.WEB_OUT_STOCK_QRCODE.getName());
            om.setBody("确认出库生成二维码！");

            PingxxMetaData pingxxMetaData = new PingxxMetaData();
            pingxxMetaData.setPingAppId(PingProperty.userAppId);
            pingxxMetaData.setOrderCarIds(orderCarNosList);
            pingxxMetaData.setTaskId(String.valueOf(webOutOfStockDto.getTaskId()));
            pingxxMetaData.setTaskCarIdList(webOutOfStockDto.getTaskCarIdList());
            pingxxMetaData.setChannel(webOutOfStockDto.getChannel());
            pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.WEB_OUT_STOCK_QRCODE.getCode()));
            pingxxMetaData.setClientType(String.valueOf(ClientEnum.WEB_SERVER.code));

            pingxxMetaData.setLoginId(String.valueOf(webOutOfStockDto.getLoginId()));
            pingxxMetaData.setLoginType(String.valueOf(UserTypeEnum.ADMIN.code));

            om.setPingxxMetaData(pingxxMetaData);
            om.setDescription("韵车订单号："+om.getPingxxMetaData().getOrderNo());

            charge = createDriverCode(om);

            cStransactionService.saveTransactions(charge, "0");
        } catch (Exception e) {
            log.error("扫码支付异常",e);
        }
        return charge;
    }

    @Override
    public Charge salesPrePay(SalesPrePayDto salesPrePayDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException {
        OrderModel om = new OrderModel();
        //创建Charge对象
        Charge charge = new Charge();

        String orderNo = salesPrePayDto.getOrderNo();
        TradeBill tradeBill = cStransactionService.getTradeBillByOrderNo(orderNo);
        if(tradeBill != null){
            com.cjyc.common.model.entity.Order o = orderDao.findByNo(orderNo);
            if(o!=null){
                if(o.getWlPayState()==2){
                    throw new CommonException("订单已支付完成","1");
                }
            }
        }/*else{
            log.info("salesPrePay orderNo ="+orderNo);
            String lockKey =getRandomNoKey(orderNo);
            if (!redisLock.lock(lockKey, 1800000, 99, 200)) {
                throw new CommonException("订单正在支付中","1");
            }
        }*/
        //try {
        om.setClientIp(salesPrePayDto.getIp());
        BigDecimal wlFee = cStransactionService.getAmountByOrderNo(salesPrePayDto.getOrderNo());
        om.setClientIp(salesPrePayDto.getIp());
        om.setAmount(wlFee);
        om.setSubject(ChargeTypeEnum.SALES_PREPAY_QRCODE.getName());
        om.setBody("业务员订单预付款");

        PingxxMetaData pingxxMetaData = new PingxxMetaData();
        pingxxMetaData.setPingAppId(PingProperty.userAppId);
        pingxxMetaData.setChannel(salesPrePayDto.getChannel());
        pingxxMetaData.setOrderNo(salesPrePayDto.getOrderNo());
        pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.SALES_PREPAY_QRCODE.getCode()));
        pingxxMetaData.setClientType(String.valueOf(ClientEnum.App_PREPAY_SALESMAN));
        pingxxMetaData.setLoginId(String.valueOf(salesPrePayDto.getLoginId()));
        pingxxMetaData.setLoginType(String.valueOf(UserTypeEnum.ADMIN.code));

        om.setPingxxMetaData(pingxxMetaData);
        // 备注：订单号
        om.setDescription("韵车订单号："+om.getPingxxMetaData().getOrderNo());

        charge = createDriverCode(om);

        cStransactionService.saveSalesPrePayTransactions(charge, "0");
        /*} catch (Exception e) {
            log.error("业务员预付款出示二维码异常",e);
        }*/
        return charge;
    }

    @Override
    public ResultVo unlock(String orderNo) {
        try {
            String key = getRandomNoKey(orderNo);
            redisUtils.delete(key);
        }catch (Exception e){
            return BaseResultUtil.fail("解锁预付单失败");
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo unlockQrcode(String orderCarNo) {
        try{
            String key = RedisKeys.getWlCollectPayLockKey(orderCarNo);
            redisUtils.delete(key);
        }catch (Exception e){
            return BaseResultUtil.fail("解锁扫码付款");
        }
        return BaseResultUtil.success();
    }

    @Override
    public void cancelOrderRefund(Long orderId) {
        log.info("cancelOrderRefund 退款，orderId = {}",orderId);
        lock.lock();
        try{
            Order order = orderDao.selectById(orderId);
            if(order == null || order.getNo()==null){
                return;
            }
            if(PayStateEnum.PAID.code != order.getWlPayState()) {
                return;
            }
            String orderNo = order.getNo();
            //先查询 是否已退款
            Refund refund = orderRefundDao.selectByOrderCode(orderNo);
            if(refund!=null){
                if(refund.getState()==2){
                    log.error("已退款 orderNo={}",orderNo);
                }else if(refund.getState()==0){
                    log.error("已申请退款，请勿重复操作 orderNo={}",orderNo);
                }
            }else{
                String description = "订单号：" + orderNo + "，退款";
                if(StringUtils.isBlank(description)){
                    log.error("退款异常，description不能为空。");
                }else{
                    log.info("退款 start，orderId = {}",orderId);
                    TradeBill tradeBill = cStransactionService.getTradeBillByOrderNo(orderNo);
                    String pingPayId = tradeBill.getPingPayId();
                    initPingApiKey();
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("description", description); // 必传
                    params.put("refund_mode", "to_source");//退款方式 原路退回

                    PingxxMetaData pingxxMetaData = new PingxxMetaData();
                    pingxxMetaData.setOrderNo(orderNo);

                    Map<String, Object> meta = BeanMapUtil.beanToMap(pingxxMetaData);
                    params.put("metadata",meta);//自定义参数
                    OrderRefund.create(pingPayId, params);

                    //添加退款记录
                    try{
                        Refund refund1 = new Refund();
                        refund1.setOrderNo(orderNo);
                        refund1.setState(0);
                        refund1.setCreateTime(System.currentTimeMillis());
                        orderRefundDao.insert(refund1);
                    }catch (Exception e){
                        log.error("添加退款记录异常 orderNo={}",orderNo);
                        log.error(e.getMessage(),e);
                    }
                }

            }

        }catch (Exception e){
            log.error("退款异常 orderId={}",orderId);
            log.error(e.getMessage(),e);
        }finally {
            lock.unlock();
        }
    }

    private String getRandomNoKey(String prefix) {
        return "cjyc:random:no:prepay:" + prefix;
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
        extra.put("open_bank_code",baseCarrierVo.getBankCode());

        params.put("extra", extra);

        PingxxMetaData pingxxMetaData = new PingxxMetaData();
        pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.UNION_PAY.getCode()));
        pingxxMetaData.setWaybillId(String.valueOf(waybill.getId()));
        pingxxMetaData.setLoginId(String.valueOf(baseCarrierVo.getCarrierId()));

        Map<String, Object> meta = BeanMapUtil.beanToMap(pingxxMetaData);
        params.put("metadata",meta);//自定义参数

        log.info("allinpayTransferDriverCreate params={}",params.toString());
        Transfer obj = Transfer.create(params);
        if(Pingpp.apiKey.contains("_test_")){//test模式调用查询相当于企业付款成功
            obj = transferRetrieve(obj.getId());
        }
        obj.setAmount(Integer.parseInt(src_amount.toString()));
        return obj;
    }

    public Transfer transferRetrieve(String transferId) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, RateLimitException{
        // 参数: transfer id
        Transfer obj = Transfer.retrieve(transferId);
        return obj;
    }
}
