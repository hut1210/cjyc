package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IDriverVehicleConDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dto.FreeVehicleDto;
import com.cjyc.common.model.entity.DriverVehicleCon;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsVehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsVehicleServiceImpl implements ICsVehicleService {

    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;

    @Override
    public ResultVo findFreeVehicle(FreeVehicleDto dto) {
        //获取该承运商下的所有车辆
        List<Vehicle> vehicles = vehicleDao.selectList(new QueryWrapper<Vehicle>().lambda()
                .eq(Vehicle::getCarrierId, dto.getCarrierId()).select(Vehicle::getId,Vehicle::getPlateNo,Vehicle::getDefaultCarryNum));
        List<FreeVehicleVo> freeVehicleVos = new ArrayList<>(10);
        //判断,copy
        if(!CollectionUtils.isEmpty(vehicles)){
            for(Vehicle vehicle : vehicles){
                FreeVehicleVo fvv = new FreeVehicleVo();
                BeanUtils.copyProperties(vehicle,fvv);
                freeVehicleVos.add(fvv);
            }
            //查询已经绑定的车辆
            List<DriverVehicleCon> driverVehicleCons = driverVehicleConDao.selectList(new QueryWrapper<DriverVehicleCon>().lambda().select(DriverVehicleCon::getVehicleId));
            //去除已绑定车辆
            for (DriverVehicleCon driverVehicleCon : driverVehicleCons) {
                for (FreeVehicleVo vo : freeVehicleVos) {
                    if(driverVehicleCon.getVehicleId().equals(vo.getId())){
                        vehicles.remove(vo);
                        break;
                    }
                }
            }
        }
        return BaseResultUtil.success(freeVehicleVos);
    }
}