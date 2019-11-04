package com.cjyc.common.model.vo.customer.CarSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ModelVo implements Serializable {
    private static final long serialVersionUID = -3751164439707028337L;

    @ApiModelProperty("型号")
    private String model;
}