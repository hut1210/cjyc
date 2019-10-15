package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.OrderCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.OrderCarCenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单明细（车辆表） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IOrderCarDao extends BaseMapper<OrderCar> {

    /**
     * 根据订单编号查询车辆信息
     * @param orderNo
     * @return
     */
    List<OrderCarCenterVo> getOrderCarByNo(@Param("orderNo") String orderNo);

    /**
     * 根据车辆id获取指定车辆信息
     * @param orderCarId
     * @return
     */
    OrderCarCenterVo  getOrderCarInfoById(@Param("orderCarId") Long orderCarId);

}
