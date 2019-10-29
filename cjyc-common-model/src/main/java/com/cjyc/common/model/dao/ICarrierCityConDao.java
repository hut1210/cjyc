package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CarrierCityCon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 司机与区县绑定 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-21
 */
public interface ICarrierCityConDao extends BaseMapper<CarrierCityCon> {

    /**
     * 根据司机id查询该承运商业务范围
     * @param driverId
     * @return
     */
    CarrierCityCon getCarrierCodeByDriverId(@Param("driverId") Long driverId);

    /**
     * 通过承运商id获取承运商业务范围
     * @param carrierId
     * @return
     */
    CarrierCityCon getCarrierCodeByCarrierId(@Param("carrierId") Long carrierId);

    /**
     * 根据承运商id删除业务范围
     * @param carrierId
     * @return
     */
    int deleteByCarrierId(@Param("carrierId") Long carrierId);

    /**
     * 根据承运商id获取承运商业务范围
     * @param carrierId
     * @return
     */
    List<LinkedHashMap> getMapCodes(@Param("carrierId") Long carrierId);

}
