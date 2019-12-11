package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.salesman.PageSalesDto;
import com.cjyc.common.model.dto.salesman.order.AppOrderQueryDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.AppOrderVo;

public interface IOrderService extends IService<Order> {

    /**
     * 草稿订单列表
     * @param dto
     * @return
     */
    ResultVo<PageVo<AppOrderVo>> findDraftOrder(PageSalesDto dto);

    /**
     * 接单和全部列表
     * @param dto
     * @return
     */
    ResultVo<PageVo<AppOrderVo>> findOrder(AppOrderQueryDto dto);
}
