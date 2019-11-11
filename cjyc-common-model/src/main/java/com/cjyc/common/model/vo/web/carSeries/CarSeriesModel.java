package com.cjyc.common.model.vo.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CarSeriesModel implements Serializable {

    private static final long serialVersionUID = -5558982774715804097L;
    @ApiModelProperty(value = "型号")
    private String model;

}
