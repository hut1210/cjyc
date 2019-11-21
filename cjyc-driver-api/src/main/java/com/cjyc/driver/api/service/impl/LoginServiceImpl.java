package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjkj.usercenter.dto.common.auth.AuthMobileLoginReq;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.CarrierDriverCon;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.login.BaseLoginVo;
import com.cjyc.common.model.vo.driver.login.DriverLoginVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.driver.api.config.LoginProperty;
import com.cjyc.driver.api.service.ILoginService;
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
    private ICsSendNoService sendNoService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ISysLoginService sysLoginService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

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
        List<BaseLoginVo> loginVos = carrierDao.findDriverLogin(driverLoginVo.getId());

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
        String no = sendNoService.getNo(SendNoTypeEnum.DRIVER);
        driver.setName(no);
        driver.setRealName(no);
        driver.setPhone(phone);
        driver.setType(DriverTypeEnum.SOCIETY.code);
        driver.setIdentity(DriverIdentityEnum.PERSONAL_DRIVER.code);
        driver.setSource(DriverSourceEnum.APP_DRIVER.code);
        driver.setCreateTime(NOW);
        //新增数据到物流平台
        ResultData<Long> rd = csDriverService.saveDriverToPlatform(driver);
        driver.setUserId(rd.getData());
        driverDao.insert(driver);

        //保存承运商信息
        Carrier carrier = new Carrier();
        carrier.setName(no);
        carrier.setType(CarrierTypeEnum.PERSONAL.code);
        carrier.setLinkman(no);
        carrier.setLinkmanPhone(phone);
        carrier.setSettleType(ModeTypeEnum.TIME.code);
        carrier.setState(CommonStateEnum.WAIT_CHECK.code);
        carrier.setCreateTime(NOW);
        carrierDao.insert(carrier);

        //承运商与司机关系
        CarrierDriverCon cdc = new CarrierDriverCon();
        cdc.setDriverId(driver.getId());
        cdc.setCarrierId(carrier.getId());
        cdc.setState(CommonStateEnum.WAIT_CHECK.code);
        cdc.setRole(DriverIdentityEnum.PERSONAL_DRIVER.code);
        carrierDriverConDao.insert(cdc);
        //组装数据
        dVo.setId(driver.getId());
        dVo.setUserId(driver.getUserId());
        dVo.setRealName(driver.getRealName());
        dVo.setPhone(driver.getPhone());
        dVo.setIdentity(DriverIdentityEnum.PERSONAL_DRIVER.code);
        dVo.setBusinessState(driver.getBusinessState());
        dVo.setCompanyName(carrier.getName());
        return BaseResultUtil.success(dVo);
    }
}