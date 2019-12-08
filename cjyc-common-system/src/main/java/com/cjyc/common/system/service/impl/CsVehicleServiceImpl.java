package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.AppCarrierVehicleDto;
import com.cjyc.common.model.dto.driver.mine.CarrierVehicleNoDto;
import com.cjyc.common.model.dto.web.vehicle.FreeVehicleDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.transport.VehicleRunStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.CarrierVehicleVo;
import com.cjyc.common.model.vo.driver.mine.SocietyVehicleVo;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.common.system.service.sys.ICsSysService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class CsVehicleServiceImpl implements ICsVehicleService {

    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private ICsSysService csSysService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<List<FreeVehicleVo>> findPersonFreeVehicle(KeywordDto dto) {
        //获取社会所有车辆
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findPersonVehicle(dto);
        return BaseResultUtil.success(freeVehicles(freeVehicleVos));
    }

    @Override
    public ResultVo<SocietyVehicleVo> findSocietyFreeVehicle(CarrierVehicleNoDto dto) {
        //获取承运商
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                .eq(CarrierDriverCon::getId, dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        //获取社会所有车辆
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findSocietyFreeVehicle(cdc.getCarrierId(),dto.getPlateNo());
        freeVehicleVos = freeVehicles(freeVehicleVos);
        SocietyVehicleVo vehicleVo = new SocietyVehicleVo();
        vehicleVo.setVehicleVo(freeVehicleVos);
        return BaseResultUtil.success(vehicleVo);
    }

    @Override
    public ResultVo<List<FreeVehicleVo>> findCarrierFreeVehicle(FreeDto dto) {
        //获取承运商
        Carrier carrier = csSysService.getCarrierByRoleId(dto.getRoleId());
        if(carrier != null){
            List<FreeVehicleVo> freeVehicleVos = vehicleDao.findCarrierVehicle(carrier.getId(),dto.getPlateNo());
            return  BaseResultUtil.success(freeVehicles(freeVehicleVos));
        }else{
            return BaseResultUtil.fail("该承运商管理员不存在,请检查");
        }
    }

    @Override
    public ResultVo<CarrierVehicleVo> findCompanyFreeVehicle(CarrierVehicleNoDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                .eq(CarrierDriverCon::getId, dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findCarrierVehicle(cdc.getCarrierId(),dto.getPlateNo());
        CarrierVehicleVo vehicleVo = new CarrierVehicleVo();
        freeVehicleVos = freeVehicles(freeVehicleVos);
        vehicleVo.setVehicleVo(freeVehicleVos);
        return BaseResultUtil.success(vehicleVo);
    }

    @Override
    public ResultVo<List<FreeVehicleVo>> findCarrierVehicleById(FreeVehicleDto dto) {
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findCarrierVehicle(dto.getCarrierId(),dto.getPlateNo());
        return BaseResultUtil.success(freeVehicles(freeVehicleVos));
    }

    @Override
    public boolean verifyDriverVehicle(Long driverId,Long vehicleId) {
        //验证司机与车辆绑定关系
        List<DriverVehicleCon> dvc = driverVehicleConDao.selectList(new QueryWrapper<DriverVehicleCon>().lambda()
                .eq(driverId != null,DriverVehicleCon::getDriverId, driverId)
                .or()
                .eq(vehicleId != null,DriverVehicleCon::getVehicleId, vehicleId));
        //验证运力
        List<VehicleRunning> vr = vehicleRunningDao.selectList(new QueryWrapper<VehicleRunning>().lambda()
                .eq(driverId != null,VehicleRunning::getDriverId, driverId)
                .or()
                .eq(vehicleId != null,VehicleRunning::getVehicleId, vehicleId));
        if(CollectionUtils.isEmpty(dvc) && CollectionUtils.isEmpty(vr)){
            return true;
        }
        return false;
    }

    @Override
    public void saveTransport(CarrierVehicleDto dto, AppCarrierVehicleDto appDto, Vehicle veh) {
        //新增运力
        VehicleRunning vr = new VehicleRunning();
        vr.setVehicleId(veh.getId());
        vr.setRunningState(VehicleRunStateEnum.FREE.code);
        vr.setCreateTime(NOW);
        //新增车辆与司机关系
        DriverVehicleCon dvc = new DriverVehicleCon();
        dvc.setVehicleId(veh.getId());
        if(dto != null){
            BeanUtils.copyProperties(dto,vr);
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            dvc.setDriverId(dto.getDriverId());
        }else if(appDto != null){
            BeanUtils.copyProperties(appDto,vr);
            vr.setCarryCarNum(appDto.getDefaultCarryNum());
            dvc.setDriverId(appDto.getDriverId());
        }
        vehicleRunningDao.insert(vr);
        driverVehicleConDao.insert(dvc);
    }

    @Override
    public void updateTransport(CarrierVehicleDto dto, AppCarrierVehicleDto appDto, Vehicle veh) {
        //更新司机与车辆
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                .eq(veh.getId() != null,DriverVehicleCon::getVehicleId, veh.getId()));
        //更新运力
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(veh.getId() != null,VehicleRunning::getVehicleId, veh.getId()));
        vr.setRunningState(VehicleRunStateEnum.FREE.code);
        vr.setUpdateTime(NOW);
        if(dto != null){
            dvc.setDriverId(dto.getDriverId());
            BeanUtils.copyProperties(dto,vr);
        }else if(appDto != null){
            dvc.setDriverId(appDto.getDriverId());
            BeanUtils.copyProperties(appDto,vr);
        }
        driverVehicleConDao.updateById(dvc);
        vehicleRunningDao.updateById(vr);
    }

    /**
     * 处理没有被绑定的车辆
     * @param freeVehicleVos
     * @return
     */
    private List<FreeVehicleVo> freeVehicles(List<FreeVehicleVo> freeVehicleVos){
        if(!CollectionUtils.isEmpty(freeVehicleVos)){
            //查询已经绑定的车辆
            List<DriverVehicleCon> driverVehicleCons = driverVehicleConDao.selectList(new QueryWrapper<DriverVehicleCon>().lambda().select(DriverVehicleCon::getVehicleId));
            //去除已绑定车辆
            if(!CollectionUtils.isEmpty(driverVehicleCons)){
                for (DriverVehicleCon driverVehicleCon : driverVehicleCons) {
                    for (FreeVehicleVo vo : freeVehicleVos) {
                        if(driverVehicleCon.getVehicleId().equals(vo.getVehicleId())){
                            freeVehicleVos.remove(vo);
                            break;
                        }
                    }
                }
            }
            return freeVehicleVos;
        }
        return Collections.emptyList();
    }
}