package com.cjyc.web.api.controller;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.excel.ImportOrderChangePriceVo;
import com.cjyc.web.api.service.IExcelService;
import com.cjyc.web.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ResultVo<List<ImportOrderChangePriceVo>> resultVo = excelService.listOrderChangePriceSimple(reqDto);
        if (ResultEnum.SUCCESS.getCode() != resultVo.getCode()) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", resultVo.getMsg()), "导出异常.xls", response);
            return;
        }
        List<ImportOrderChangePriceVo> opcList = resultVo.getData();
        if (CollectionUtils.isEmpty(opcList)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"), "结果为空.xls", response);
            return;
        }
        try{
            ExcelUtil.exportExcel(opcList, "订单改价信息", "订单改价信息", ImportOrderChangePriceVo.class, System.currentTimeMillis()+"订单改价信息.xls", response);
        }catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "导出订单信息异常: " + e.getMessage()), "导出异常.xls", response);
        }
    }

}
