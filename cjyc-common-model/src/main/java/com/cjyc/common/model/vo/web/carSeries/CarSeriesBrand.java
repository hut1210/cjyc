package com.cjyc.common.model.vo.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CarSeriesBrand {

    @ApiModelProperty(value = "品牌")
    private String brand;

    private String logoImg;

    private List<CarSeriesModel> list;
}
