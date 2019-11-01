package com.cjyc.common.model.dto.web.line;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ListLineDto {

    @NotNull
    @ApiModelProperty(value = "出发地级城市编码",required = true)
    private String startCityCode;
    @NotNull
    @ApiModelProperty(value = "目的地级城市编码",required = true)
    private String endCityCode;

}
