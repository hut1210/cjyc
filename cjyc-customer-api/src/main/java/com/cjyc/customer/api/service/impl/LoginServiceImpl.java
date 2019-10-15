package com.cjyc.customer.api.service.impl;

import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.dto.salesman.login.LoginByUserNameDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.customer.api.feign.ISysRoleService;
import com.cjyc.customer.api.service.IAdminService;
import com.cjyc.customer.api.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Resource
    private ICustomerDao customerDao;
    @Autowired
    private StringRedisUtil redisUtil;

    @Override
    public ResultVo<Customer> loginByCaptcha(LoginByPhoneDto paramsDto) {
        //返回内容
        CustomerLoginVo customerLoginVo = new CustomerLoginVo();
        //参数
        String phone = paramsDto.getPhone();
        String captcha = paramsDto.getCaptcha();
        Integer type = paramsDto.getType();
        //校验验证码
        String key = RedisKeys.getSalesmanCaptchaKeyByPhone(phone, type);
        String captchaCached = redisUtil.getStrValue(key);
        if(captchaCached == null){
            return BaseResultUtil.fail("请重新获取验证码");
        }
        if(!captchaCached.equals(captcha)){
            return BaseResultUtil.fail("验证码错误");
        }

        //查询本地用户信息
        Customer customer = customerDao.findByPhone(phone);
        if(customer == null || customer.getUserId() == null){
            return BaseResultUtil.fail("用户不存在");
        }
        BeanUtils.copyProperties(customer, customerLoginVo);

        //获取网关令牌


        //验证是否是管理员
       /* Admin admin = adminService.getByUserId(customer.getUserId());
        if(admin != null){
            //查询用户角色信息
            List<SysRoleDto> roleList = sysRoleService.getListByUserId(saleman.getUserId());
            salemanLoginVo.setRoleList(roleList);
        }*/
        return BaseResultUtil.success(customer);
    }

    @Override
    public ResultVo<Customer> loginBypassword(LoginByUserNameDto paramsDto) {
        //TODO 用户名密码登录
        return null;
    }
}
