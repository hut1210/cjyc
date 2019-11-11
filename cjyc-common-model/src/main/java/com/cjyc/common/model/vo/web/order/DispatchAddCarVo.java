package com.cjyc.common.model.vo.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DispatchAddCarVo {

    @ApiModelProperty("车辆列表")
    private String guideLine;
    @ApiModelProperty("车辆列表")
    private List<CarFromToGetVo> list;
}
