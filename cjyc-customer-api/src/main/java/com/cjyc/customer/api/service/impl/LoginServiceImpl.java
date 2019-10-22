package com.cjyc.customer.api.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;
import com.cjyc.customer.api.config.LoginProperty;
import com.cjyc.customer.api.feign.ISysLoginService;
import com.cjyc.customer.api.feign.ISysUserService;
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
    @Resource
    private ISysLoginService sysLoginService;
    @Resource
    private ISysUserService sysUserService;


    @Override
    public ResultVo<CustomerLoginVo> loginByCaptcha(LoginByPhoneDto paramsDto) {
        //返回内容
        CustomerLoginVo customerLoginVo = new CustomerLoginVo();
        //参数
        String phone = paramsDto.getPhone();
        String captcha = paramsDto.getCaptcha();
        //校验验证码
        String key = RedisKeys.getSalesmanCaptchaKeyByPhone(phone, CaptchaTypeEnum.LOGIN.code);
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
            //注册用户
            customer = register(phone);
        }
        //是否是第一次操作APP，若是更新注册时间
        if(customer.getRegisterTime() == null){
            customer.setRegisterTime(System.currentTimeMillis());
            customerDao.updateById(customer);
        }
        BeanUtils.copyProperties(customer, customerLoginVo);

        //获取网关令牌
        AuthLoginReq authLoginReq = new AuthLoginReq();
        authLoginReq.setClientId(LoginProperty.clientId);
        authLoginReq.setClientSecret(LoginProperty.clientSecret);
        authLoginReq.setGrantType(LoginProperty.grantType);
        authLoginReq.setUsername(phone);
        authLoginReq.setPassword(LoginProperty.password);
        ResultData<AuthLoginResp> resultData = sysLoginService.getAuthentication(authLoginReq);
        if(resultData == null || resultData.getData() == null || resultData.getData().getAccessToken() == null){
            return BaseResultUtil.fail("无法登陆，请联系管理员");
        }
        String Authentication = resultData.getData().getAccessToken();
        customerLoginVo.setAuthentication(Authentication);

        return BaseResultUtil.success(customerLoginVo);
    }

    private Customer register(String phone) {

        long currentTimeMillis = System.currentTimeMillis();

        //写入权限用户中心
        AddUserReq addUserReq = new AddUserReq();
        //addUserReq.setName();
        addUserReq.setAccount(phone);
        //addUserReq.setDeptId();
        addUserReq.setMobile(phone);
        addUserReq.setPassword(LoginProperty.password);
        //addUserReq.setRoleIdList();
        ResultData<AddUserResp> resultData = sysUserService.save(addUserReq);
        AddUserResp addUserResp = resultData.getData();

        //添加用户
        Customer customer = new Customer();
        customer.setPhone(phone);
        customer.setUserId(addUserResp.getUserId());
        customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
        customer.setSource(CustomerSourceEnum.APP.code);
        customer.setState(CustomerStateEnum.CHECKED.code);
        customer.setPayMode(PayModeEnum.CURRENT.code);
        customer.setRegisterTime(currentTimeMillis);
        customer.setCreateTime(currentTimeMillis);
        customerDao.insert(customer);
        return customer;
    }
}
