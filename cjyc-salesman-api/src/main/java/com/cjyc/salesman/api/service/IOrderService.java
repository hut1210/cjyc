package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.order.WaitDispatchListOrderCarDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;

/**
 * <p>
 * 订单表(客户下单) 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
public interface IOrderService extends IService<Order> {

    ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(WaitDispatchListOrderCarDto reqDto, Object o);
}
