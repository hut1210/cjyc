package com.cjyc.common.model.vo.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CarSeriesTree {


    @ApiModelProperty(value = "拼音首字母")
    private String pinInitial;

    private List<CarSeriesBrand> list;


}
