package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Order;
import com.cjyc.web.api.dto.OrderDto;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.service
 * @date:2019/10/15
 */
public interface IOrderService extends IService<Order> {

    boolean commitOrder(OrderDto orderDto);


    List<Map<String, Object>> waitDispatchCarCountList();

    Map<String, Object> totalWaitDispatchCarCount();
}
