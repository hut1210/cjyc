package com.cjyc.web.api.controller;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.common.model.entity.defined.FullOrder;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.ListOrderVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import com.cjyc.web.api.service.IExcelService;
import com.cjyc.web.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "导入导出")
@RestController
@CrossOrigin
@RequestMapping(value = "/excel")
public class ExcelController {

    @Resource
    private IExcelService excelService;


    @ApiOperation(value = "导出订单改价记录")
    @GetMapping(value = "/order/change/price/simple/export")
    public void exportOrderChangePriceSimple(ChangePriceExportDto reqDto, HttpServletResponse response){
        ResultVo<List<FullOrder>> resultVo = excelService.listOrderChangePriceSimple(reqDto);
        if (resultVo.getCode() != ResultEnum.SUCCESS.getCode()) {
            return;
        }
    }
    @ApiOperation(value = "导出订单改价记录")
    @GetMapping(value = "/order/change/price/full/export")
    public void exportOrderChangePriceFull(ListOrderDto reqDto, HttpServletResponse response){

    }

}
