package com.cjyc.common.model.vo.web.city;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class CityTreeVo implements Serializable {

    @ApiModelProperty("城市父编码")
    private String parentCode;

    @ApiModelProperty("城市编码")
    private String code;

    @ApiModelProperty("城市名称")
    private String name;

    @ApiModelProperty("城市级别")
    private Integer level;

    @ApiModelProperty("子节点")
    private List<CityTreeVo> cityVos;

}