package com.cjyc.common.model.vo.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TreeCityVo<T> implements Serializable {

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String parentCode;

    @ApiModelProperty("名称")
    private Integer level;

    @ApiModelProperty("下属城市")
    List<T> next;
}