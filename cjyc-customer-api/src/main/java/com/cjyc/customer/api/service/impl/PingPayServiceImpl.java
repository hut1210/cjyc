package com.cjyc.customer.api.service.impl;

import com.cjyc.customer.api.config.PingProperty;
import com.cjyc.customer.api.dto.OrderModel;
import com.cjyc.customer.api.service.IPingPayService;
import com.cjyc.customer.api.service.ITransactionService;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:Hut
 * @Date:2019/11/20 14:57
 */
@Service
@Slf4j
public class PingPayServiceImpl implements IPingPayService {

    private static Logger logger = LoggerFactory.getLogger(PingPayServiceImpl.class);

    @Autowired
    private ITransactionService iTransactionService;

    @Override
    public Order pay(HttpServletRequest request, OrderModel om) {

        String clientIp = request.getRemoteAddr();
        String pingAppId =PingProperty.userAppId;
        om.setClientIp(clientIp);
        om.setPingAppId(pingAppId);

        Order order = new Order();
        om.setSubject("预付款");
        om.setBody("订单预付款");
        om.setChargeType("1");
        om.setClientType("customer");
        String channel = om.getChannel();
        // 备注：订单号
        om.setDescription("韵车订单号："+om.getOrderNo());
        try {
            if(!"balance".equals(channel)){
                order = payOrder(om);
                logger.debug(om.toString());
                iTransactionService.saveTransactions(order, "0");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return order;
    }

    @Override
    public Order payOrder(OrderModel om) throws InvalidRequestException, APIException,
            ChannelException, RateLimitException, APIConnectionException, AuthenticationException,FileNotFoundException {
        Order order = createOrder(om);
        if(order != null){
            order = payOrder(om.getPingAppId(),om.getChannel(), om.getAmount(), order.getId());
            return order;
        }
        return order;
    }

    @Override
    public Charge sweepDriveCode(OrderModel om) throws RateLimitException, APIException, ChannelException,InvalidRequestException,
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
        meta.put("code", om.getOrderNo());	//订单号
        meta.put("orderDetailId",om.getOrderCarId());//订单Id
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
        meta.put("code", om.getOrderNo());	//订单号
        meta.put("orderDetailId",om.getOrderCarId());//订单Id
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
        meta.put("code", om.getOrderNo());	//订单号
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
    public Order payOrder(String pingAppId,String channel,int charge_amount,String pingOrderId) throws RateLimitException,
            APIException, ChannelException, InvalidRequestException,FileNotFoundException,
            APIConnectionException, AuthenticationException {
        initPingApiKey();
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
}
