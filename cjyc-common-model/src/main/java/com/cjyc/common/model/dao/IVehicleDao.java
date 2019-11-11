package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.carrier.SeleVehicleDriverDto;
import com.cjyc.common.model.dto.web.mimeCarrier.QueryMyCarDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.entity.Vehicle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.mimeCarrier.MyCarVo;
import com.cjyc.common.model.vo.web.vehicle.FreeVehicleVo;
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
     * 根据承运商id条件查询车辆信息
     * @param dto
     * @return
     */
    List<BaseVehicleVo> getBaseVehicleByTerm(SeleVehicleDriverDto dto);

    /**
     * 根据条件查询车辆信息
     * @param dto
     * @return
     */
    List<VehicleVo> findVehicle(SelectVehicleDto dto);

    /**
     * 模糊匹配查询空闲社会车辆
     * @param plateNo
     * @return
     */
    List<FreeVehicleVo> findFreeVehicle(@Param("plateNo") String plateNo,@Param("carrierId") Long carrierId);

    /**
     * 根据条件查询该承运商下的车辆
     * @param dto
     * @return
     */
    List<MyCarVo> findMyCar(QueryMyCarDto dto);

}
