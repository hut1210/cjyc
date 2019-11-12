package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IDriverVehicleConDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IVehicleRunningDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.web.vehicle.*;
import com.cjyc.common.model.entity.DriverVehicleCon;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.vehicle.FreeVehicleVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.service.IVehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VehicleServiceImpl extends ServiceImpl<IVehicleDao, Vehicle> implements IVehicleService {

    @Resource
    private IVehicleDao vehicleDao;

    @Resource
    private IVehicleRunningDao vehicleRunningDao;

    @Resource
    private IDriverVehicleConDao driverVehicleConDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo saveVehicle(VehicleDto dto) {
        //判断车辆是否已有
        Vehicle veh = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo,dto.getPlateNo()));
        if(veh != null){
            return BaseResultUtil.fail("该车辆已存在,请检查");
        }
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto,vehicle);
        vehicle.setOwnershipType(VehicleOwnerEnum.PERSONAL.code);
        vehicle.setCreateUserId(dto.getLoginId());
        vehicle.setCreateTime(NOW);
        super.save(vehicle);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findVehicle(SelectVehicleDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<VehicleVo> vehicleVos = vehicleDao.findVehicle(dto);
        PageInfo<VehicleVo> pageInfo = new PageInfo<>(vehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo removeVehicle(RemoveVehicleDto dto) {
        if(dto.getDriverId() != null){
            //车辆与司机有绑定关系
            //删除与司机关系
            driverVehicleConDao.removeCon(dto.getDriverId());
            vehicleRunningDao.removeRun(dto.getDriverId());
        }
        vehicleDao.deleteById(dto.getVehicleId());
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo modifyVehicle(ModifyCarryNumDto dto) {
        Vehicle vehicle = vehicleDao.selectById(dto.getVehicleId());
        vehicle.setDefaultCarryNum(dto.getDefauleCarryNum());
        vehicleDao.updateById(vehicle);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findFreeVehicle(FreeVehicleDto dto) {
        //查询个人所有车辆
        /*List<Vehicle> vehicles = vehicleDao.selectList(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getOwnershipType, VehicleOwnerEnum.PERSONAL.code)
                                            .like(!StringUtils.isNotBlank(dto.getKeyword()),Vehicle::getPlateNo,dto.getKeyword()).select(Vehicle::getId,Vehicle::getPlateNo,Vehicle::getDefaultCarryNum));*/
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findFreeVehicle(dto.getPlateNo(),dto.getCarrierId());
        //查询已经绑定的车辆
        List<DriverVehicleCon> driverVehicleCons = driverVehicleConDao.selectList(new QueryWrapper<DriverVehicleCon>().lambda().select(DriverVehicleCon::getVehicleId));
        //去除已绑定车辆
        for (DriverVehicleCon driverVehicleCon : driverVehicleCons) {
            for (FreeVehicleVo vo : freeVehicleVos) {
                if(driverVehicleCon.getVehicleId().equals(vo.getId())){
                    freeVehicleVos.remove(vo);
                    break;
                }
            }
        }
        return BaseResultUtil.success(freeVehicleVos);
    }

}