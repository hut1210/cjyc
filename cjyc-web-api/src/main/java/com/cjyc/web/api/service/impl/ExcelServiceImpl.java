package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import com.cjyc.web.api.service.IExcelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelServiceImpl implements IExcelService {
    @Override
    public ResultVo<List<OrderVo>> listOrderChangePriceSimple(ListOrderDto reqDto) {
        return null;
    }
}
