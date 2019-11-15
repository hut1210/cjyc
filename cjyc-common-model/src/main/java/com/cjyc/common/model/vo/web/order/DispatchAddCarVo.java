package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DispatchAddCarVo {

    @ApiModelProperty("车辆列表")
    private String guideLine;
    @ApiModelProperty("车辆列表")
    private List<WaybillCarVo> list;
}
