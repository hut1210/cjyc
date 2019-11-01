package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.ListOrderCarVo;
import com.cjyc.common.model.vo.web.order.ListOrderVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.system
 * @date:2019/10/15
 */
public interface IOrderService extends IService<Order> {


    ResultVo commit(CommitOrderDto paramsDto);


    ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList();

    /**
     * 查询待调度车辆列表
     * @author JPG
     * @since 2019/10/15 20:03
     */
    ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(WaitDispatchListOrderCarDto paramsDto, List<Long> bizScopeStoreIds);

    /**
     * 按线路统计待调度车辆（统计列表）
     * @author JPG
     * @since 2019/10/16 10:04
     * @param bizScopeStoreIds
     */
    ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(LineWaitDispatchCountListOrderCarDto paramsDto, List<Long> bizScopeStoreIds);

    OrderVo getVoById(Long orderId);

    ResultVo allot(AllotOrderDto paramsDto);

    ResultVo<PageVo<ListOrderVo>> list(ListOrderDto paramsDto);

    ResultVo cancel(CancelOrderDto paramsDto);

    ResultVo obsolete(CancelOrderDto paramsDto);

    ResultVo changePrice(ChangePriceOrderDto paramsDto);

    ResultVo replenishInfo(ReplenishOrderDto paramsDto);

    ResultVo<PageVo<ListOrderCarVo>> carlist(ListOrderCarDto paramsDto);

    ResultVo check(CheckOrderDto reqDto);

    ResultVo save(SaveOrderDto reqDto);
}
