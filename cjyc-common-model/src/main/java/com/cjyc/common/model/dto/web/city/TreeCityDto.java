package com.cjyc.common.model.dto.web.city;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TreeCityDto {

    @ApiModelProperty("根节点级别：-1中国，0大区，1省，2市，3区")
    private Integer rootLevel = 0;

    @ApiModelProperty("最小叶子节点级别")
    private Integer minLeafLevel= 2;
}
