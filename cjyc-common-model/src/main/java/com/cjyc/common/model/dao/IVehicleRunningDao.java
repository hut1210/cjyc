package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.VehicleRunning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 运行运力池表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IVehicleRunningDao extends BaseMapper<VehicleRunning> {

    /**
     * 根据运行运力池查看运力信息
     * @param vehicleId
     * @return
     */
    VehicleRunning getVehiRunByVehiId(@Param("vehicleId") Long vehicleId);

    /**
     * 根据司机id查询运力信息
     * @param driverId
     * @return
     */
    VehicleRunning getVehiRunByDriverId(@Param("driverId") Long driverId);

    /**
     * 根据运力driverId删除运力信息
     * @param driverId,vehicleId
     * @return
     */
    int removeRun(@Param("driverId") Long driverId,@Param("vehicleId") Long vehicleId);

    VehicleRunning findByDriverId(Long driverId);

    int updateOccupiedNumForLoad(@Param("driverId") Long driverId, @Param("count") int count);

    int updateOccupiedNumForUnload(@Param("driverId") Long driverId, @Param("count") int count);

    int updateOccupiedNum(@Param("vehicleRunningId") Long vehicleRunningId);
}
