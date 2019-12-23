package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaybillCarVo extends WaybillCar {

    @ApiModelProperty(value = "状态")
    private String outterState;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "价卡运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal lineFreightFee;

    @ApiModelProperty(value = "始发地是否可以编辑")
    private Boolean startFixedFlag;
    @ApiModelProperty(value = "目的地是否可以编辑")
    private Boolean endFixedFlag;
    @ApiModelProperty(value = "目的地是否可以编辑")
    private Boolean hasLine;

}
