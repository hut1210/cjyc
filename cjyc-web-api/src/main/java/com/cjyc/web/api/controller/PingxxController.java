package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.model.dto.customer.pingxx.ValidateSweepCodeDto;
import com.cjyc.common.model.dto.web.pingxx.WebOutOfStockDto;
import com.cjyc.common.model.dto.web.pingxx.WebPrePayDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateSweepCodePayVo;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.web.api.service.IPingxxService;
import com.cjyc.web.api.util.QRcodeUtil;
import com.pingplusplus.model.Charge;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public ResultVo getPayQrCode(HttpServletRequest request, @RequestBody WebPrePayDto prePayDto){
        prePayDto.setIp(StringUtil.getIp(IPUtil.getIpAddr(request)));
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

    @ApiOperation(value = "获取出库二维码")
    @PostMapping("/qrcode/getOutOfStock")
    public ResultVo getOutOfStockQrCode(HttpServletRequest request, @RequestBody WebOutOfStockDto webOutOfStockDto){
        webOutOfStockDto.setIp(StringUtil.getIp(IPUtil.getIpAddr(request)));
        Charge charge = new Charge();
        Map<String,String> map=new HashMap<>();
        try {
            charge = csPingPayService.getOutOfStockQrCode(webOutOfStockDto);

            JSONObject jsonObject = JSONObject.parseObject(charge.toString());
            // 获取到key为shoppingCartItemList的值
            String credential = jsonObject.getString("credential");

            JSONObject credentialJson = JSONObject.parseObject(credential.toString());
            String qrcode = "";
            if(webOutOfStockDto.getChannel().equals("wx_pub_qr")){
                qrcode = credentialJson.getString("wx_pub_qr");
            }else if(webOutOfStockDto.getChannel().equals("alipay_qr")){
                qrcode = credentialJson.getString("alipay_qr");
            }

            map.put("imageUrl",QRcodeUtil.creatRrCode(qrcode,200,200));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "验证支付状态")
    @PostMapping(value = "/validate")
    public ResultVo<ValidateSweepCodePayVo> validateCarPayState(@RequestBody ValidateSweepCodeDto validateSweepCodeDto) {

        return csPingPayService.validateCarPayState(validateSweepCodeDto, false);
    }

    @ApiOperation(value = "解锁预付单")
    @PostMapping(value = "/prepay/unlock/{orderNo}")
    public ResultVo unlock(@PathVariable String orderNo) {

        return csPingPayService.unlock(orderNo);
    }

    @ApiOperation(value = "解锁扫码付款")
    @PostMapping(value = "/qrcode/unlock/{orderCarNo}")
    public ResultVo unlockQrcode(@PathVariable String orderCarNo) {

        return csPingPayService.unlockQrcode(orderCarNo);
    }
}
