package com.cjyc.common.model.vo.customer.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class AreaTreeVo implements Serializable {
    private static final long serialVersionUID = -7252733019802907120L;
    @ApiModelProperty("城市父编码")
    private String parentCode;

    @ApiModelProperty("城市编码")
    private String code;

    @ApiModelProperty("城市名称")
    private String name;
}