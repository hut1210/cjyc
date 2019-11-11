package com.cjyc.common.model.vo.customer.CarSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InitialVo implements Serializable {
    private static final long serialVersionUID = 1707572265089482507L;

    @ApiModelProperty("拼音首字母")
    private String initial;

    @ApiModelProperty("品牌")
    private List<CarSeriesVo> carSeriesVos;
}