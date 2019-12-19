package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.web.api.service.IPingxxService;
import com.cjyc.web.api.util.QRcodeUtil;
import com.pingplusplus.model.Charge;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Ping++
 * @author JPG
 */
@Controller
@Api(tags = "资金-Ping++")
@RequestMapping(value = "/pingxx",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class PingxxController {

    @Resource
    private IPingxxService pingxxService;

    @Autowired
    private ICsPingPayService csPingPayService;
    /**
     * 获取支付二维码
     * @author JPG
     * @since 2019/11/6 19:51
     * @param
     */
    @ApiOperation(value = "获取支付宝二维码")
    @GetMapping("/qrcode/get")
    public void getPayQrCode(HttpServletRequest request, PrePayDto prePayDto, HttpServletResponse response){
        prePayDto.setIp(IPUtil.getIpAddr(request));
        Charge charge = new Charge();
        try {
            charge = csPingPayService.prePay(prePayDto);

            JSONObject jsonObject = JSONObject.parseObject(charge.toString());
            // 获取到key为shoppingCartItemList的值
            String credential = jsonObject.getString("credential");

            JSONObject credentialJson = JSONObject.parseObject(credential.toString());
            String qrcode = "";
            if(prePayDto.getChannel().equals("wx_pub_qr")){
                qrcode = credentialJson.getString("wx_pub_qr");
            }else if(prePayDto.getChannel().equals("alipay_qr")){
                qrcode = credentialJson.getString("alipay_qr");
            }

            QRcodeUtil.creatRrCode(qrcode,200,200,response);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }



}
