package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表(客户下单) 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
public interface IOrderService extends IService<Order> {

    List<Map<String, Object>> waitDispatchCarCountList();

    int totalWaitDispatchCarCount();
}
