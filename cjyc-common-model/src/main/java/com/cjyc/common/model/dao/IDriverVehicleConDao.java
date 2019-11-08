package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.DriverVehicleCon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 司机与运力关系 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IDriverVehicleConDao extends BaseMapper<DriverVehicleCon> {

    /**
     * 根据运力id查看运行运力信息
     * @param vehicleId
     * @return
     */
    DriverVehicleCon getDriverVehicleCon(@Param("vehicleId") Long vehicleId);

    /**
     * 通过司机id获取司机与车辆绑定关系
     * @param driverId
     * @return
     */
    DriverVehicleCon getDriVehConByDriId(@Param("driverId") Long driverId);

    /**
     * 根据运力vehicleId删除司机与运力关系
     * @param vehicleId
     * @return
     */
    int delDriverVehConByVehId(@Param("vehicleId") Long vehicleId);

    /**
     * 根据车辆id删除与司机绑定关系
     * @param driverId
     * @return
     */
    int removeCon(@Param("driverId") Long driverId);

}
