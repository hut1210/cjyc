package com.cjyc.common.model.vo.web.carSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarSeriesBrand implements Serializable {

    private static final long serialVersionUID = 1458528473730873013L;
    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "logo")
    private String logoImg;

    private List<CarSeriesModel> list;
}
