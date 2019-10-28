package com.cjyc.common.model.vo.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ProvinceCityVo implements Serializable {

    @ApiModelProperty("省/直辖市名称")
    private String provinceName;

    @ApiModelProperty("省/直辖市编码")
    private String provinceCode;

    @ApiModelProperty("城市名称")
    private String cityName;

    @ApiModelProperty("城市编码")
    private String cityCode;

}