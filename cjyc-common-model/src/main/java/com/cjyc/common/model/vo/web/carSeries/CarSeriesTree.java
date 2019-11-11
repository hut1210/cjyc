package com.cjyc.common.model.vo.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarSeriesTree implements Serializable {

    private static final long serialVersionUID = -2664784551055534819L;
    @ApiModelProperty(value = "拼音首字母")
    private String pinInitial;

    private List<CarSeriesBrand> list;


}
