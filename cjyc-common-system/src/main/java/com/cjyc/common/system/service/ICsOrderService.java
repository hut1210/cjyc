package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.order.CheckOrderDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.vo.ResultVo;

/**
 * 订单公用业务
 * @author JPG
 */
public interface ICsOrderService {

    ResultVo save(SaveOrderDto reqDto, OrderStateEnum state);

    ResultVo commit(CommitOrderDto reqDto);
    /**
     * 审核订单
     * @author JPG
     * @since 2019/11/5 15:03
     * @param reqDto
     */
    ResultVo check(CheckOrderDto reqDto);
}
