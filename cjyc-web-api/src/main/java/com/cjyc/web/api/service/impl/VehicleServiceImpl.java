package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IDriverVehicleConDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IVehicleRunningDao;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.vehicle.DriverVehicleConDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.entity.CarrierCarCount;
import com.cjyc.common.model.entity.DriverVehicleCon;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.service.IVehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Override
    public boolean saveVehicle(VehicleDto dto) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto,vehicle);
        vehicle.setOwnershipType(VehicleOwnerEnum.PERSONAL.code);
        vehicle.setCreateUserId(dto.getUserId());
        vehicle.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        return super.save(vehicle);
    }

    @Override
    public ResultVo findVehicle(SelectVehicleDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<VehicleVo> vehicleVos = vehicleDao.findVehicle(dto);
        PageInfo<VehicleVo> pageInfo = new PageInfo<>(vehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo verifyVehicle(OperateDto dto) {
        if(FlagEnum.DELETE.code == dto.getFlag()){
            //删除与司机关系
            driverVehicleConDao.removeCon(dto.getId());
            vehicleRunningDao.removeRun(dto.getId());
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo modifyVehicle(DriverVehicleConDto dto) {
        //判断车辆是否存在
        Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo, dto.getPlateNo()));
        if(vehicle == null){
            return BaseResultUtil.fail("该车辆不存在");
        }
        if(!vehicle.getId().equals(dto.getId())){
            //该车辆与之前车辆不相同
            //更新车辆与司机绑定关系
            DriverVehicleCon con = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getVehicleId, vehicle.getId()));
            if(con != null){
               // con.setVehicleId()
            }
        }
        return null;
    }

}