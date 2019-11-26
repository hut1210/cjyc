package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.CarrierDriverNameDto;
import com.cjyc.common.model.dto.driver.mine.CarrierVehicleNoDto;
import com.cjyc.common.model.entity.CarrierDriverCon;
import com.cjyc.common.model.entity.DriverVehicleCon;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.entity.VehicleRunning;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsVehicleService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CsVehicleServiceImpl implements ICsVehicleService {

    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

    @Override
    public ResultVo<List<FreeVehicleVo>> findPersonFreeVehicle(KeywordDto dto) {
        //获取社会所有车辆
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findPersonVehicle(dto);
        return freeVehicles(freeVehicleVos);
    }

    @Override
    public ResultVo<List<FreeVehicleVo>> findCarrierFreeVehicle(FreeDto dto) {
        //获取承运商
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc != null){
            List<FreeVehicleVo> freeVehicleVos = vehicleDao.findCarrierVehicle(cdc.getCarrierId(),dto.getPlateNo());
            return freeVehicles(freeVehicleVos);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<List<FreeVehicleVo>> findCarrierVehicle(CarrierVehicleNoDto dto) {
        List<FreeVehicleVo> freeVehicleVos = vehicleDao.findCarrierVehicle(dto.getCarrierId(),dto.getPlateNo());
        if(!CollectionUtils.isEmpty(freeVehicleVos)){
            return freeVehicles(freeVehicleVos);
        }
        return BaseResultUtil.success();
    }

    /**
     * 处理没有被绑定的车辆
     * @param freeVehicleVos
     * @return
     */
    private ResultVo<List<FreeVehicleVo>> freeVehicles(List<FreeVehicleVo> freeVehicleVos){
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
            return BaseResultUtil.success(freeVehicleVos);
        }
        return BaseResultUtil.success();
    }
}