package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:22
 *  @Description:运输车辆管理
 */
public interface IVehicleService {

    /**
     * 添加车辆信息
     * @param dto
     * @return
     */
    boolean saveVehicle(VehicleDto dto);

}
