package com.cjyc.common.system.service.impl;

import com.Pingxx.model.OrderModel;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.system.config.PingProperty;
import com.cjyc.common.system.service.IPingPayService;
import com.cjyc.common.system.service.ITransactionService;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hut
 * @Date: 2019/12/9 19:29
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class PingPayServiceImpl implements IPingPayService {

    @Autowired
    private ITransactionService transactionService;

    @Override
    public Charge sweepDriveCode(SweepCodeDto sweepCodeDto) throws RateLimitException, APIException, ChannelException, InvalidRequestException,
            APIConnectionException, AuthenticationException, FileNotFoundException {
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

    private void initPingApiKey() throws FileNotFoundException {
        Pingpp.apiKey = PingProperty.apiKey;
        //Pingpp.overrideApiBase("https://sapi.pingxx.com");//此接口为ping++协助测试的接口 升级完成后注释掉 20181023 add
        System.setProperty("https.protocols", "TLSv1.2");//20181023 添加 (TLSv1.2升级配置)
        Pingpp.privateKeyPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"your_rsa_private_key_pkcs.pem").getPath();
    }
}
