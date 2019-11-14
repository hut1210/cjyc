package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderUpdateDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;

import java.util.Map;

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
     * 根据客户id查询订单数量
     * @param loginId
     * @return
     */
    ResultVo<Map<String, Object>> getOrderCount(Long loginId);

    /**
     * 根据条件查询各种状态下的订单明细
     * @param dto
     * @return
     */
    ResultVo<OrderCenterDetailVo> getDetail(OrderUpdateDto dto);

    /**
     * 确认收车
     * @param dto
     * @return
     */
    ResultVo confirmPickCar(OrderUpdateDto dto);

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

    /**
     * 保存订单
     * @author JPG
     * @since 2019/11/5 8:39
     * @param reqDto
     */
    ResultVo save(SaveOrderDto reqDto);

    /**
     * 订单提交-客户
     * @author JPG
     * @since 2019/11/5 10:16
     * @param reqDto
     */
    ResultVo submit(SaveOrderDto reqDto);

    /**
     * 订单提交-业务员
     * @author JPG
     * @since 2019/11/5 8:46
     * @param reqDto
     */
    ResultVo commit(CommitOrderDto reqDto);


}
