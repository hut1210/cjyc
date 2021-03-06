package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.web.carrier.TransportDto;
import com.cjyc.common.model.dto.web.mineCarrier.QueryMyCarDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.entity.Vehicle;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.web.carrier.TransportVehicleVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarVo;
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
     * 根据条件查询车辆信息
     * @param dto
     * @return
     */
    List<VehicleVo> findVehicle(SelectVehicleDto dto);

    /**
     * 根据条件查询该承运商下的车辆
     * @param dto
     * @return
     */
    List<MyCarVo> findMyCar(QueryMyCarDto dto);

    /**
     * 获取该承运商下车辆信息
     * @param dto
     * @return
     */
    List<TransportVehicleVo> findTransportVehicle(TransportDto dto);

    /**
     * 获取所有社会车辆
     * @return
     */
    List<FreeVehicleVo> findPersonVehicle(KeywordDto keyword);

    /**
     * 司机端获取个人司机的空闲车辆
     * @param
     * @return
     */
    List<FreeVehicleVo> findSocietyFreeVehicle(@Param("carrierId") Long carrierId,@Param("plateNo") String plateNo);

    /**
     * 获取该承运商下的车辆
     * @return
     */
    List<FreeVehicleVo> findCarrierVehicle(@Param("carrierId") Long carrierId,@Param("plateNo") String plateNo);




    /************************************韵车集成改版 st***********************************/

    /**
     * 根据条件查询该承运商下的车辆
     * @param dto
     * @return
     */
    List<MyCarVo> findMyCarNew(QueryMyCarDto dto);

    /**
     * 获取该承运商下车辆信息_改版
     * @param dto
     * @return
     */
    List<TransportVehicleVo> findTransportVehicleNew(TransportDto dto);

    /**
     * 功能描述: 根据司机ID查询司机车辆信息
     * @author liuxingxiang
     * @date 2020/4/20
     * @param driverId
     * @return com.cjyc.common.model.entity.Vehicle
     */
    Vehicle selectVehicleInfoByDriverId(Long driverId);

}
