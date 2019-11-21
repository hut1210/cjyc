package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class HotCityVo implements Serializable {
    private static final long serialVersionUID = 5802438093336555686L;

    @ApiModelProperty("城市编码")
    private String code;
    @ApiModelProperty("城市名称")
    private String name;
    @ApiModelProperty("区县名称")
    private String areaName;
    @ApiModelProperty("区县co编码")
    private String areaCode;
}