package com.cjyc.common.model.vo.customer.CarSeries;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarVo implements Serializable {
    private static final long serialVersionUID = 2363009297494520191L;

    @ApiModelProperty("首字母")
    private List<InitialVo> initial;

}