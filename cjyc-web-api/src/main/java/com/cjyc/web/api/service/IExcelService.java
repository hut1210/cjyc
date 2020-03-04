package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.excel.ChangePriceExportDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.excel.ImportOrderChangePriceVo;

import java.util.List;

public interface IExcelService {

    ResultVo<List<ImportOrderChangePriceVo>> listOrderChangePriceSimple(ChangePriceExportDto reqDto);
}
