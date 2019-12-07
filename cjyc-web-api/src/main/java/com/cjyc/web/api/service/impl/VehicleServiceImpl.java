package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IDriverVehicleConDao;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IVehicleRunningDao;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.dto.web.vehicle.*;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.entity.VehicleRunning;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.service.IVehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    @Resource
    private ITaskDao taskDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo saveVehicle(VehicleDto dto) {
        //判断车辆是否已有
        Vehicle veh = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo, dto.getPlateNo()));
        if (veh != null) {
            return BaseResultUtil.fail("该车辆已存在,请检查");
        }
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto, vehicle);
        vehicle.setOwnershipType(VehicleOwnerEnum.PERSONAL.code);
        vehicle.setCreateUserId(dto.getLoginId());
        vehicle.setCreateTime(NOW);
        boolean result = super.save(vehicle);
        if(result){
            return BaseResultUtil.success();
        }else{
            return BaseResultUtil.fail();
        }
    }

    @Override
    public ResultVo<PageVo<VehicleVo>> findVehicle(SelectVehicleDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<VehicleVo> vehicleVos = vehicleDao.findVehicle(dto);
        PageInfo<VehicleVo> pageInfo = new PageInfo<>(vehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo removeVehicle(RemoveVehicleDto dto) {
        //判断该运力是否在运输中
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(dto.getDriverId() != null, VehicleRunning::getDriverId, dto.getDriverId())
                .eq(dto.getVehicleId() != null, VehicleRunning::getVehicleId, dto.getVehicleId()));
        if (vr != null) {
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(task != null){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        if (dto.getDriverId() != null) {
            //车辆与司机有绑定关系
            //删除与司机关系
            driverVehicleConDao.removeCon(dto.getDriverId(), dto.getVehicleId());
            vehicleRunningDao.removeRun(dto.getDriverId(), dto.getVehicleId());
        }
        vehicleDao.deleteById(dto.getVehicleId());
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo modifyVehicle(ModifyCarryNumDto dto) {
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(VehicleRunning::getDriverId, dto.getDriverId())
                .eq(VehicleRunning::getVehicleId, dto.getVehicleId()));
        if (vr != null) {
            //更新运力
            VehicleRunning vRun = new VehicleRunning();
            vRun.setId(vr.getId());
            vRun.setCarryCarNum(dto.getDefaultCarryNum());
            vehicleRunningDao.updateById(vRun);
        }
        //更新车辆
        Vehicle vehicle = vehicleDao.selectById(dto.getVehicleId());
        vehicle.setDefaultCarryNum(dto.getDefaultCarryNum());
        vehicle.setCreateUserId(dto.getLoginId());
        vehicle.setCreateTime(NOW);
        vehicleDao.updateById(vehicle);
        return BaseResultUtil.success();
    }
}