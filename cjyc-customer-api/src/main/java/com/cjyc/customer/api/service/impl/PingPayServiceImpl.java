package com.cjyc.customer.api.service.impl;

import com.Pingxx.model.MetaDataEntiy;
import com.Pingxx.model.PingOrderModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.customer.order.CarCollectPayDto;
import com.cjyc.common.model.dto.customer.order.CarPayStateDto;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.TradeBill;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.exception.CommonException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.Pingxx.model.Order;
import com.Pingxx.model.OrderRefund;
import com.cjyc.common.system.service.ICsPingxxService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.util.RedisLock;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.customer.api.config.PingProperty;
import com.cjyc.customer.api.dto.OrderModel;
import com.cjyc.customer.api.service.IPingPayService;
import com.cjyc.customer.api.service.ITransactionService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author:Hut
 * @date:2019/11/20 14:57
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class PingPayServiceImpl implements IPingPayService {
    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICsPingxxService csPingxxService;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICsSendNoService csSendNoService;

    @Resource
    private RedisLock redisLock;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ITransactionService transactionService;

    @Override
    public Order prePay(PrePayDto reqDto) {

        PingOrderModel pm = new PingOrderModel();
        pm.setAmount(1000);
        pm.setCurrency("cny");
        pm.setMerchantOrderNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));
        pm.setLivemode(false);
        pm.setObject("charge");
        pm.setApp(PingProperty.customerAppId);
        pm.setClientIp(reqDto.getIp());
        pm.setSubject("预付款");
        pm.setBody("订单预付款");
        //meta信息
        MetaDataEntiy metaDataEntiy = new MetaDataEntiy();
        metaDataEntiy.setChannel(reqDto.getChannel());
        metaDataEntiy.setClientId(String.valueOf(ClientEnum.APP_CUSTOMER.code));
        metaDataEntiy.setChargeType("1");
        metaDataEntiy.setSourceNos(reqDto.getOrderNo());
        metaDataEntiy.setLoginId(reqDto.getUid());
        pm.setMetaDataEntiy(metaDataEntiy);
        Order pingOrder = null;
        try {
            pingOrder = csPingxxService.payOrderByModel(pm);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pingOrder;
    }
    
    @Override
    public Order pay(PrePayDto reqDto) {
        String orderNo = reqDto.getOrderNo();
        OrderModel om = new OrderModel();
        Order order = new Order();

        TradeBill tradeBill = transactionService.getTradeBillByOrderNo(reqDto.getOrderNo());
        if(tradeBill != null){
            throw new CommonException("订单已支付完成","1");
        }else{
            String lockKey =getRandomNoKey(orderNo);
            if (!redisLock.lock(lockKey, 30000, 99, 200)) {
                throw new CommonException("订单正在支付中","1");
            }
        }
        try {
            BigDecimal wlFee = transactionService.getAmountByOrderNo(reqDto.getOrderNo());
            om.setPingAppId(PingProperty.customerAppId);
            om.setClientIp(reqDto.getIp());
            om.setChannel(reqDto.getChannel());
            om.setOrderNo(reqDto.getOrderNo());
            om.setUid(String.valueOf(reqDto.getUid()));
            om.setAmount(wlFee);
            om.setSubject("预付款");
            om.setBody("订单预付款");
            om.setChargeType("1");
            om.setClientType(String.valueOf(ClientEnum.APP_CUSTOMER.code));
            // 备注：订单号
            om.setDescription("韵车订单号："+om.getOrderNo());

            order = payOrder(om);
            log.debug(order.toString());
            transactionService.saveTransactions(order, "0");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return order;
    }

    private String getRandomNoKey(String prefix) {
        return "cjyc:random:no:prepay:" + prefix;
    }

    private Order payOrder(OrderModel om) throws InvalidRequestException, APIException,
            ChannelException, RateLimitException, APIConnectionException, AuthenticationException,FileNotFoundException {
        Order order = createOrder(om);
        if(order != null){
            order = payOrder(om.getPingAppId(),om.getChannel(), om.getAmount(), order.getId());
            return order;
        }
        return order;
    }

    private Order createOrder(OrderModel om) throws RateLimitException, APIException,
            ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException,FileNotFoundException {

        initPingApiKey();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", om.getUid()); // 用户在当前 app 下的 User ID, 可选
        params.put("app", om.getPingAppId()); // App ID, 必传
        Calendar calendar = Calendar.getInstance();
        params.put("merchant_order_no", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis()); // 商户订单号, 必传
        params.put("subject", om.getSubject()); // 商品的标题, 必传
        params.put("body", om.getBody()); // 商品的描述信息, 必传
        params.put("amount", om.getAmount()); // 订单总金额，单位：分, 必传
        params.put("currency", "cny"); // 仅支持人民币 cny, 必传
        params.put("client_ip", om.getClientIp()); // 客户端的 IP 地址 (IPv4 格式，要求商户上传真实的，渠道可能会判断), 必传
        params.put("description", om.getDescription()); // 备注：订单号

        Map<String, Object> meta = new HashMap<String,Object>();
        meta.put("chargeType", om.getChargeType());//0:定金	1：尾款    2:居间服务费
        meta.put("orderNo", om.getOrderNo());	//订单号
        meta.put("batch", om.getBatch());	//是否批量支付尾款
        meta.put("deductFee", om.getDeductFee());	//扣款金额
        meta.put("type", om.getClientType()); //customer 用户  bond 司机保证金  freight 司机运费收入  driver 居间服务费
        meta.put("deposit", om.getDeposit()); //定金金额
        meta.put("orderMan", om.getOrderMan()); //当前app登陆人的ID
        params.put("metadata",meta);//自定义参数
        Order order = Order.create(params); // 创建 Order 对象 方法
        return order;
    }

    /**
     * 支付订单
     * @param channel	交易渠道
     * @param charge_amount	支付金额
     * @param pingOrderId	订单ID
     * @return	订单支付凭证
     *
     * @throws RateLimitException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws AuthenticationException
     */
    public Order payOrder(String pingAppId, String channel, BigDecimal charge_amount, String pingOrderId) throws RateLimitException,
            APIException, ChannelException, InvalidRequestException,FileNotFoundException,
            APIConnectionException, AuthenticationException {
        initPingApiKey();
        Pingpp.appId = pingAppId;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channel", channel);
        params.put("charge_amount", charge_amount);
        params.put("extra", channelExtra(channel));
        Order order = Order.pay(pingOrderId, params); // 创建支付 Order 对象 方法
        return order;
    }

    private Map<String, Object> channelExtra(String channel) {
        Map<String, Object> extra = new HashMap<>();

        switch (channel) {
            case "alipay"://支付宝app
                extra = alipayExtra();
                break;
            case "wx"://微信app
                extra = wxExtra();
                break;
            case "upacp"://银联手机支付
                extra = upacpExtra();
                break;
            case "applepay_upacp"://Apple Pay
                extra = applepayUpacpExtra();
                break;
            case "cp_b2b"://银联企业网银支付
                extra = cpB2bExtra();
                break;
            case "alipay_pc_direct"://支付宝电脑网站支付
                extra = alipayPcDirectExtra();
                break;
            case "alipay_wap"://支付宝手机网站支付
                extra = alipayWapExtra();
                break;
            case "upacp_wap"://银联手机网页支付
                extra = upacpWapExtra();
                break;
        }
        return extra;
    }

    private Map<String, Object> upacpWapExtra() {
        Map<String, Object> extra = new HashMap<>();
//		extra.put("upacp_mer_id",StringUtil.getProperties("upacp_mer_id"));
//		extra.put("upacp_client_cert",StringUtil.getProperties("upacp_mer_id"));
        return null;
    }

    private Map<String, Object> alipayWapExtra() {
        Map<String, Object> extra = new HashMap<>();
        extra.put("success_url", "");
        return extra;
    }
    private Map<String, Object> alipayExtra() {
        Map<String, Object> extra = new HashMap<>();
        return extra;
    }


    private Map<String, Object> alipayPcDirectExtra() {
        Map<String, Object> extra = new HashMap<>();
        return extra;
    }

    private Map<String, Object> wxExtra() {
        Map<String, Object> extra = new HashMap<>();
        return extra;
    }

    private Map<String, Object> upacpExtra() {
        Map<String, Object> extra = new HashMap<>();
        return extra;
    }

    private Map<String, Object> applepayUpacpExtra() {
        Map<String, Object> extra = new HashMap<>();
        return extra;
    }

    private Map<String, Object> cpB2bExtra() {
        Map<String, Object> extra = new HashMap<>();
        return extra;
    }

    private void initPingApiKey() throws FileNotFoundException {
        Pingpp.apiKey = PingProperty.apiKey;
        //Pingpp.overrideApiBase("https://sapi.pingxx.com");//此接口为ping++协助测试的接口 升级完成后注释掉 20181023 add
        System.setProperty("https.protocols", "TLSv1.2");//20181023 添加 (TLSv1.2升级配置)
        Pingpp.privateKeyPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"your_rsa_private_key_pkcs.pem").getPath();
    }

    @Override
    public Charge sweepDriveCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
            APIConnectionException, AuthenticationException,FileNotFoundException{
        OrderModel om = new OrderModel();

        om.setClientIp(sweepCodeDto.getIp());
        om.setPingAppId(PingProperty.driverAppId);
        //创建Charge对象
        Charge charge = new Charge();
        try {
            BigDecimal freightFee = transactionService.getAmountByOrderCarIds(sweepCodeDto.getOrderCarIds());
            om.setAmount(freightFee);
            om.setDriver_code(sweepCodeDto.getPayeeId());
            om.setOrderCarIds(om.getOrderCarIds());
            om.setChannel(sweepCodeDto.getChannel());
            om.setSubject("司机端收款码功能!");
            om.setBody("生成二维码！");
            om.setChargeType("2");
            om.setClientType(String.valueOf(ClientEnum.APP_DRIVER.code));
            om.setDescription("韵车订单号："+om.getOrderNo());
            charge = createDriverCode(om);

        } catch (Exception e) {
            log.error("扫码支付异常",e);
        }
        return charge;
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
        meta.put("chargeType", om.getChargeType());//0:定金	1：尾款
        //自定义存储字段
        meta.put("orderNo", om.getOrderNo());	//订单号
        meta.put("orderCarIds",om.getOrderCarIds());//订单Id
        meta.put("driver_code", om.getDriver_code());//司机Code
        meta.put("order_type", om.getOrder_type());
        meta.put("driver_name", om.getDriver_name());
        meta.put("driver_phone", om.getDriver_phone());
        meta.put("back_type", om.getBack_type());
        //当为微信支付是需要product_id
        if("wx_pub_qr".equals(om.getChannel())){
            Map<String, Object> extra  = new HashMap<String,Object>();
            extra.put("product_id", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis());
            params.put("extra", extra);
        }
        params.put("metadata",meta);//自定义参数
        Charge charge = Charge.create(params); // 创建 Charge 对象 方法
        return charge;
    }

    @Override
    public Charge sweepSalesmanCode(OrderModel om) throws RateLimitException,APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException ,FileNotFoundException{
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
        meta.put("chargeType", om.getChargeType());//0:定金	1：尾款
        //自定义存储字段
        meta.put("orderNo", om.getOrderNo());	//订单号
        meta.put("orderCarIds",om.getOrderCarIds());//订单Id
        meta.put("driver_code", om.getDriver_code());//司机Code
        meta.put("order_type", om.getOrder_type());
        meta.put("driver_name", om.getDriver_name());
        meta.put("driver_phone", om.getDriver_phone());
        meta.put("back_type", om.getBack_type());
        //当为微信支付是需要product_id
        if("wx_pub_qr".equals(om.getChannel())){
            Map<String, Object> extra  = new HashMap<String,Object>();
            extra.put("product_id", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis());
            params.put("extra", extra);
        }
        params.put("metadata",meta);//自定义参数
        Charge charge = Charge.create(params); // 创建 Charge 对象 方法
        return charge;
    }

    @Override
    public void cancelOrderRefund(String orderCode) {
        try{
            String description = "订单号：" + orderCode + "，定金退款";
            if(StringUtils.isBlank(description)){
                log.error("定金退款异常，description不能为空。");
            }else if(StringUtils.isBlank(orderCode)){
                log.error("定金退款异常，orderCode不能为空。");
            }else{
                TradeBill tradeBill = transactionService.getTradeBillByOrderNo(orderCode);
                String pingPayId = tradeBill.getPingPayId();
                initPingApiKey();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("description", description); // 必传
                params.put("refund_mode", "to_source");//退款方式 原路退回
                OrderRefund.create(pingPayId, params);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public ResultVo<ValidateReceiptCarPayVo> validateCarPayState(CarPayStateDto paramsDto, boolean addLock) {


        List<String> orderCarNosList = paramsDto.getOrderCarNos();
        List<com.cjyc.common.model.entity.Order> list = orderDao.findListByCarNos(orderCarNosList);
        if (CollectionUtils.isEmpty(list)) {
            return BaseResultUtil.fail("订单信息丢失");
        }
        if (list.size() > 1) {
            return BaseResultUtil.fail("仅支持同一订单内的车辆签收");
        }

        com.cjyc.common.model.entity.Order order = list.get(0);

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
                    if (value != null && !value.equals(paramsDto.getLoginId().toString())) {
                        return BaseResultUtil.fail("订单车辆{0}正在支付中", orderCar.getNo());
                    }
                    if (value != null) {
                        redisUtils.delete(lockKey);
                    }
                    if (!redisLock.lock(lockKey, paramsDto.getLoginId(), 1800000, 100, 300)) {
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

            ValidateReceiptCarPayVo resVo = new ValidateReceiptCarPayVo();
            resVo.setIsNeedPay(isNeedPay);
            resVo.setAmount(amount);
            resVo.setOrderCarNos(orderCarNos);
            resVo.setOrderNo(order.getNo());
            return BaseResultUtil.success(resVo);
        } finally {
            if(addLock && isNeedPay == 0){
                //解锁
                redisLock.releaseLock(lockKeySet);
            }
        }
    }

    @Override
    public ResultVo carCollectPay(CarCollectPayDto reqDto) {
        //验证车辆是否支付
        CarPayStateDto carPayStateDto = new CarPayStateDto();
        carPayStateDto.setLoginId(reqDto.getLoginId());
        carPayStateDto.setOrderCarNos(reqDto.getOrderCarNos());
        ResultVo<ValidateReceiptCarPayVo> resultVo = validateCarPayState(carPayStateDto, true);
        if(ResultEnum.SUCCESS.getCode() != resultVo.getCode()){
            return BaseResultUtil.fail(resultVo.getMsg());
        }
        BigDecimal amount = resultVo.getData().getAmount();
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            return BaseResultUtil.fail("无需支付");
        }

        //组装支付数据
        PingOrderModel pm = new PingOrderModel();
        Order order = null;

        pm.setApp(PingProperty.customerAppId);
        pm.setMerchantOrderNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));
        pm.setObject("order");
        pm.setClientIp(reqDto.getIp());
        pm.setUid(String.valueOf(reqDto.getLoginId()));
        pm.setAmount(amount.intValue());
        pm.setSubject("客户签收支付");
        pm.setBody("客户批量签收");
        pm.setTimeExpire(LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.plus(LocalDateTime.now(), 30, ChronoUnit.MINUTES)));

        MetaDataEntiy mde = new MetaDataEntiy();
        mde.setChannel(reqDto.getChannel());
        mde.setClientId(String.valueOf(ClientEnum.APP_CUSTOMER.code));
        mde.setChargeType(String.valueOf(ChargeTypeEnum.COLLECT_PAY.getCode()));
        String join = Joiner.on(",").join(reqDto.getOrderCarNos());
        mde.setSourceNos(join);
        mde.setLoginId(reqDto.getLoginId().toString());
        mde.setLoginName(reqDto.getLoginName());
        mde.setSourceMainNo(resultVo.getData().getOrderNo());
        pm.setMetaDataEntiy(mde);
        // 备注：订单号
        pm.setDescription("韵车订单车辆号：" + join);

        try {
            order = csPingxxService.payOrderByModel(pm);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if(order == null){
            BaseResultUtil.fail("操作失败");
        }
        transactionService.save(order);
        return BaseResultUtil.success(JSON.parseObject(order.toString()));
    }
    @Override
    public ResultVo<ResultReasonVo> receiptBatch(ReceiptBatchDto reqDto) {
        return null;
    }
}

