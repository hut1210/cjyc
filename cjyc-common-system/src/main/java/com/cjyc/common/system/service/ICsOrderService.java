package com.cjyc.common.system.service;

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
}
