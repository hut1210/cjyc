package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 入参
 * @author JPG
 */
@Data
@ApiModel
public class SortNodeDto {
    @NotNull
    @ApiModelProperty(value = "出发地级城市名",required = true)
    private String startCityName;
    @NotNull
    @ApiModelProperty(value = "目的地级城市名",required = true)
    private String endCityName;
}
