package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.dto.web.order.ListOrderDto;
import com.cjyc.web.api.service.IExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
        //ResultVo<List<FullOrder>> resultVo = excelService.listOrderChangePriceSimple(reqDto);
        /*if (resultVo.getCode() != ResultEnum.SUCCESS.getCode()) {
            return;
        }*/
    }
    @ApiOperation(value = "导出订单改价记录")
    @GetMapping(value = "/order/change/price/full/export")
    public void exportOrderChangePriceFull(ListOrderDto reqDto, HttpServletResponse response){

    }

}
