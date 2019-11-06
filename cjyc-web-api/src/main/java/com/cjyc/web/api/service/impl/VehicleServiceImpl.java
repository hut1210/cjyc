package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IDriverVehicleConDao;
import com.cjyc.common.model.dao.IVehicleDao;
import com.cjyc.common.model.dao.IVehicleRunningDao;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.web.api.service.IVehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class VehicleServiceImpl extends ServiceImpl<IVehicleDao, Vehicle> implements IVehicleService {

    @Resource
    private IVehicleDao iVehicleDao;

    @Resource
    private IVehicleRunningDao iVehicleRunningDao;

    @Resource
    private IDriverVehicleConDao iDriverVehicleConDao;

    @Override
    public boolean saveVehicle(VehicleDto dto) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto,vehicle);
        vehicle.setOwnershipType(VehicleOwnerEnum.PERSONAL.code);
        vehicle.setCreateUserId(dto.getUserId());
        vehicle.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        return super.save(vehicle);
    }

}