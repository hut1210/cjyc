package com.cjyc.common.model.vo.web.postal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class AreaVo implements Serializable {
    private static final long serialVersionUID = -4413737953959711624L;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "邮政编码")
    private String postalCode;

    @ApiModelProperty(value = "电话区号")
    private String areaCode;
}