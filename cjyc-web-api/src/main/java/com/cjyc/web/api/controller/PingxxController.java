package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.model.dto.customer.pingxx.PrePayDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Ping++
 * @author JPG
 */
@RestController
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
    @ApiOperation(value = "获取二维码")
    @PostMapping("/qrcode/get")
    public ResultVo getPayQrCode(HttpServletRequest request, @RequestBody PrePayDto prePayDto){
        prePayDto.setIp(IPUtil.getIpAddr(request));
        Charge charge = new Charge();
        Map<String,String> map=new HashMap<>();
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

            map.put("imageUrl",QRcodeUtil.creatRrCode(qrcode,200,200));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return BaseResultUtil.success(map);
    }



}
