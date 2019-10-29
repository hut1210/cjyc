package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.carrier.SeleVehicleDriverDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.entity.Vehicle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 运输车辆表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IVehicleDao extends BaseMapper<Vehicle> {

    /**
     * 根据车辆编号id获取车辆信息
     * @param vehicleId
     * @return
     */
    VehicleVo getVehicleById(@Param("vehicleId") Long vehicleId);

    /**
     * 根据条件查询车辆信息
     * @param dto
     * @return
     */
    List<VehicleVo> getVehicleByTerm(SelectVehicleDto dto);

    /**
     * 根据承运商id条件查询车辆信息
     * @param dto
     * @return
     */
    List<BaseVehicleVo> getBaseVehicleByTerm(SeleVehicleDriverDto dto);

}
