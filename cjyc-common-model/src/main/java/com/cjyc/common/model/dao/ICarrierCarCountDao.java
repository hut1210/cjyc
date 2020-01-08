package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CarrierCarCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 司机运输车辆统计表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
public interface ICarrierCarCountDao extends BaseMapper<CarrierCarCount> {

    /**
     * 根据承运商id查询该承运商的运车数和总收入
     * @param carrierId
     * @return
     */
    CarrierCarCount count(@Param("carrierId") Long carrierId);

    /**
     * 根据司机id统计司机运车数和总收入
     * @param driverId
     * @return
     */
    CarrierCarCount driverCount(@Param("driverId") Long driverId);

    /*********************************韵车集成改版 st*****************************/
    /**
     * 根据承运商id查询该承运商的运车数和总收入_改版
     * @param carrierId
     * @return
     */
    CarrierCarCount countNew(@Param("carrierId") Long carrierId);
    /*********************************韵车集成改版 ed*****************************/

}
