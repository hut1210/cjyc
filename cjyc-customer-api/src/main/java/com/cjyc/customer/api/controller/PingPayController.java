package com.cjyc.customer.api.controller;

import com.alibaba.nacos.client.utils.StringUtils;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.config.PingProperty;
import com.cjyc.customer.api.dto.OrderModel;
import com.cjyc.customer.api.service.IPingPayService;
import com.cjyc.customer.api.service.ITransactionService;
import com.pingplusplus.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/20 9:32
 */
@RestController
@RequestMapping("/pingpay")
@Api(tags = "Ping++支付")
public class PingPayController {

    private static Logger logger = LoggerFactory.getLogger(PingPayController.class);

    @Autowired
    private IPingPayService iPingPayService;

    @Autowired
    private ITransactionService iTransactionService;

    @ApiOperation("付款")
    @PostMapping("/pay")
    public ResultVo pay(HttpServletRequest request,@RequestBody OrderModel om){
        Order order = new Order();
        try{
            logger.debug("pay---------"+om.toString());
            order = iPingPayService.pay(request,om);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return BaseResultUtil.fail("预付款异常");
        }
        return BaseResultUtil.success((Object) order.toString());
    }

    /**
     * 取消订单 退款
     * @throws Exception
     */
    @ApiOperation("退款")
    @PostMapping("/cancel")
    public ResultVo cancelOrderRefund(HttpServletRequest request){
        String orderCode = request.getParameter("orderCode");
        String jsonpCallback = request.getParameter("jsonpCallback");
        //退款
        try {
            iTransactionService.cancelOrderRefund(orderCode);
        } catch (Exception e) {
            logger.error("取消订单"+orderCode+"，退款异常",e);
            return BaseResultUtil.fail("取消订单退款异常");
        }

        return BaseResultUtil.success();
    }

    @ApiOperation("回调")
    @PostMapping("/webhooksNotice")
    public ResultVo webhooksNotice(HttpServletRequest request){
        try{
            request.setCharacterEncoding("UTF8");
            // 获得 http body 内容
            BufferedReader reader = request.getReader();
            StringBuffer buffer = new StringBuffer();
            String str = "";
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
            reader.close();
            String signature = request.getHeader("X-Pingplusplus-Signature");
            boolean verifyData = verifyData(buffer.toString(), signature, getPubKey());
            if(verifyData) {
                // 解析异步通知数据
                if (StringUtils.isNotBlank(buffer.toString())) {
                    Event event = Webhooks.eventParse(buffer.toString());
                    EventData data = event.getData();
                    logger.debug("------event.getType()=" + event.getType());
                    if ("order.succeeded".equals(event.getType())) {//ping++订单支付成功
                        Order order = (Order) data.getObject();
                        if (data.getObject() instanceof Order) {
                            logger.debug("------------->order.succeeded");
                            //pingWebhooksService.handleOrderData((Order) data.getObject(), event, "1");
                        }
                    }else if("charge.succeeded".equals(event.getType())){
                        Charge charge = (Charge)data.getObject();
                        if(data.getObject() instanceof Charge){
                            logger.debug("------------->charge.succeeded");
                            //将金额存进OrderDeTailId
                            //updateAmount(charge);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return BaseResultUtil.fail(500,"ping++订单支付回调异常");
        }
        return BaseResultUtil.success();
    }

    /**
     * 获得公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPubKey() throws Exception {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"pingpp_public_key.pem");
        String pubKeyPath = file.getPath();
        String pubKeyString = getStringFromFile(pubKeyPath);
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    public static void main(String arg[]) throws Exception{
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"pingpp_public_key.pem");
        String pubKeyPath = PingPayController.class.getClassLoader().getResource("pingpp_public_key.pem").getPath();
        System.out.println(file.getPath());
        System.out.println(pubKeyPath);

    }

    /**
     * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(inReader);
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = bf.readLine();
            if (line != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } while (line != null);

        return sb.toString();
    }

    public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }

    @ApiOperation("司机扫码")
    @PostMapping("/sweepDriveCode")
    public ResultVo sweepDriveCode(HttpServletRequest request,@RequestBody OrderModel om){

        om.setClientIp(request.getRemoteAddr());
        om.setPingAppId(PingProperty.userAppId);
        //创建Charge对象
        Charge charge = new Charge();
        try {
            om.setAmount(om.getAmount());
            om.setDriver_code(om.getDriver_code());
            om.setOrder_type(om.getOrder_type());
            om.setDriver_name(om.getDriver_name());
            om.setDriver_phone(om.getDriver_phone());
            om.setBack_type(om.getBack_type());
            om.setOrderCarId(om.getOrderCarId());
            om.setChannel(om.getChannel());
            om.setOrderNo(om.getOrderNo());
            om.setSubject("订单的收款码功能!");
            om.setBody("生成二维码！");
            om.setChargeType("1");
            om.setClientType("driver");
            om.setDescription("韵车订单号："+om.getOrderNo());
            charge = iPingPayService.sweepDriveCode(om);

        } catch (Exception e) {
            logger.error("扫码支付异常",e);
            return BaseResultUtil.fail(500,"司机扫码支付异常");
        }
        return BaseResultUtil.success((Object)charge.toString());
    }

    @ApiOperation("业务员出示二维码，用户扫码")
    @PostMapping("/sweepSalesmanCode")
    public ResultVo sweepSalesmanCode(HttpServletRequest request,@RequestBody OrderModel om){

        om.setClientIp(request.getRemoteAddr());
        om.setPingAppId(PingProperty.userAppId);
        //创建Charge对象
        Charge charge = new Charge();
        try {
            om.setAmount(om.getAmount());
            om.setDriver_code(om.getDriver_code());
            om.setOrder_type(om.getOrder_type());
            om.setDriver_name(om.getDriver_name());
            om.setDriver_phone(om.getDriver_phone());
            om.setBack_type(om.getBack_type());
            om.setOrderCarId(om.getOrderCarId());
            om.setChannel(om.getChannel());
            om.setOrderNo(om.getOrderNo());
            om.setSubject("订单的收款码功能!");
            om.setBody("生成二维码！");
            om.setChargeType("1");//支付尾款
            om.setClientType("user");
            om.setDescription("韵车订单号："+om.getOrderNo());
            charge = iPingPayService.sweepSalesmanCode(om);

        } catch (Exception e) {
            logger.error("扫码支付异常",e);
            return BaseResultUtil.fail(500,"业务员出示二维码，用户扫码支付异常");
        }
        return BaseResultUtil.success((Object)charge.toString());
    }
}
