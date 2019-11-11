package com.cjyc.common.model.vo.customer.CarSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarVo implements Serializable {

    private static final long serialVersionUID = 5534408070705846757L;
    @ApiModelProperty("车系")
    private List<CarSeriesVo> carSeriesVos;
}