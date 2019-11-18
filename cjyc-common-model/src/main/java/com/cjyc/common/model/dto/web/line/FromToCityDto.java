package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FromToCityDto {

    @ApiModelProperty(value = "出发地行政区编码")
    private String fromCityCode;

    @ApiModelProperty(value = "目的地行政区编码")
    private String toCityCode;
}
