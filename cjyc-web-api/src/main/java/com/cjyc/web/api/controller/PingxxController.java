package com.cjyc.web.api.controller;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IPingxxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Ping++
 * @author JPG
 */
@RestController
@Api(tags = "Ping++")
@RequestMapping(value = "/pingxx",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PingxxController {

    @Resource
    private IPingxxService pingxxService;


    /**
     * 获取支付二维码
     * @author JPG
     * @since 2019/11/6 19:51
     * @param
     */
    @ApiOperation(value = "获取支付二维码")
    @PostMapping("/qrcode/get")
    public ResultVo getPayQrCode(){
        return null;
    }



}
