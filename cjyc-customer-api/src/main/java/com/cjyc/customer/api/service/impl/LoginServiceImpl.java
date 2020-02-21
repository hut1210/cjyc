package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjkj.usercenter.dto.common.auth.AuthMobileLoginReq;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IUserRoleDeptDao;
import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.entity.UserRoleDept;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.customer.CustomerPayEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.service.ICsUserRoleDeptService;
import com.cjyc.customer.api.config.LoginProperty;
import com.cjyc.customer.api.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 登录
 * @author JPG
 */
@Service
@Slf4j
public class LoginServiceImpl extends SuperServiceImpl<ICustomerDao, Customer> implements ILoginService {

    @Resource
    private ICustomerDao customerDao;
    @Autowired
    private StringRedisUtil redisUtil;
    @Resource
    private ISysLoginService sysLoginService;
    @Resource
    private ICsSendNoService sendNoService;
    @Resource
    private ICsCustomerService comCustomerService;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ICsUserRoleDeptService csUserRoleDeptService;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<CustomerLoginVo> login(LoginDto dto) {
        Customer c = customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getContactPhone, dto.getPhone()));
        if(c == null){
            //添加数据
           c = addToPlatform(dto.getPhone());
        }
        if(c != null){
            if(c.getType() == CustomerTypeEnum.ENTERPRISE.code){
                return BaseResultUtil.fail("大客户不能登录app");
            }
            if((c.getState() == CustomerStateEnum.REJECT.code) || (c.getState() == CustomerStateEnum.FROZEN.code)){
                return BaseResultUtil.fail("账号处于冻结/审核拒绝不能登录app");
            }
        }
        //调用架构组验证手机号验证码登陆
        AuthMobileLoginReq req = new AuthMobileLoginReq();
        req.setClientId(LoginProperty.clientId);
        req.setClientSecret(LoginProperty.clientSecret);
        req.setGrantType(LoginProperty.grantType);
        req.setMobile(dto.getPhone());
        req.setSmsCode(dto.getCode());
        ResultData<AuthLoginResp> rd = sysLoginService.mobileLogin(req);
        if(rd == null || rd.getData() == null || rd.getData().getAccessToken() == null){
            return BaseResultUtil.getVo(ResultEnum.LOGIN_FAIL.getCode(),ResultEnum.LOGIN_FAIL.getMsg());
        }
        //组装返回给移动端
        CustomerLoginVo loginVo = new CustomerLoginVo();
        BeanUtils.copyProperties(c,loginVo);
        loginVo.setUserId(c.getUserId() == null ? 0 : c.getUserId());
        loginVo.setAccessToken(rd.getData().getAccessToken());
        loginVo.setPhotoImg(StringUtils.isNotBlank(c.getPhotoImg()) ? c.getPhotoImg():"");
        loginVo.setName(c.getName());
        loginVo.setContactMan(c.getContactMan());
        loginVo.setPhone(c.getContactPhone());
        return BaseResultUtil.success(loginVo);
    }

    /**
     * 添加用户到架构组/韵车库中
     * @param phone
     * @return
     */
    private Customer addToPlatform(String phone){
        Customer c = new Customer();
        String no = sendNoService.getNo(SendNoTypeEnum.CUSTOMER);
        c.setName(no);
        c.setAlias(no);
        c.setContactMan(no);
        c.setContactPhone(phone);
        c.setCustomerNo(no);
        c.setPayMode(CustomerPayEnum.TIME_PAY.code);
        c.setType(CustomerTypeEnum.INDIVIDUAL.code);
        c.setState(CommonStateEnum.CHECKED.code);
        c.setSource(CustomerSourceEnum.APP.code);
        c.setRegisterTime(NOW);
        c.setCreateTime(NOW);
        //新增用户信息到物流平台
        ResultData<Long> rd = comCustomerService.addCustomerToPlatform(c);
        c.setUserId(rd.getData());
        customerDao.insert(c);
        return c;
    }

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
        //customer = comCustomerService.save(customer);
        return customer;
    }


    /************************************韵车集成改版 st***********************************/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo<CustomerLoginVo> loginNew(LoginDto dto,String clientId,String clientSecret,String grantType) {
        Customer customer = customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getContactPhone, dto.getPhone()));
        if(customer == null){
            //添加数据
            ResultVo<Customer> rd = addUserToPlatform(dto.getPhone());
            if(!ResultEnum.SUCCESS.getCode().equals(rd.getCode())){
                return BaseResultUtil.fail("新增账号错误,请联系管理员");
            }
            customer = rd.getData();
        }
        UserRoleDept urd = null;
        if(customer != null){
            if(customer.getType() == CustomerTypeEnum.ENTERPRISE.code){
                return BaseResultUtil.fail("大客户不能登录app");
            }
            urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                    .eq(UserRoleDept::getUserId, customer.getId())
                    .eq(UserRoleDept::getDeptType, DeptTypeEnum.CUSTOMER.code)
                    .eq(UserRoleDept::getUserType, UserTypeEnum.CUSTOMER.code));
            if(urd == null){
                return BaseResultUtil.fail("数据错误,请先检查");
            }
            if((urd.getState() == CustomerStateEnum.REJECT.code) || (urd.getState() == CustomerStateEnum.FROZEN.code)){
                return BaseResultUtil.fail("账号处于冻结/审核拒绝不能登录app");
            }
        }
        //调用架构组验证手机号验证码登陆
        AuthMobileLoginReq req = new AuthMobileLoginReq();
        req.setClientId(clientId);
        req.setClientSecret(clientSecret);
        req.setGrantType(grantType);
        req.setMobile(dto.getPhone());
        req.setSmsCode(dto.getCode());
        ResultData<AuthLoginResp> rd = sysLoginService.mobileLogin(req);
        if(rd == null || rd.getData() == null || rd.getData().getAccessToken() == null){
            return BaseResultUtil.getVo(ResultEnum.LOGIN_FAIL.getCode(),ResultEnum.LOGIN_FAIL.getMsg());
        }
        //组装返回给移动端
        CustomerLoginVo loginVo = new CustomerLoginVo();
        BeanUtils.copyProperties(customer,loginVo);
        loginVo.setState(urd.getState());
        loginVo.setUserId(customer.getUserId() == null ? 0 : customer.getUserId());
        loginVo.setAccessToken(rd.getData().getAccessToken());
        loginVo.setPhotoImg(StringUtils.isNotBlank(customer.getPhotoImg()) ? customer.getPhotoImg():"");
        loginVo.setName(customer.getName());
        loginVo.setContactMan(customer.getContactMan());
        loginVo.setPhone(customer.getContactPhone());
        return BaseResultUtil.success(loginVo);
    }

    /**
     * 添加用户到架构组/韵车库中
     * @param phone
     * @return
     */
    private ResultVo<Customer> addUserToPlatform(String phone){
        Customer customer = new Customer();
        String no = sendNoService.getNo(SendNoTypeEnum.CUSTOMER);
        customer.setName(no);
        customer.setAlias(no);
        customer.setContactMan(no);
        customer.setContactPhone(phone);
        customer.setCustomerNo(no);
        customer.setPayMode(CustomerPayEnum.TIME_PAY.code);
        customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
        customer.setSource(CustomerSourceEnum.APP.code);
        customer.setCreateTime(NOW);
        Role role = csRoleService.getByName(YmlProperty.get("cjkj.customer_client_role_name"), DeptTypeEnum.CUSTOMER.code);
        if(role == null){
            return BaseResultUtil.fail("C端客户角色不存在，请先添加");
        }
        //新增用户信息到物流平台
        ResultData<Long> rd = comCustomerService.addUserToPlatform(phone,no,role);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        if(rd.getData() != null){
            customer.setUserId(rd.getData());
        }
        customerDao.insert(customer);
        //保存用户角色机构关系
        csUserRoleDeptService.saveCustomerToUserRoleDept(customer,role.getId(),customer.getId());
        return BaseResultUtil.success(customer);
    }

}
