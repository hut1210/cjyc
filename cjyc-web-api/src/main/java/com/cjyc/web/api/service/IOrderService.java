package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.BaseWebDto;
import com.cjyc.common.model.dto.web.dispatch.LineWaitCountDto;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.order.*;

import java.util.List;
import java.util.Map;

/**
 * @author JPG
 */
public interface IOrderService extends IService<Order> {


    @Deprecated
    ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList();

    /**
     * 查询待调度车辆列表
     * @author JPG
     * @since 2019/10/15 20:03
     */
    ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(WaitDispatchListOrderCarDto paramsDto);

    /**
     * 按线路统计待调度车辆（统计列表）
     * @author JPG
     * @since 2019/10/16 10:04
     * @param bizScopeStoreIds
     */
    @Deprecated
    ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(LineWaitDispatchCountDto paramsDto, List<Long> bizScopeStoreIds);

    OrderVo getVoById(Long orderId);

    ResultVo<PageVo<ListOrderVo>> list(ListOrderDto paramsDto);

    ResultVo<List<ListOrderVo>> listAll(ListOrderDto dto);

    ResultVo<PageVo<ListOrderCarVo>> carlist(ListOrderCarDto paramsDto);

    List<ListOrderCarVo> carListAll(ListOrderCarDto dto);

    ResultVo<DispatchAddCarVo> getWaybillCarEndpoint(ComputeCarEndpointDto reqDto);

    List<ListOrderChangeLogVo> getChangeLogVoById(ListOrderChangeLogDto reqDto);

    List<TransportInfoOrderCarVo> getTransportInfoVoById(Long orderId);

    void importCustomerOrder(List<ImportCustomerOrderDto> orderList,
                             List<ImportCustomerOrderCarDto> carList, Admin admin);

    void importKeyCustomerOrder(List<ImportKeyCustomerOrderDto> orderList,
                                List<ImportKeyCustomerOrderCarDto> carList, Admin admin);

    void importPatCustomerOrder(List<ImportPatCustomerOrderDto> orderList,
                                List<ImportPatCustomerOrderCarDto> carList, Admin admin);

    OrderCarVo getCarVoById(Long orderCarId);

    ResultVo<PageVo<ListOrderVo>> listForHhr(ListOrderDto reqDto);

    ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchTrunkCarList(WaitDispatchTrunkDto reqDto);

    ResultVo<ListVo<Map<String, Object>>> waitDispatchTrunkCarCountList(BaseWebDto reqDto);

    ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchTrunkCarCountList(LineWaitDispatchCountDto reqDto);

    ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountListV2(BaseWebDto reqDto);

    ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountListV2(LineWaitCountDto reqDto);
}
