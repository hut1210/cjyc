package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.model.vo.web.order.ListOrderVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

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

    OrderVo findVoById(Long orderId);

    List<ListOrderVo> findListSelective(@Param("paramsDto") ListOrderDto paramsDto);

    /**
     * 根据条件查询订单信息
     * @param dto
     * @return
     */
    List<OrderCenterVo> selectPage(OrderQueryDto dto);

    int updateStateForLoad(@Param("orderState") int orderState, @Param("orderCarId") Long orderCarId);

    Map<String, Object> countForAllTab();

    int updateStateById(@Param("state") int state, @Param("id") Long id);
}
