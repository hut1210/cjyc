package com.cjyc.common.model.entity.defined;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全级别城市对象
 * @author JPG
 */
@Data
public class FullCity {
    @ApiModelProperty("中国")
    private String china;
    @ApiModelProperty("中国编码")
    private String chinaCode;
    @ApiModelProperty("大区")
    private String region;
    @ApiModelProperty("大区编码")
    private String regionCode;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("省编码")
    private String provinceCode;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("市编码")
    private String cityCode;
    @ApiModelProperty("区")
    private String area;
    @ApiModelProperty("区编码")
    private String areaCode;
}
