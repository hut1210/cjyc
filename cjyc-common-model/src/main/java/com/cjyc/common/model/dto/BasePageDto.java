package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BasePageDto implements Serializable {

    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("每页条数")
    private Integer pageSize;
}