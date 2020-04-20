package com.cjyc.customer.api.service.impl;

import com.Pingxx.model.MetaDataEntiy;
import com.Pingxx.model.OrderRefund;
import com.Pingxx.model.PingOrderModel;
import com.Pingxx.model.PingxxMetaData;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.IOrderRefundDao;
import com.cjyc.common.model.dto.customer.order.CarCollectPayDto;
import com.cjyc.common.model.dto.customer.order.CarPayStateDto;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Refund;
import com.cjyc.common.model.entity.TradeBill;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.exception.CommonException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.BeanMapUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.cjyc.common.system.config.PingProperty;
import com.cjyc.common.system.service.ICsPingxxService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.common.system.service.ICsTransactionService;
import com.cjyc.common.system.util.RedisLock;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.customer.api.dto.OrderModel;
import com.cjyc.customer.api.service.IPingPayService;
import com.cjyc.customer.api.service.ITransactionService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Order;
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
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private ICsTaskService csTaskService;

    @Resource
    private RedisLock redisLock;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ITransactionService transactionService;

    @Resource
    private IOrderRefundDao orderRefundDao;

    @Autowired
    private ICsTransactionService csTransactionService;

    private final Lock lock = new ReentrantLock();

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
    public com.pingplusplus.model.Order pay(PrePayDto reqDto) throws FileNotFoundException, RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException {
        String orderNo = reqDto.getOrderNo();
        OrderModel om = new OrderModel();
        Order order = new Order();

        TradeBill tradeBill = transactionService.getTradeBillByOrderNo(reqDto.getOrderNo());
        if(tradeBill != null){
            com.cjyc.common.model.entity.Order o = orderDao.findByNo(reqDto.getOrderNo());
            if(o!=null){
                if(o.getWlPayState()==2){
                    throw new CommonException("订单已支付完成","1");
                }
            }
        }else{
            log.info("pay orderNo ="+orderNo);
            String lockKey =RedisKeys.getWlPayLockKey(orderNo);
            if (!redisLock.lock(lockKey, 1800000, 99, 200)) {
                throw new CommonException("订单正在支付中","1");
            }
        }
        //try {
            BigDecimal wlFee = transactionService.getAmountByOrderNo(reqDto.getOrderNo());
            om.setClientIp(reqDto.getIp());
            om.setUid(String.valueOf(reqDto.getUid()));
            om.setAmount(wlFee);
            om.setSubject(ChargeTypeEnum.PREPAY.getName());
            om.setBody("订单预付款");
            // 备注：订单号
            om.setDescription("韵车订单号："+reqDto.getOrderNo());

            PingxxMetaData pingxxMetaData = new PingxxMetaData();
            pingxxMetaData.setChannel(reqDto.getChannel());
            pingxxMetaData.setPingAppId(PingProperty.customerAppId);
            pingxxMetaData.setOrderNo(reqDto.getOrderNo());
            pingxxMetaData.setChargeType(String.valueOf(ChargeTypeEnum.PREPAY.getCode()));
            pingxxMetaData.setClientType(String.valueOf(ClientEnum.APP_CUSTOMER.code));
            pingxxMetaData.setLoginId(reqDto.getUid());
            pingxxMetaData.setLoginType(String.valueOf(UserTypeEnum.CUSTOMER.code));

            om.setPingxxMetaData(pingxxMetaData);
            order = payOrder(om);
            log.debug(order.toString());
            transactionService.saveTransactions(order, "0");
        /*} catch (Exception e) {
            log.error(e.getMessage(),e);
        }*/
        return order;
    }


    private Order payOrder(OrderModel om) throws InvalidRequestException, APIException,
            ChannelException, RateLimitException, APIConnectionException, AuthenticationException,FileNotFoundException {
        Order order = createOrder(om);
        if(order != null){
            order = payOrder(om.getPingxxMetaData().getPingAppId(),om.getPingxxMetaData().getChannel(), om.getAmount(), order.getId());
            return order;
        }
        return order;
    }

    private Order createOrder(OrderModel om) throws RateLimitException, APIException,
            ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException,FileNotFoundException {

        initPingApiKey();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", om.getUid()); // 用户在当前 app 下的 User ID, 可选
        params.put("app", om.getPingxxMetaData().getPingAppId()); // App ID, 必传
        Calendar calendar = Calendar.getInstance();
        params.put("merchant_order_no", Integer.toString(calendar.get(Calendar.YEAR)) + System.currentTimeMillis()); // 商户订单号, 必传
        params.put("subject", om.getSubject()); // 商品的标题, 必传
        params.put("body", om.getBody()); // 商品的描述信息, 必传
        params.put("amount", om.getAmount()); // 订单总金额，单位：分, 必传
        params.put("currency", "cny"); // 仅支持人民币 cny, 必传
        params.put("client_ip", om.getClientIp()); // 客户端的 IP 地址 (IPv4 格式，要求商户上传真实的，渠道可能会判断), 必传
        params.put("description", om.getDescription()); // 备注：订单号

        Map<String, Object> meta = BeanMapUtil.beanToMap(om.getPingxxMetaData());
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
    public ResultVo cancelOrderRefund(String orderCode) {
        lock.lock();
        try{
            //先查询 是否已退款
            Refund refund = orderRefundDao.selectByOrderCode(orderCode);
            if(refund!=null){
                if(refund.getState()==2){
                    log.error("已退款 orderCode={}",orderCode);
                    return BaseResultUtil.fail("已退款");
                }else if(refund.getState()==0){
                    log.error("已申请退款，请勿重复操作 orderCode={}",orderCode);
                    return BaseResultUtil.fail("已申请退款，请勿重复操作");
                }
            }else{
                String description = "订单号：" + orderCode + "，退款";
                if(StringUtils.isBlank(description)){
                    log.error("退款异常，description不能为空。");
                    return BaseResultUtil.fail("退款异常，description不能为空。");
                }else if(StringUtils.isBlank(orderCode)){
                    log.error("退款异常，orderCode不能为空。");
                    return BaseResultUtil.fail("退款异常，orderCode不能为空。");
                }else{
                    TradeBill tradeBill = transactionService.getTradeBillByOrderNo(orderCode);
                    String pingPayId = tradeBill.getPingPayId();
                    initPingApiKey();
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("description", description); // 必传
                    params.put("refund_mode", "to_source");//退款方式 原路退回

                    PingxxMetaData pingxxMetaData = new PingxxMetaData();
                    pingxxMetaData.setOrderNo(orderCode);

                    Map<String, Object> meta = BeanMapUtil.beanToMap(pingxxMetaData);
                    params.put("metadata",meta);//自定义参数
                    OrderRefund.create(pingPayId, params);

                    //添加退款记录
                    try{
                        Refund refund1 = new Refund();
                        refund1.setOrderNo(orderCode);
                        refund1.setState(0);
                        refund1.setCreateTime(System.currentTimeMillis());
                        orderRefundDao.insert(refund1);
                    }catch (Exception e){
                        log.error("添加退款记录异常 orderCode={}",orderCode);
                        log.error(e.getMessage(),e);
                    }

                }
            }

        }catch (Exception e){
            //添加退款记录
            /*try{
                Refund refund1 = new Refund();
                refund1.setOrderNo(orderCode);
                refund1.setState(-1);
                refund1.setCreateTime(System.currentTimeMillis());
                orderRefundDao.insert(refund1);
            }catch (Exception e1){
                log.error("添加退款记录异常1 orderCode={}",orderCode);
                log.error(e1.getMessage(),e1);
            }*/

            log.error("退款异常 orderCode={}",orderCode);
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail("退款异常 orderCode="+orderCode);
        }finally {
            lock.unlock();
        }

        return BaseResultUtil.success();
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
                    String lockKey = RedisKeys.getWlPayLockKey(orderCar.getNo());
                    String value = redisUtils.get(lockKey);
                    if (value != null && !value.equals(paramsDto.getLoginId().toString())) {
                        return BaseResultUtil.fail("订单车辆{0}正在支付中", orderCar.getNo());
                    }
                    if (value != null) {
                        redisUtils.delete(lockKey);
                    }
                    if (!redisLock.lock(lockKey, paramsDto.getLoginId(), 300000, 10, 300)) {
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
            resVo.setPayType(order.getPayType());
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
        pm.setBody("客户按车辆收银台支付");
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
        mde.setLoginType(String.valueOf(UserTypeEnum.CUSTOMER.code));
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
        return csTaskService.receiptBatch(reqDto);
    }
}

