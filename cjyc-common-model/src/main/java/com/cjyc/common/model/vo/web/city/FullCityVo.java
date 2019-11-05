package com.cjyc.common.model.vo.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FullCityVo implements Serializable {
    private static final long serialVersionUID = -6447405441823853694L;
    @ApiModelProperty("省名称")
    private String province;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("城市名称")
    private String city;

    @ApiModelProperty("城市编码")
    private String cityCode;

    @ApiModelProperty("区/县名称")
    private String area;

    @ApiModelProperty("区/县编码")
    private String areaCode;

    @ApiModelProperty("大区名称")
    private String regionCode;

    @ApiModelProperty("大区编码")
    private String region;
}
