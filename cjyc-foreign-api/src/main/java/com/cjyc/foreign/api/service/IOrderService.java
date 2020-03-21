package com.cjyc.foreign.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dto.req.CancelOrderReqDto;
import com.cjyc.foreign.api.dto.req.OrderDetailReqDto;
import com.cjyc.foreign.api.dto.req.OrderSubmitReqDto;
import com.cjyc.foreign.api.dto.res.OrderDetailResDto;

/**
 * 订单
 */
public interface IOrderService extends IService<Order>{
    /**
     * 功能描述: 下单
     * @author liuxingxiang
     * @date 2020/3/11
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo<java.lang.String>
     */
    ResultVo<String> submitOrder(OrderSubmitReqDto reqDto);

    /**
     * 功能描述: 根据订单号查询订单详情
     * @author liuxingxiang
     * @date 2020/3/11
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.foreign.api.dto.res.OrderDetailResDto>
     */
    ResultVo<OrderDetailResDto> getOrderDetailByOrderNo(OrderDetailReqDto dto);

    /**
     * 功能描述: 取消订单
     * @author zhangcangman
     * @date 2020/3/20
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo<java.lang.String>
     */
    ResultVo<String> cancelOrder(CancelOrderReqDto reqDto);
}
