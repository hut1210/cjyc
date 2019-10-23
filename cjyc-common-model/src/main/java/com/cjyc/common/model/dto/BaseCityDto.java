package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCityDto implements Serializable {

    @ApiModelProperty(value = "行政区编码（含大区、省、市、区）")
    private String code;

    @ApiModelProperty(value = "行政区名称")
    private String name;
}