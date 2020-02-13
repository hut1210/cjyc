package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.*;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.model.vo.customer.order.OutterOrderCarLogVo;

import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.system
 * @date:2019/10/8
 */
public interface IOrderService extends IService<Order> {


    /**
     * 根据条件查询订单信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto);

    /**
     * 根据条件查询各种状态下的订单明细
     * @param dto
     * @return
     */
    ResultVo<OrderCenterDetailVo> getDetail(OrderDetailDto dto);

    /**
     * 确认收车
     * @param dto
     * @return
     */

    /**
     * 查询为开发票订单列表
     * @param dto
     * @return
     */
    ResultVo getUnInvoicePage(InvoiceApplyQueryDto dto);

    /**
     * 查询发票申请信息订单明细
     * @param dto
     * @return
     */
    ResultVo getInvoiceApplyOrderPage(InvoiceApplyQueryDto dto);

    ResultVo simpleSubmit(SimpleSaveOrderDto reqDto);

    ResultVo<List<OutterOrderCarLogVo>> ListOrderCarLog(String orderCarNo);
}
