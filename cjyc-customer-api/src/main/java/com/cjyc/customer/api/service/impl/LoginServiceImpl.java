package com.cjyc.customer.api.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.customer.api.config.LoginProperty;
import com.cjyc.customer.api.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录
 * @author JPG
 */
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
    private ICsCustomerService comCustomerService;


    @Override
    public ResultVo<CustomerLoginVo> loginByPhone(LoginByPhoneDto paramsDto) {
        //返回内容
        CustomerLoginVo customerLoginVo = new CustomerLoginVo();
        //参数
        String phone = paramsDto.getPhone();
        String captcha = paramsDto.getCaptcha();
        //校验验证码
        String key = RedisKeys.getCaptchaKey(ClientEnum.APP_CUSTOMER, phone, CaptchaTypeEnum.LOGIN);
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
            return BaseResultUtil.fail("登录失败,请联系管理员");
        }
        String authentication = resultData.getData().getAccessToken();
        customerLoginVo.setAuthentication(authentication);

        //获取token

        return BaseResultUtil.success(customerLoginVo);
    }

    /**
     * 注册用户
     * @param phone
     * @return
     */
    private Customer register(String phone) {

        long currentTimeMillis = System.currentTimeMillis();
        //添加用户
        Customer customer = new Customer();
        customer.setContactPhone(phone);
        customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
        customer.setSource(CustomerSourceEnum.APP.code);
        customer.setState(CustomerStateEnum.CHECKED.code);
        customer.setPayMode(PayModeEnum.COLLECT.code);
        customer.setRegisterTime(currentTimeMillis);
        customer.setCreateTime(currentTimeMillis);
        customer = comCustomerService.save(customer);
        return customer;
    }
}
