package com.cjyc.common.model.dto.web.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaybillPriceCompareExportDto {
    @ApiModelProperty("开始时间")
    private Long endDate;
    @ApiModelProperty("结束时间")
    private Long startDate;
    @ApiModelProperty("查询类型: 0默认查询所有")
    private int queryType;
}
