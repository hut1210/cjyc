package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BasePageDto implements Serializable {

    @ApiModelProperty(value = "当前页",required = true)
    private Integer currentPage;

    @ApiModelProperty(value = "每页条数",required = true)
    private Integer pageSize;
}