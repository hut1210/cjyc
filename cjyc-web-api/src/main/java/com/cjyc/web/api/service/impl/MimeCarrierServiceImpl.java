package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.mimeCarrier.MyDriverDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IMimeCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class MimeCarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements IMimeCarrierService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

    @Resource
    private IDriverDao driverDao;

    @Resource
    private IDriverVehicleConDao driverVehicleConDao;

    @Resource
    private IVehicleRunningDao vehicleRunningDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo saveDriver(MyDriverDto dto) {
        Driver dri = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                                            .eq(Driver::getPhone,dto.getPhone()).or()
                                            .eq(Driver::getIdCard,dto.getIdCard()));
        if(dri != null){
            return BaseResultUtil.fail("该账号已添加,请重新输入");
        }
        //保存司机
        Driver driver = new Driver();
        BeanUtils.copyProperties(dto,driver);
        driver.setType(DriverTypeEnum.SOCIETY.code);
        driver.setIdentity(DriverIdentityEnum.SUB_DRIVER.code);
        driver.setBusinessState(BusinessStateEnum.OUTAGE.code);
        if(dto.getCarrierId() == null){
            //承运商超级管理员登陆
            driver.setSource(DriverSourceEnum.CARRIER_ADMIN.code);
        }else {
            //业务员登陆
            driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
        }
        driver.setState(CommonStateEnum.CHECKED.code);
        driver.setCreateUserId(dto.getLoginId());
        driver.setCreateTime(NOW);
        driverDao.insert(driver);

        //保存司机与承运商关系
        CarrierDriverCon driverCon = new CarrierDriverCon();
        if(dto.getCarrierId() != null){
            //业务员添加
            driverCon.setCarrierId(dto.getCarrierId());
        }else{
            //承运商超级管理员登陆，查询司机所在的承运商
            CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getDriverId,dto.getLoginId()));
            if(cdc != null){
                driverCon.setCarrierId(cdc.getCarrierId());
            }
        }
        driverCon.setDriverId(driver.getId());
        driverCon.setRole(DriverIdentityEnum.SUB_DRIVER.code);
        carrierDriverConDao.insert(driverCon);
        //车牌号不为空
        if(StringUtils.isNotBlank(dto.getPlateNo())){
            //保存司机与车辆关系
            DriverVehicleCon dvc = new DriverVehicleCon();
            dvc.setVehicleId(dto.getVehicleId());
            dvc.setDriverId(driver.getId());
            driverVehicleConDao.insert(dvc);

            //保存运力信息
            VehicleRunning vr = new VehicleRunning();
            vr.setDriverId(driver.getId());
            vr.setVehicleId(dto.getVehicleId());
            vr.setPlateNo(dto.getPlateNo());
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            vr.setState(VehicleStateEnum.EFFECTIVE.code);
            vr.setRunningState(BusinessStateEnum.OUTAGE.code);
            vr.setCreateTime(NOW);
            vehicleRunningDao.insert(vr);
        }
        return BaseResultUtil.success();
    }
}