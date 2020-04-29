package com.cjyc.common.model.dto.web.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaybillPriceCompareExportDto {
    @ApiModelProperty("开始时间")
    private Long startDate;
    @ApiModelProperty("结束时间")
    private Long endDate;
    @ApiModelProperty("查询类型: 0默认查询所有, 1提车运单，2干线运单，3送车运单")
    private int queryType;
}
