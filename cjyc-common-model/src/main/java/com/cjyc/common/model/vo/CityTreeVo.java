package com.cjyc.common.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityTreeVo implements Serializable {

    private static final long serialVersionUID = -3585390982612085010L;
    @ApiModelProperty("城市父编码")
    private String parentCode;

    @ApiModelProperty("城市编码")
    private String code;

    @ApiModelProperty("城市名称")
    private String name;

    @ApiModelProperty("子节点")
    private List<CityTreeVo> cityVos;

}