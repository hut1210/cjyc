package com.cjyc.common.model.dto.web.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderChangePriceExportDto {
    @ApiModelProperty("开始时间")
    private Long startDate;
    @ApiModelProperty("结束时间")
    private Long endDate;

}
