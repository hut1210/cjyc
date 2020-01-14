package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.auth.SendSmsCodeReq;
import com.cjyc.common.model.dto.VerifyCodeDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.service.ICsLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsLoginServiceImpl implements ICsLoginService {

    @Resource
    private ISysLoginService sysLoginService;

    @Override
    public ResultVo verifyCode(VerifyCodeDto dto) {
        String signature = YmlProperty.get("cjkj.signature");
        if(StringUtils.isBlank(signature)){
            return BaseResultUtil.fail("签名不能为空，请先添加签名");
        }
        SendSmsCodeReq req = new SendSmsCodeReq();
        req.setPhone(dto.getPhone());
        req.setSignature(signature);
        ResultData rd = sysLoginService.verifyCode(req);
        if(!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())){
            return BaseResultUtil.fail(rd.getMsg());
        }
        return BaseResultUtil.success(rd.getData());
    }
}