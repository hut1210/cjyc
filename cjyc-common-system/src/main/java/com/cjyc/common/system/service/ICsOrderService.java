package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;

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
    ResultVo obsolete(CancelOrderDto paramsDto);

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

    int changeOrderCarCarryType(ChangeCarryTypeDto reqDto);
}
