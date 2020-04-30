package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.excel.DriverLoginCountExportDto;
import com.cjyc.common.model.dto.web.excel.OrderChangePriceExportDto;
import com.cjyc.common.model.dto.web.excel.WaybillPriceCompareExportDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.excel.DriverLoginCountExportVo;
import com.cjyc.common.model.vo.web.excel.ImportOrderChangePriceVo;
import com.cjyc.common.model.vo.web.excel.WaybillPriceCompareExportVo;

import java.util.List;

public interface IExcelService {

    ResultVo<List<ImportOrderChangePriceVo>> listOrderChangePriceSimple(OrderChangePriceExportDto reqDto);

    ResultVo<List<WaybillPriceCompareExportVo>> listWaybillPriceCompare(WaybillPriceCompareExportDto reqDto);

    ResultVo<List<DriverLoginCountExportVo>> ListDriverLoginCount(DriverLoginCountExportDto reqDto);
}
