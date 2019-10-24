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
     * 根据承运商id查询车数和总收入
     * @param carrierId
     * @return
     */
    Map getCarIncomeByCarrId(@Param("carrierId") Long carrierId);

}
