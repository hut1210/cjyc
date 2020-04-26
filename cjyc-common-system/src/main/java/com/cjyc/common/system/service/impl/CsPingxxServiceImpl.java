package com.cjyc.common.system.service.impl;

import com.cjyc.common.system.config.PingProperty;
import com.pingplusplus.model.Order;
import com.Pingxx.model.PingOrderModel;
import com.cjyc.common.model.util.BeanMapUtil;
import com.cjyc.common.system.service.ICsPingxxService;
import com.google.common.collect.Maps;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CsPingxxServiceImpl implements ICsPingxxService {


    @Override
    public Order createOrderByModel(PingOrderModel pm) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException {
        initPingApiKey();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("uid", pm.getUid()); // 用户在当前 app 下的 User ID, 可选
        params.put("app", pm.getApp()); // App ID, 必传
        params.put("merchant_order_no", pm.getMerchantOrderNo()); // 商户订单号, 必传
        params.put("subject", pm.getSubject()); // 商品的标题, 必传
        params.put("body", pm.getBody()); // 商品的描述信息, 必传
        params.put("amount", pm.getAmount()); // 订单总金额，单位：分, 必传
        params.put("currency", "cny"); // 仅支持人民币 cny, 必传
        params.put("client_ip", pm.getClientIp()); // 客户端的 IP 地址 (IPv4 格式，要求商户上传真实的，渠道可能会判断), 必传
        params.put("description", pm.getDescription()); // 备注：订单号

        Map<String, Object> meta = BeanMapUtil.beanToMap(pm.getMetaDataEntiy());
        params.put("metadata", meta);//自定义参数
        log.info("createOrderByModel={}",params.toString());
        return Order.create(params);

    }

    @Override
    public Order payOrderByModel(PingOrderModel pm) throws FileNotFoundException, InvalidRequestException, APIException, ChannelException, RateLimitException, APIConnectionException, AuthenticationException {
        Order pingOrder = createOrderByModel(pm);
        if (pingOrder != null) {
            pingOrder = payPingOrder(pm.getApp(), pm.getMetaDataEntiy().getChannel(), pm.getAmount(), pingOrder.getId());
        }
        log.info("payOrderByModel pingOrder={}", pingOrder == null ? "null" : pingOrder.toString());
        return pingOrder;
    }

    private Order payPingOrder(Object app, String channel, Integer amount, String pingOrderId) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, FileNotFoundException {
        initPingApiKey();
        Pingpp.appId = app.toString();
        Map<String, Object> params = Maps.newHashMap();
        params.put("channel", channel);
        params.put("charge_amount", amount);
        //params.put("extra", Maps.newHashMap());
        return Order.pay(pingOrderId, params);
    }

    private void initPingApiKey() throws FileNotFoundException {
        Pingpp.apiKey = PingProperty.apiKey;
        //Pingpp.overrideApiBase("https://sapi.pingxx.com");//此接口为ping++协助测试的接口 升级完成后注释掉 20181023 add
        System.setProperty("https.protocols", "TLSv1.2");//20181023 添加 (TLSv1.2升级配置)
        Pingpp.privateKeyPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "your_rsa_private_key_pkcs.pem").getPath();
    }
}
