package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mimeCarrier.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mimeCarrier.MyCarVo;
import com.cjyc.common.model.vo.web.mimeCarrier.MyDriverVo;
import com.cjyc.common.model.vo.web.mimeCarrier.MyFreeDriverVo;
import com.cjyc.common.model.vo.web.vehicle.FreeVehicleVo;
import com.cjyc.web.api.service.IMimeCarrierService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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

    @Resource
    private ICarrierCarCountDao carrierCarCountDao;

    @Resource
    private IVehicleDao vehicleDao;

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

    @Override
    public ResultVo findPageDriver(QueryMyDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        if(dto.getCarrierId() == null){
            //此时登陆时承运商管理员，loginId --- carrierId
            //承运商超级管理员登陆，查询司机所在的承运商
            CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getDriverId,dto.getLoginId()));
            if(cdc != null){
                dto.setCarrierId(cdc.getCarrierId());
            }
        }
        List<MyDriverVo> myDriverVos =  driverDao.findMyDriver(dto);
        if(!CollectionUtils.isEmpty(myDriverVos)){
            for(MyDriverVo vo : myDriverVos){
                CarrierCarCount count = carrierCarCountDao.driverCount(vo.getDriverId());
                if(count != null){
                    vo.setCarNum(count.getCarNum() == null ? 0:count.getCarNum());
                }
            }
        }
        PageInfo<MyDriverVo> pageInfo = new PageInfo<>(myDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo verifyDriver(OperateDto dto) {
        Driver driver = driverDao.selectById(dto.getId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda().eq(CarrierDriverCon::getDriverId,dto.getId()));
        if(dto.getFlag() == FlagEnum.ADMINISTRATOR.code){
            //设为管理员
            driver.setIdentity(DriverIdentityEnum.ADMIN.code);
            //修改身份
            cdc.setRole(DriverIdentityEnum.ADMIN.code);
            carrierDriverConDao.updateById(cdc);
        }else if(dto.getFlag() == FlagEnum.REMOVE_ADMINISTRATOR.code){
            //解除管理员
            driver.setIdentity(DriverIdentityEnum.SUB_DRIVER.code);
            //修改身份
            cdc.setRole(DriverIdentityEnum.SUB_DRIVER.code);
            carrierDriverConDao.updateById(cdc);
        }else if(dto.getFlag() == FlagEnum.FROZEN.code){
            //冻结
            driver.setState(CommonStateEnum.FROZEN.code);
        }else if(dto.getFlag() == FlagEnum.THAW.code){
            //解冻
            driver.setState(CommonStateEnum.CHECKED.code);
        }
        driverDao.updateById(driver);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo saveCar(MyCarDto dto) {
        Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo,dto.getPlateNo()));
        if(vehicle != null){
            return BaseResultUtil.fail("该车辆已添加，请核对");
        }
        Vehicle veh = new Vehicle();
        BeanUtils.copyProperties(dto,veh);
        veh.setOwnershipType(VehicleOwnerEnum.CARRIER.code);
        //承运商超级管理员登陆，查询司机所在的承运商
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId,dto.getLoginId()));
        if(cdc != null){
            veh.setCarrierId(cdc.getCarrierId());
        }
        veh.setCreateUserId(dto.getLoginId());
        veh.setCreateTime(NOW);
        vehicleDao.insert(veh);
        //选择绑定司机
        if(StringUtils.isNotBlank(dto.getPlateNo())){
            //有司机，绑定与车辆关系
            DriverVehicleCon dvc = new DriverVehicleCon();
            dvc.setDriverId(dto.getDriverId());
            dvc.setVehicleId(veh.getId());
            driverVehicleConDao.insert(dvc);
            //保存运力关系
            VehicleRunning vr = new VehicleRunning();
            vr.setDriverId(dto.getDriverId());
            vr.setVehicleId(veh.getId());
            vr.setPlateNo(dto.getPlateNo());
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            vr.setState(VehicleStateEnum.EFFECTIVE.code);
            vr.setRunningState(BusinessStateEnum.OUTAGE.code);
            vr.setCreateTime(NOW);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findPageCar(QueryMyCarDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        if(dto.getCarrierId() == null){
            //此时登陆时承运商管理员，loginId --- carrierId
            //承运商超级管理员登陆，查询司机所在的承运商
            CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getDriverId,dto.getLoginId()));
            if(cdc != null){
                dto.setCarrierId(cdc.getCarrierId());
            }
        }
        List<MyCarVo> myCarVos = vehicleDao.findMyCar(dto);
        PageInfo<MyCarVo> pageInfo = new PageInfo<>(myCarVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo findFreeDriver(Long carrierId, String realName) {
        //查询该承运商下的符合的全部司机
        List<MyFreeDriverVo> freeDriverVos = driverDao.findMyAllDriver(carrierId,realName);
        //查询已被绑定的司机
        List<Long> driverIds = driverDao.findMyBusyDriver(carrierId);
        //去除已绑定司机
        for (Long driverId : driverIds) {
            for (MyFreeDriverVo vo : freeDriverVos) {
                if(driverId.equals(vo.getDriverId())){
                    freeDriverVos.remove(vo);
                    break;
                }
            }
        }
        return BaseResultUtil.success(freeDriverVos);
    }

    @Override
    public ResultVo modifyVehicleDriver(ModifyMyCarDto dto) {
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getVehicleId,dto.getVehicleId()));
        if((dvc != null && dto.getDriverId().equals(dvc.getDriverId()))
            || (dvc == null && dto.getDriverId() == null)){
            //选择与之前相同的司机绑定或者之前与现在都没绑定司机
            return BaseResultUtil.success();
        }
        if(dto.getDriverId() != null){
            if(dvc != null){
                //之前绑定与现在绑定不相同，先解绑再绑定新的
                driverVehicleConDao.deleteById(dvc);
                vehicleRunningDao.updateVehicleRunning(dto.getVehicleId());
            }
            if(dvc == null || !dto.getDriverId().equals(dvc.getDriverId())){
                //绑定新的
                DriverVehicleCon driverCon = new DriverVehicleCon();
                driverCon.setDriverId(dto.getDriverId());
                driverCon.setVehicleId(dto.getVehicleId());
                driverVehicleConDao.insert(driverCon);
                //保存运力关系
                VehicleRunning vr = new VehicleRunning();
                vr.setDriverId(dto.getDriverId());
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(VehicleStateEnum.EFFECTIVE.code);
                vr.setRunningState(BusinessStateEnum.OUTAGE.code);
                vr.setCreateTime(NOW);
            }
        }
        return BaseResultUtil.success();
    }
}