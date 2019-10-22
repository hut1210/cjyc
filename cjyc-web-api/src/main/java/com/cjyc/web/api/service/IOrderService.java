package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.web.api.dto.OrderCommitDto;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.service
 * @date:2019/10/15
 */
public interface IOrderService extends IService<Order> {


    ResultVo save(OrderCommitDto orderCommitDto);

    ResultVo update(OrderCommitDto orderCommitDto);


    ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList();

    /**
     * 查询待调度车辆列表
     * @author JPG
     * @since 2019/10/15 20:03
     * @param reqDto
     */
    ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(OrderCarWaitDispatchListDto reqDto, List<Long> bizScopeStoreIds);

    /**
     * 按线路统计待调度车辆（统计列表）
     * @author JPG
     * @since 2019/10/16 10:04
     * @param reqDto
     * @param bizScopeStoreIds
     */
    ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(OrderCarLineWaitDispatchCountListDto reqDto, List<Long> bizScopeStoreIds);

}
