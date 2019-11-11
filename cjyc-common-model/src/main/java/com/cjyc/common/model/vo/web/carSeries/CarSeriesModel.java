package com.cjyc.common.model.vo.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CarSeriesModel {

    @ApiModelProperty(value = "型号")
    private String model;

}
