package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.salesman.PageSalesDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.model.vo.web.order.ListOrderVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    int updateStateForLoad(@Param("orderState") int orderState, @Param("orderIdSet") Set<Long> orderIdSet);

    Map<String, Object> countForAllTab(@Param("paramsDto") ListOrderDto paramsDto);

    int updateStateById(@Param("state") int state, @Param("id") Long id);

    Order findByCarId(Long orderCarId);

    int updateForReceipt(@Param("id") Long orderId, @Param("currentTimeMillis") long currentTimeMillis);

    int countUnReceipt(Long orderId);

    List<Order> findListByCarIds(@Param("list") List<Long> orderCarIds);

    List<Order> findListByCarNos(@Param("list")List<String> orderCarNos);

    /**
     * 查询业务员端接单和全部列表
     * @param dto
     * @return
     */
    List<SalesOrderVo> findOrder(SalesOrderQueryDto dto);
}
