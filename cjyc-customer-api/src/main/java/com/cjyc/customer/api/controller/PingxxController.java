package com.cjyc.customer.api.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ping++
 * @author JPG
 */
@Api(tags = "测试")
@Slf4j
@RestController
@RequestMapping(value = "/pay",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PingxxController {

/*    public void paytest(@RequestBody PayTestDto dto) throws FileNotFoundException {
        Pingpp.apiKey = PingProperty.apiKey;
        //Pingpp.overrideApiBase("https://sapi.pingxx.com");//此接口为ping++协助测试的接口 升级完成后注释掉 20181023 add
        System.setProperty("https.protocols", "TLSv1.2");//20181023 添加 (TLSv1.2升级配置)
        Pingpp.privateKeyPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"your_rsa_private_key_pkcs.pem").getPath();

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
        meta.put("type", om.getClientId()); //customer 用户  bond 司机保证金  freight 司机运费收入  driver 居间服务费
        meta.put("deposit", om.getDeposit()); //定金金额
        meta.put("orderMan", om.getOrderMan()); //当前app登陆人的ID
        params.put("metadata",meta);//自定义参数
        Order order = Order.create(params); // 创建 Order 对象 方法


        Pingpp.appId = pingAppId;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channel", channel);
        params.put("charge_amount", charge_amount);
        params.put("extra", channelExtra(channel));
        Order order = Order.pay(pingOrderId, params); // 创建支付 Order 对象 方法
    }*/




}
