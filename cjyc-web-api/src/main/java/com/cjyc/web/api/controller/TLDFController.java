package com.cjyc.web.api.controller;

import com.aipg.acquery.AcNode;
import com.aipg.common.AipgRsp;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.config.TLDFProperty;
import com.cjyc.web.api.service.ITLDFService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hut
 * @Date: 2020/02/25 11:48
 */
@RestController
@Api(tags = "通联代付")
@RequestMapping(value = "/allinpay",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class TLDFController {

    @Autowired
    private ITLDFService tldfService;

    @ApiOperation(value = "获取通联代付账户余额")
    @PostMapping(value = "/balance")
    public ResultVo queryAccountMsg(){
        Map<String, Object> accountMsg = tldfService.queryAccountMsg();
        AipgRsp aipgrsp = (AipgRsp) accountMsg.get("aipgrsp");
        AcNode acNode = (AcNode) accountMsg.get("acNode");
        String ret_code = aipgrsp.getINFO().getRET_CODE();
        String balance = "0";
        if(!"0000".equals(ret_code)){
            log.error("响应码" + aipgrsp.getINFO().getRET_CODE() + "原因：" + aipgrsp.getINFO().getERR_MSG());
            balance = "查询余额异常！";
        }else{
            if(acNode != null){
                balance = acNode.getBALANCE();
                if(balance == null){
                    balance = "0";
                }
                BigDecimal b = new BigDecimal(balance);
                balance = b.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
        }
        Map<String,String> result = new HashMap<>();
        result.put("balance",balance);
        return BaseResultUtil.success(result);
    }

}
