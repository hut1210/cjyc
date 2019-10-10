package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasePageVo implements Serializable {

    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo;
}