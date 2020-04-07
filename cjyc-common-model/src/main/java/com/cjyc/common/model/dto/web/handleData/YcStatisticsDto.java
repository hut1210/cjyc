package com.cjyc.common.model.dto.web.handleData;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class YcStatisticsDto implements Serializable {
    private static final long serialVersionUID = 7522585798757497111L;

    @ApiModelProperty(value = "韵车数量id")
    private Long id;

    @ApiModelProperty(value = "每日运量(台数)")
    private Integer dayCount;

    @ApiModelProperty(value = "日期")
    private String dayTime;
}