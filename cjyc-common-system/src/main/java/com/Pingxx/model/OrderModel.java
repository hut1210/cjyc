package com.Pingxx.model;

import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.net.APIResource;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hut
 * @Date: 2019/12/9 19:39
 */
@Data
public class OrderModel extends APIResource {
    private String id;
    private String object;
    private Long created;
    private Boolean livemode;
    private String status;
    private Boolean paid;
    private Boolean refunded;
    private Object app;
    private String charge;
    private String uid;
    private String merchantOrderNo;
    private BigDecimal amount;
    private Integer couponAmount;
    private Integer actualAmount;
    private Integer amountRefunded;
    private Integer amountPaid;
    private String currency;
    private String subject;
    private String body;
    private String clientIp;
    private Long timePaid;
    private Long timeExpire;
    private String coupon;
    private ChargeCollection charges;
    private String description;
    private Map<String, Object> metadata;
    private ChargeEssentials chargeEssentials;
    private Long availableBalance;
    private String receiptApp;
    private String serviceApp;
    private List<String> availableMethods;


    /**支付渠道*/
    private String channel;
    private String orderNo; //平台订单编号
    private String chargeType;	//支付类型 类型：1物流费预付，2物流费全款到付，3物流费分车支付， 11运费支付、12居间服务费支付
    private String batch;	//0:整体支付尾款	 1：批量支付尾款
    private BigDecimal deductFee;	//扣费金额
    private String clientType;	//客户端类型 customer:用户端	driver:司机端    user:业务员端
    private String pingAppId;
    private BigDecimal deposit;	//定金
    private String orderMan; //当前app登陆人的Id
    private List<String> orderCarIds;
    private String driver_code;
    private String order_type;
    private String driver_name;
    private String driver_phone;
    private String back_type;
    private String taskId;
    private List<String>  taskCarIdList;

    public List<String> getTaskCarIdList() {
        return taskCarIdList;
    }

    public void setTaskCarIdList(List<String> taskCarIdList) {
        this.taskCarIdList = taskCarIdList;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getBack_type() {
        return back_type;
    }

    public void setBack_type(String back_type) {
        this.back_type = back_type;
    }

    public List<String> getOrderCarIds() {
        return orderCarIds;
    }

    public void setOrderCarIds(List<String> orderCarIds) {
        this.orderCarIds = orderCarIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getRefunded() {
        return refunded;
    }

    public void setRefunded(Boolean refunded) {
        this.refunded = refunded;
    }

    public Object getApp() {
        return app;
    }

    public void setApp(Object app) {
        this.app = app;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Integer getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Integer actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(Integer amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Long getTimePaid() {
        return timePaid;
    }

    public void setTimePaid(Long timePaid) {
        this.timePaid = timePaid;
    }

    public Long getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Long timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public ChargeCollection getCharges() {
        return charges;
    }

    public void setCharges(ChargeCollection charges) {
        this.charges = charges;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public ChargeEssentials getChargeEssentials() {
        return chargeEssentials;
    }

    public void setChargeEssentials(ChargeEssentials chargeEssentials) {
        this.chargeEssentials = chargeEssentials;
    }

    public Long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getReceiptApp() {
        return receiptApp;
    }

    public void setReceiptApp(String receiptApp) {
        this.receiptApp = receiptApp;
    }

    public String getServiceApp() {
        return serviceApp;
    }

    public void setServiceApp(String serviceApp) {
        this.serviceApp = serviceApp;
    }

    public List<String> getAvailableMethods() {
        return availableMethods;
    }

    public void setAvailableMethods(List<String> availableMethods) {
        this.availableMethods = availableMethods;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    /**
     * 创建 order
     *
     * @param params
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static OrderModel create(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        return request(RequestMethod.POST, classURL(OrderModel.class), params, OrderModel.class);
    }

    /**
     * 查询 order
     *
     * @param id
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static OrderModel retrieve(String id) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, instanceURL(OrderModel.class, id), null, OrderModel.class);
    }

    /**
     * 查询 order 列表
     *
     * @param params
     * @return OrderCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static OrderCollection list(Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, classURL(OrderModel.class), params, OrderCollection.class);
    }

    /**
     * 更新 order
     *
     * @param params
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static OrderModel update(String id, Map<String, Object> params)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        return request(RequestMethod.PUT, instanceURL(OrderModel.class, id), params, OrderModel.class);
    }

    /**
     * 取消 order
     *
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public OrderModel cancel()
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", "canceled");
        return update(this.getId(), params);
    }

    /**
     * 取消 order
     *
     * @param id
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static OrderModel cancel(String id)
            throws AuthenticationException, InvalidRequestException,
            APIConnectionException, APIException, ChannelException, RateLimitException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", "canceled");
        return update(id, params);
    }

    /**
     * 支付 order
     *
     * @param params
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public OrderModel pay(Map<String, Object> params) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.POST, String.format("%s/pay", instanceURL(OrderModel.class, id)),
                params, OrderModel.class);
    }

    /**
     * 支付 order
     *
     * @param id
     * @param params
     * @return Order
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static OrderModel pay(String id, Map<String, Object> params) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.POST, String.format("%s/pay", instanceURL(OrderModel.class, id)),
                params, OrderModel.class);
    }

    /**
     * 查询订单 Charge 列表
     *
     * @param id
     * @param params
     * @return ChargeCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static ChargeCollection chargeList(String id, Map<String, Object> params) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, String.format("%s/charges", instanceURL(OrderModel.class, id)),
                params, ChargeCollection.class);
    }

    /**
     * 查询订单 Charge
     *
     * @param orderId
     * @param chargeId
     * @return Charge
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    public static Charge retrieveCharge(String orderId, String chargeId) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(RequestMethod.GET, String.format("%s/charges/%s", instanceURL(OrderModel.class, orderId), chargeId),
                null, Charge.class);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getPingAppId() {
        return pingAppId;
    }

    public void setPingAppId(String pingAppId) {
        this.pingAppId = pingAppId;
    }

    public String getOrderMan() {
        return orderMan;
    }

    public void setOrderMan(String orderMan) {
        this.orderMan = orderMan;
    }

    public String getDriver_code() {
        return driver_code;
    }

    public void setDriver_code(String driver_code) {
        this.driver_code = driver_code;
    }

    public BigDecimal getDeductFee() {
        return deductFee;
    }

    public void setDeductFee(BigDecimal deductFee) {
        this.deductFee = deductFee;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }
}

