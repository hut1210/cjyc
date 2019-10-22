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

    /**
     * 根据车辆id查询车辆信息
     * @param vehicleId
     * @return
     */
    VehicleVo showVehicle(Long vehicleId);

    /**
     * 根据车辆id更新车辆信息
     * @param dto
     * @return
     */
    boolean updateVehicle(VehicleDto dto);

    /**
     * 根据条件查询车辆信息
     * @param dto
     * @return
     */
    PageInfo<VehicleVo> getVehicleByTerm(SelectVehicleDto dto);

    /**
     * 根据车辆ids删除车辆信息
     * @param ids
     * @return
     */
    boolean delVehicleByIds(List<Long> ids);
}
