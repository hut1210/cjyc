package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 订单公用业务
 * @author JPG
 */
public interface ICsOrderService {

    ResultVo save(SaveOrderDto reqDto);

    ResultVo commit(CommitOrderDto reqDto);
    /**
     * 审核订单
     * @author JPG03
     * @since 2019/11/5 15:
     * @param reqDto
     */
    ResultVo check(CheckOrderDto reqDto);

    Order fillOrderInputStore(Order order);

    Set<String> validateOrderCarPlateNoInfo(Set<String> plateNoSet, String plateNo);

    Set<String> validateOrderCarVinInfo(Set<String> vinSet, String vin);

    Order fillOrderStoreInfo(Order order, boolean isForceUpdate);

    /**
     * 提交并审核
     * @param reqDto
     * @return
     */
    ResultVo commitAndCheck(CommitOrderDto reqDto);

    /**
     * 分配订单
     * @author JPG
     * @since 2019/11/5 16:05
     * @param paramsDto
     */
    ResultVo allot(AllotOrderDto paramsDto);

    /**
     * 驳回订单
     * @author JPG
     * @since 2019/11/5 16:07
     * @param paramsDto
     */
    ResultVo reject(RejectOrderDto paramsDto);

    /**
     * 订单取消
     * @author JPG
     * @since 2019/11/5 16:52
     * @param paramsDto
     */
    ResultVo cancel(CancelOrderDto paramsDto);

    /**
     * 订单作废
     * @author JPG
     * @since 2019/11/5 16:51
     * @param paramsDto
     */
    ResultVo obsolete(ObsoleteOrderDto paramsDto);

    /**
     * 订单改价
     * @author JPG
     * @since 2019/11/5 16:51
     * @param paramsDto
     */
    ResultVo changePrice(ChangePriceOrderDto paramsDto);

    /**
     * 完善订单信息
     * @author JPG
     * @since 2019/11/5 16:51
     * @param paramsDto
     */
    ResultVo replenishInfo(ReplenishOrderDto paramsDto);

    /**
     * 计算车辆起始目的地
     * @author JPG
     * @since 2019/12/11 13:43
     * @param paramsDto
     */
    ResultVo<DispatchAddCarVo> computerCarEndpoint(ComputeCarEndpointDto paramsDto);

    ResultVo simpleCommitAndCheck(CheckOrderDto reqDto);

    ResultVo changeOrderCarCarryType(ChangeCarryTypeDto reqDto);

    /**
     * 验证是否到达业务中心或者城市范围
     * @author JPG
     * @since 2020/4/15 11:12
     * @param endStoreId 目的地业务中心ID
     * @param endAreaCode 目的地区编码
     * @param endCityCode 目的地城市编码
     * @param orderEndStoreId 订单目的地业务中心ID
     * @param orderEndCityCode 订单目的地城市编码
     */
    boolean validateIsArriveStoreOrCityRange(Long endStoreId, String endAreaCode, String endCityCode, Long orderEndStoreId, String orderEndCityCode);

    BigDecimal getCarWlFee(OrderCar orderCar);

    /**
     * 验证是否重复车牌号，重复返回false
     * @author JPG
     * @since 2020/4/13 9:38
     * @param orderNo 订单ID
     * @param orderCarId 车辆ID
     * @param plateNo 车牌号
     */
    boolean validateIsNotRepeatPlateNo(String orderNo, Long orderCarId, String plateNo);
    /**
     * 验证是否重复Vin，重复返回false
     * @author JPG
     * @since 2020/4/13 9:38
     * @param orderNo 订单ID
     * @param orderCarId 车辆ID
     * @param vin 车架号
     */
    boolean validateIsNotRepeatVin(String orderNo, Long orderCarId, String vin);

    /**
     * 均摊车辆费用
     * @author JPG
     * @since 2020/4/27 13:05
     * @param totalFee
     * @param ocList
     */
    List<OrderCar> shareTotalFee(BigDecimal totalFee, List<OrderCar> ocList);
}
