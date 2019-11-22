package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.mine.DeleteVehicleDto;
import com.cjyc.common.model.dto.driver.mine.PersonDriverDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.BaseDto;
import com.cjyc.common.model.dto.driver.mine.FrozenDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.enums.transport.VehicleRunStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;
import com.cjyc.common.model.vo.driver.mine.DriverInfoVo;
import com.cjyc.common.model.vo.driver.mine.DriverVehicleVo;
import com.cjyc.driver.api.service.IMineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MineServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IMineService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private IBankCardBindDao bankCardBindDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private ITaskDao taskDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<List<BinkCardVo>> findBinkCard(BaseDto dto) {
        List<BinkCardVo> bankCardVos = bankCardBindDao.findBinkCardInfo(dto.getLoginId());
        return BaseResultUtil.success(CollectionUtils.isEmpty(bankCardVos) ? Collections.EMPTY_LIST:bankCardVos);
    }

    @Override
    public ResultVo<PageVo<DriverInfoVo>> findDriver(BaseDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<Long> driverIds = driverDao.findDriverIds(dto.getLoginId());
        List<DriverInfoVo> driverInfo = null;
        if(!CollectionUtils.isEmpty(driverIds)){
            driverInfo = driverDao.findDriverInfo(driverIds);
        }
        PageInfo<DriverInfoVo> pageInfo = new PageInfo(driverInfo);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>():pageInfo);
    }

    @Override
    public ResultVo frozenDriver(FrozenDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                                .eq(CarrierDriverCon::getDriverId,dto.getDriverId())
                                .eq(CarrierDriverCon::getCarrierId,dto.getCarrierId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在");
        }
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getId, dto.getDriverId()));
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在");
        }
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(NOW);
        driverDao.updateById(driver);

        cdc.setState(CommonStateEnum.FROZEN.code);
        carrierDriverConDao.updateById(cdc);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo addOrModifyVehicle(PersonDriverDto dto) {
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId, dto.getLoginId()));
        if(vr == null){
            //新增
            //根据车牌号查询库中有没有添加
            Vehicle veh = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo,dto.getPlateNo()));
            if(veh != null){
                return BaseResultUtil.getVo(ResultEnum.EXIST_VEHICLE.getCode(),ResultEnum.EXIST_VEHICLE.getMsg());
            }
            //获取carrierId
            CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getId, dto.getRoleId())
                    .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
            //添加车辆
            if(cdc == null){
                return BaseResultUtil.fail("数据错误");
            }
            saveVehicle(cdc,dto);
        }else{
            //修改
            mofidyVehicle(dto);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo deleteVehicle(DeleteVehicleDto dto) {
        //判断该运力是否在运输中
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(VehicleRunning::getDriverId, dto.getLoginId())
                .eq(VehicleRunning::getVehicleId, dto.getVehicleId()));
        if(vr != null){
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda().eq(Task::getVehicleRunningId,vr.getId()));
            if(task != null && task.getState() == TaskStateEnum.TRANSPORTING.code){
                return BaseResultUtil.getVo(ResultEnum.VEHICLE_RUNNING.getCode(),ResultEnum.VEHICLE_RUNNING.getMsg());
            }
        }
        if(dto.getLoginId() != null){
            //车辆与司机有绑定关系
            //删除与司机关系
            driverVehicleConDao.removeCon(dto.getLoginId(),dto.getVehicleId());
            vehicleRunningDao.removeRun(dto.getLoginId(),dto.getVehicleId());
        }
        vehicleDao.deleteById(dto.getVehicleId());
        return BaseResultUtil.success();
    }

    /**
     * 保存车辆信息
     * @param cdc
     * @param dto
     */
    private void saveVehicle(CarrierDriverCon cdc,PersonDriverDto dto){
        //保存车辆信息
        Vehicle veh = new Vehicle();
        veh.setCarrierId(cdc.getCarrierId());
        veh.setPlateNo(dto.getPlateNo());
        veh.setDefaultCarryNum(dto.getDefaultCarryNum());
        veh.setOwnershipType(VehicleOwnerEnum.PERSONAL.code);
        veh.setCreateTime(NOW);
        veh.setCarrierId(dto.getLoginId());
        vehicleDao.insert(veh);

        //保存车辆与司机关系
        DriverVehicleCon dvc = new DriverVehicleCon();
        dvc.setVehicleId(veh.getId());
        dvc.setDriverId(dto.getLoginId());
        driverVehicleConDao.insert(dvc);

        //保存运力信息
        VehicleRunning vr = new VehicleRunning();
        vr.setDriverId(dto.getLoginId());
        vr.setVehicleId(veh.getId());
        vr.setPlateNo(dto.getPlateNo());
        vr.setCarryCarNum(dto.getDefaultCarryNum());
        vr.setRunningState(VehicleRunStateEnum.FREE.code);
        vr.setCreateTime(NOW);
        vehicleRunningDao.insert(vr);
    }

    /**
     * 修改个人车辆信息
     * @param dto
     */
    private void mofidyVehicle(PersonDriverDto dto){
        //获取车辆
        Vehicle vehicle = vehicleDao.selectById(dto.getVehicleId());
        vehicle.setDefaultCarryNum(dto.getDefaultCarryNum());
        vehicle.setPlateNo(dto.getPlateNo());
        vehicleDao.updateById(vehicle);
        //修改运力
        VehicleRunning vehr = vehicleRunningDao.selectById(dto.getVehicleId());
        vehr.setPlateNo(dto.getPlateNo());
        vehr.setCarryCarNum(dto.getDefaultCarryNum());
        vehicleRunningDao.updateById(vehr);
    }

    @Override
    public ResultVo findVehicle(BaseDriverDto dto) {
        List<Long> driverIds = null;
        CarrierDriverCon cdc = carrierDriverConDao.selectById(dto.getRoleId());
        if(cdc != null){
            if(cdc.getRole() < 2){
                //普通司机
                driverIds.add(dto.getLoginId());
            }else{
                driverIds = driverDao.findDriverIds(dto.getLoginId());
            }
            PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
            List<DriverVehicleVo> vehicleVos = driverDao.findVehicle(driverIds);
            PageInfo<DriverVehicleVo> pageInfo = new PageInfo(vehicleVos);
            return BaseResultUtil.success(pageInfo == null ? new PageInfo<>():pageInfo);
        }
        return BaseResultUtil.fail("数据错误");
    }
}
