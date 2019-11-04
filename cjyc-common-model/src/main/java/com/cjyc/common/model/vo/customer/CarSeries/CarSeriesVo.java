package com.cjyc.common.model.vo.customer.CarSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarSeriesVo implements Serializable {
    private static final long serialVersionUID = 228092806035514120L;

    @ApiModelProperty("品牌logo")
    private String logoImg;

    @ApiModelProperty("品牌")
    private String brand;

    private List<ModelVo> modelVos;
}