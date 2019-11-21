package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjyc.common.model.dto.customer.login.VerifyCodeDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.service.ICsLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsLoginServiceImpl implements ICsLoginService {

    @Resource
    private ISysLoginService sysLoginService;

    @Override
    public ResultVo verifyCode(VerifyCodeDto dto) {
        ResultData rd = sysLoginService.verifyCode(dto.getPhone());
        if(!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())){
            return BaseResultUtil.fail("超时，请联系管理员");
        }
        return BaseResultUtil.success(rd.getData());
    }
}