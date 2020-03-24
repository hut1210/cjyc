package com.cjyc.common.model.entity.defined;

import com.cjyc.common.model.entity.WaybillCar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FullWaybillCar extends WaybillCar {

    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("出发地业务中心地址")
    private String startStoreFullAddress;
    @ApiModelProperty("目的地业务中心地址")
    private String endStoreFullAddress;
}
