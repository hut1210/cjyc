package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderVo;

import java.util.List;

public interface IExcelService {

    ResultVo<List<OrderVo>> listOrderChangePriceSimple(ListOrderDto reqDto);
}
