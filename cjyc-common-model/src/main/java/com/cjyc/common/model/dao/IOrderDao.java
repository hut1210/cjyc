package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表(客户下单) Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface IOrderDao extends BaseMapper<Order> {

    int addOrder(Order order);

    /**
     * 获取所有待确认订单
     * @return
     */
    List<OrderCenterVo> getWaitConFirmOrders();

    /**
     * 获取所有运输中订单
     * @return
     */
    List<OrderCenterVo> getTransOrders();

    /**
     * 获取所有已支付订单
     * @return
     */
    List<OrderCenterVo> getPaidOrders();

    /**
     * 获取全部订单
     * @return
     */
    List<OrderCenterVo> getAllOrders();


}
