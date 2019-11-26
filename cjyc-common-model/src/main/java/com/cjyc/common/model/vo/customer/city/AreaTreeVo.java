package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class AreaTreeVo implements Serializable {
    private static final long serialVersionUID = -7252733019802907120L;
    @ApiModelProperty("区父级code")
    private String areaParentCode;

    @ApiModelProperty("区编码")
    private String areaCode;

    @ApiModelProperty("区名称")
    private String areaName;
}