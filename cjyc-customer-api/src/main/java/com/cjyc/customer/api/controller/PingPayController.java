package com.cjyc.customer.api.controller;

import com.Pingxx.model.Order;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.model.dto.customer.order.CarCollectPayDto;
import com.cjyc.common.model.dto.customer.order.CarPayStateDto;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.cjyc.common.system.config.PingProperty;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsPingxxService;
import com.cjyc.customer.api.dto.OrderModel;
import com.cjyc.customer.api.service.IPingPayService;
import com.cjyc.customer.api.service.ITransactionService;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.EventData;
import com.pingplusplus.model.Webhooks;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author:Hut
 * @Date:2019/11/20 9:32
 */
@RestController
@RequestMapping("/pay")
@Slf4j
@Api(tags = "Ping++支付")
public class PingPayController {


    @Autowired
    private IPingPayService pingPayService;

    @Autowired
    private ITransactionService transactionService;

    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsPingxxService csPingxxService;

/*    @ApiOperation("付款")
    @PostMapping("/order/pre/pay")
    public ResultVo pay(HttpServletRequest request,@RequestBody PrePayDto reqDto){
        reqDto.setIp(IPUtil.getIpAddr(request));
        Order pingOrder;
        try{
            pingOrder = pingPayService.prePay(reqDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail("预付款异常");
        }
        return BaseResultUtil.success(JSONObject.parseObject(pingOrder.toString()));
    }*/

    @ApiOperation("付款")
    @PostMapping("/pay")
    public ResultVo prepay(HttpServletRequest request,@RequestBody PrePayDto reqDto){
        reqDto.setIp(IPUtil.getIpAddr(request));
        Order order;
        try{
            log.debug("pay---------"+reqDto.toString());
            order = pingPayService.pay(reqDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail(e.getMessage());
        }
        return BaseResultUtil.success(JSONObject.parseObject(order.toString()));
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
            pingPayService.cancelOrderRefund(orderCode);
        } catch (Exception e) {
            log.error("取消订单"+orderCode+"，退款异常",e);
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
                    log.debug("------event.getType()=" + event.getType());
                    if ("order.succeeded".equals(event.getType())) {//ping++订单支付成功
                        Order order = (Order) data.getObject();
                        if (data.getObject() instanceof Order) {
                            log.debug("------------->order.succeeded");
                            transactionService.updateTransactions((Order)data.getObject(),event,"1");
                        }
                    }else if("charge.succeeded".equals(event.getType())){
                        Charge charge = (Charge)data.getObject();
                        if(data.getObject() instanceof Charge){
                            log.debug("------------->charge.succeeded");
                            //将金额存进OrderDeTailId
                            //updateAmount(charge);
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
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

    @ApiOperation("司机出示二维码")
    @PostMapping("/sweepDriveCode")
    public ResultVo sweepDriveCode(HttpServletRequest request,@RequestBody SweepCodeDto sweepCodeDto){
        sweepCodeDto.setIp(IPUtil.getIpAddr(request));
        Charge charge;
        try {
            charge = pingPayService.sweepDriveCode(sweepCodeDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail(500,"司机二维码异常");
        }
        return BaseResultUtil.success(JSONObject.parseObject(charge.toString()));
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
            om.setOrderCarIds(om.getOrderCarIds());
            om.setChannel(om.getChannel());
            om.setOrderNo(om.getOrderNo());
            om.setSubject("订单的收款码功能!");
            om.setBody("生成二维码！");
            om.setChargeType("1");//支付尾款
            om.setClientType("user");
            om.setDescription("韵车订单号："+om.getOrderNo());
            charge = pingPayService.sweepSalesmanCode(om);

        } catch (Exception e) {
            log.error("扫码支付异常",e);
            return BaseResultUtil.fail(500,"业务员出示二维码，用户扫码支付异常");
        }
        return BaseResultUtil.success(JSONObject.parseObject(charge.toString()));
    }

    /**
     * 签收-验证支付状态
     * @author JPG
     */
    @ApiOperation(value = "签收-验证支付状态")
    @PostMapping(value = "/receipt/car/pay/state/validate")
    public ResultVo<ValidateReceiptCarPayVo> validateCarPayState(@RequestBody CarPayStateDto reqDto) {
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        return pingPayService.validateCarPayState(reqDto, false);
    }
    /**
     * 签收-按车辆申请支付
     * @author JPG
     */
    @ApiOperation(value = "签收-按车辆申请支付")
    @PostMapping(value = "/receipt/car/apply/pay")
    public ResultVo carCollectPay(HttpServletRequest request, @RequestBody CarCollectPayDto reqDto) {
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        reqDto.setIp(IPUtil.getIpAddr(request));
        return pingPayService.carCollectPay(reqDto);
    }

    /**
     * 签收-无需支付
     * @author JPG
     */
    @ApiOperation(value = "签收-无需支付")
    @PostMapping(value = "/receipt/car")
    public ResultVo<ResultReasonVo> receiptBatch(@RequestBody ReceiptBatchDto reqDto) {
        Customer customer = csCustomerService.validate(reqDto.getLoginId());
        reqDto.setLoginName(customer.getName());
        reqDto.setLoginPhone(customer.getContactPhone());
        reqDto.setLoginType(UserTypeEnum.CUSTOMER);
        return pingPayService.receiptBatch(reqDto);
    }
}
