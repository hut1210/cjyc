package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjkj.usercenter.dto.common.auth.AuthMobileLoginReq;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.enums.role.RoleTitleEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.login.BaseLoginVo;
import com.cjyc.common.model.vo.driver.login.DriverLoginVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.service.ICsUserRoleDeptService;
import com.cjyc.driver.api.config.LoginProperty;
import com.cjyc.driver.api.service.ILoginService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class LoginServiceImpl extends SuperServiceImpl<IDriverDao, Driver> implements ILoginService {

    @Resource
    private IDriverDao driverDao;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ISysLoginService sysLoginService;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsUserRoleDeptService csUserRoleDeptService;

    @Override
    public ResultVo<DriverLoginVo> login(LoginDto dto) {
        //查询该手机号是否在司机表中
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                .eq(Driver::getPhone, dto.getPhone()));
        DriverLoginVo driverLoginVo = new DriverLoginVo();
        //在韵车库中不存在的
        if(driver == null){
            ResultVo rv = addToPlatform(dto.getPhone());
            if(rv.getCode() == 1){
                //失败状态码
                return BaseResultUtil.fail("登陆失败，请联系管理员");
            }
            driverLoginVo = (DriverLoginVo)rv.getData();
        }
        //查询所有的
        List<BaseLoginVo> loginVos = null;
        if(driver == null){
            loginVos = carrierDao.findDriverLogin(driverLoginVo.getId());
        }else{
            loginVos = carrierDao.findDriverLogin(driver.getId());
        }
        //韵车库中没有
        if(CollectionUtils.isEmpty(loginVos)){
            return BaseResultUtil.fail("该账号已停用");
        }
        BaseLoginVo baseLoginVo = null;
        if(!CollectionUtils.isEmpty(loginVos)){
            //处理韵车业务
            if(loginVos.size() == 1){
                baseLoginVo = loginVos.get(0);
            }
            //企业承运商
            if(loginVos.size() > 1){
                //把角色存入到set集合中
                //一个司机在多个承运商下，取角色最大的一个
                Set<Integer> set = new HashSet<>(10);
                for(BaseLoginVo vo : loginVos){
                    set.add(vo.getRole());
                }
                //取出可用的集合中role值最大的一个
                for(BaseLoginVo vo : loginVos){
                    if(vo.getRole().equals(Collections.max(set))){
                        baseLoginVo = vo;
                        break;
                    }
                }
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
        BeanUtils.copyProperties(baseLoginVo,driverLoginVo);
        driverLoginVo.setIdCard(StringUtils.isNotBlank(baseLoginVo.getIdCard()) ? baseLoginVo.getIdCard():"");
        driverLoginVo.setUserId(baseLoginVo.getUserId() == null ? 0 : baseLoginVo.getUserId());
        driverLoginVo.setPlateNo(StringUtils.isNotBlank(baseLoginVo.getPlateNo()) ? baseLoginVo.getPlateNo():"");
        driverLoginVo.setPhotoImg(StringUtils.isNotBlank(baseLoginVo.getPhotoImg()) ? baseLoginVo.getPhotoImg():"");
        driverLoginVo.setAccessToken(rd.getData().getAccessToken());
        return BaseResultUtil.success(driverLoginVo);
    }

    /**
     * 将新注册司机信息注册到物流平台和韵车
     */
    private ResultVo addToPlatform(String phone){
        DriverLoginVo dVo = new DriverLoginVo();
        //架构组和韵车添加数据
        Driver driver = new Driver();
        driver.setName(phone);
        driver.setRealName(phone);
        driver.setPhone(phone);
        driver.setType(DriverTypeEnum.SOCIETY.code);
        driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
        driver.setSource(DriverSourceEnum.APP_DRIVER.code);
        driver.setCreateTime(System.currentTimeMillis());
        //新增数据到物流平台
        ResultData<Long> rd = csDriverService.saveDriverToPlatform(driver);
        driver.setUserId(rd.getData());
        driverDao.insert(driver);

        //保存承运商信息
        Carrier carrier = new Carrier();
        carrier.setName(phone);
        carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
        carrier.setType(CarrierTypeEnum.PERSONAL.code);
        carrier.setLinkman(phone);
        carrier.setLinkmanPhone(phone);
        carrier.setSettleType(ModeTypeEnum.TIME.code);
        carrier.setState(CommonStateEnum.WAIT_CHECK.code);
        carrier.setCreateTime(System.currentTimeMillis());
        carrierDao.insert(carrier);

        //承运商与司机关系
        CarrierDriverCon cdc = new CarrierDriverCon();
        cdc.setDriverId(driver.getId());
        cdc.setCarrierId(carrier.getId());
        cdc.setState(CommonStateEnum.WAIT_CHECK.code);
        cdc.setRole(DriverRoleEnum.PERSONAL_DRIVER.code);
        carrierDriverConDao.insert(cdc);
        //组装数据
        dVo.setId(driver.getId());
        dVo.setRoleId(cdc.getId());
        dVo.setRole(cdc.getRole());
        dVo.setType(carrier.getType());
        dVo.setUserId(driver.getUserId());
        dVo.setRealName(StringUtils.isBlank(driver.getRealName()) ? "":driver.getRealName());
        dVo.setPhone(driver.getPhone());
        dVo.setIdCard("");
        dVo.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
        dVo.setBusinessState(driver.getBusinessState());
        dVo.setCompanyName(StringUtils.isBlank(carrier.getName()) ? "":carrier.getName());
        return BaseResultUtil.success(dVo);
    }


    /************************************韵车集成改版 st***********************************/

    @Override
    public ResultVo<DriverLoginVo> loginNew(LoginDto dto,String clientId,String clientSecret,String grantType) {
        //查询该手机号是否在司机表中
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                .eq(Driver::getPhone, dto.getPhone()));
        DriverLoginVo driverLoginVo = new DriverLoginVo();
        //在韵车库中不存在的
        if(driver == null){
            ResultVo rv = addDriverToPlatform(dto.getPhone());
            if (!ResultEnum.SUCCESS.getCode().equals(rv.getCode())) {
                return BaseResultUtil.fail("登陆失败，请联系管理员");
            }
            driverLoginVo = (DriverLoginVo)rv.getData();
        }
        //查询所有的
        List<BaseLoginVo> loginVos = null;
        if(driver == null){
            loginVos = carrierDao.findDriverLoginNew(driverLoginVo.getId());
        }else{
            loginVos = carrierDao.findDriverLoginNew(driver.getId());
        }
        //韵车库中没有
        if(CollectionUtils.isEmpty(loginVos)){
            return BaseResultUtil.fail("数据错误，请联系管理员");
        }
        BaseLoginVo baseLoginVo = null;
        if(!CollectionUtils.isEmpty(loginVos)){
            //处理韵车业务
            if(loginVos.size() == 1){
                baseLoginVo = loginVos.get(0);
            }
            //企业承运商
            if(loginVos.size() > 1){
                //把角色存入到set集合中
                //一个司机在多个承运商下，取角色最大的一个
                Set<Integer> set = Sets.newHashSet();
                for(BaseLoginVo vo : loginVos){
                    set.add(vo.getRole());
                }
                //取出可用的集合中role值最大的一个
                for(BaseLoginVo vo : loginVos){
                    if(vo.getRole().equals(Collections.max(set))){
                        baseLoginVo = vo;
                        break;
                    }
                }
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
            return BaseResultUtil.fail(rd.getMsg());
        }
        BeanUtils.copyProperties(baseLoginVo,driverLoginVo);
        driverLoginVo.setIdCard(StringUtils.isNotBlank(baseLoginVo.getIdCard()) ? baseLoginVo.getIdCard():"");
        driverLoginVo.setUserId(baseLoginVo.getUserId() == null ? 0 : baseLoginVo.getUserId());
        driverLoginVo.setPlateNo(StringUtils.isNotBlank(baseLoginVo.getPlateNo()) ? baseLoginVo.getPlateNo():"");
        driverLoginVo.setPhotoImg(StringUtils.isNotBlank(baseLoginVo.getPhotoImg()) ? baseLoginVo.getPhotoImg():"");
        driverLoginVo.setAccessToken(rd.getData().getAccessToken());
        return BaseResultUtil.success(driverLoginVo);
    }

    /**
     * 将新注册司机信息注册到物流平台和韵车
     */
    private ResultVo addDriverToPlatform(String phone){
        Role role = csRoleService.getByName(YmlProperty.get("cjkj.carrier_personal_driver_role_name"), DeptTypeEnum.CARRIER.code);
        if(role == null){
            return BaseResultUtil.fail("个人司机角色不存在，请先添加");
        }
        ResultData<Long> rd = csCustomerService.addUserToPlatform(phone,phone,role);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("司机信息保存失败，原因：" + rd.getMsg());
        }
        if(rd.getData() == null){
            return BaseResultUtil.fail("获取架构组userId失败");
        }
        DriverLoginVo dVo = new DriverLoginVo();
        //架构组和韵车添加数据
        Driver driver = new Driver();
        driver.setUserId(rd.getData());
        driver.setName(phone);
        driver.setRealName(phone);
        driver.setPhone(phone);
        driver.setType(DriverTypeEnum.SOCIETY.code);
        driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
        driver.setSource(DriverSourceEnum.APP_DRIVER.code);
        driver.setCreateTime(System.currentTimeMillis());
        //新增数据到物流平台
        driverDao.insert(driver);

        //保存承运商信息
        Carrier carrier = new Carrier();
        carrier.setName(phone);
        carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
        carrier.setType(CarrierTypeEnum.PERSONAL.code);
        carrier.setLinkman(phone);
        carrier.setLinkmanPhone(phone);
        carrier.setSettleType(ModeTypeEnum.TIME.code);
        carrier.setState(CommonStateEnum.WAIT_CHECK.code);
        carrier.setCreateTime(System.currentTimeMillis());
        carrierDao.insert(carrier);

        //保存司机角色机构关系
        ResultVo<UserRoleDept> resultVo = csUserRoleDeptService.saveAppDriverToUserRoleDept(carrier, driver, role.getId());
        if (!ResultEnum.SUCCESS.getCode().equals(resultVo.getCode())) {
            return BaseResultUtil.fail("司机信息保存失败，原因：" + rd.getMsg());
        }
        if(resultVo.getData() == null){
            return BaseResultUtil.fail("保存司机信息失败");
        }
        //组装数据
        dVo.setId(driver.getId());
        dVo.setRoleId(resultVo.getData().getId());
        dVo.setRole(RoleTitleEnum.PERSON_DRIVER.getCode());
        dVo.setType(carrier.getType());
        dVo.setUserId(driver.getUserId());
        dVo.setRealName(StringUtils.isBlank(driver.getRealName()) ? "":driver.getRealName());
        dVo.setPhone(driver.getPhone());
        dVo.setIdCard("");
        dVo.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
        dVo.setBusinessState(driver.getBusinessState());
        dVo.setCompanyName(StringUtils.isBlank(carrier.getName()) ? "":carrier.getName());
        return BaseResultUtil.success(dVo);
    }
}