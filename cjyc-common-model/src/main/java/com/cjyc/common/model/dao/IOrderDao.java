package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.customer.OrderConditionDto;
import com.cjyc.common.model.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.common.model.vo.customer.OrderDetailVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 根据订单编号查看订单相关信息
     * @param orderNo
     * @return
     */
    OrderDetailVo getOrderDetailByNo(@Param("orderNo") String orderNo);

    /**
     * 根据条件进行筛选待确认订单
     * @param dto
     * @return
     */
    List<OrderCenterVo> getConFirmOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件进行筛选运输中订单
     * @param dto
     * @return
     */
    List<OrderCenterVo> getTransOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件进行筛选已支付订单
     * @param dto
     * @return
     */
    List<OrderCenterVo> getPaidOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件进行筛选全部订单
     * @param dto
     * @return
     */
    List<OrderCenterVo> getAllOrdsByTerm(OrderConditionDto dto);
}
