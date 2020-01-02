package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SimpleCommitOrderDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;

public interface IOrderService extends IService<Order> {

    /**
     * 下单/接单/全部列表
     * @param dto
     * @return
     */
    ResultVo<PageVo<SalesOrderVo>> findOrder(SalesOrderQueryDto dto);

    /**
     * 订单详情
     * @param dto
     * @return
     */
    ResultVo<SalesOrderDetailVo> findOrderDetail(SalesOrderDetailDto dto);
}
