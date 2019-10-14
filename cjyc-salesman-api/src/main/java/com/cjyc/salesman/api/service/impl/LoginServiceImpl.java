package com.cjyc.salesman.api.service.impl;

import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.dto.salesman.login.LoginByPasswordDto;
import com.cjyc.common.model.dto.sys.SysRoleDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Saleman;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.SalemanLoginVo;
import com.cjyc.salesman.api.fegin.ISysRoleService;
import com.cjyc.salesman.api.service.IAdminService;
import com.cjyc.salesman.api.service.ILoginService;
import com.cjyc.salesman.api.service.ISalemanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Resource
    private ISalemanService salemanService;

    @Resource
    private IAdminService adminService;

    @Resource
    private ISysRoleService sysRoleService;

    @Autowired
    private StringRedisUtil redisUtil;

    @Override
    public ResultVo<SalemanLoginVo> loginByCaptcha(CaptchaValidatedDto paramsDto) {
        //返回内容
        SalemanLoginVo salemanLoginVo = new SalemanLoginVo();
        //参数
        String phone = paramsDto.getPhone();
        String captcha = paramsDto.getPhone();

        //查询本地用户信息
        Saleman saleman = salemanService.getByphone(phone);
        if(saleman == null || saleman.getUserId() == null){
            return BaseResultUtil.fail("用户不存在");
        }
        BeanUtils.copyProperties(saleman, salemanLoginVo);

        //验证是否是管理员
        Admin admin = adminService.getByUserId(saleman.getUserId());
        if(admin != null){
            //查询用户角色信息
            List<SysRoleDto> roleList = sysRoleService.getListByUserId(saleman.getUserId());
            salemanLoginVo.setRoleList(roleList);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<SalemanLoginVo> loginBypassword(LoginByPasswordDto paramsDto) {
        //TODO 用户名密码登录
        return null;
    }
}