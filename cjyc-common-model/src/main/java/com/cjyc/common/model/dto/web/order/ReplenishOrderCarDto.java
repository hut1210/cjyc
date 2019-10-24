package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ReplenishOrderCarDto {

    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "品牌",required = true)
    private String brand;
    @ApiModelProperty(value = "型号",required = true)
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
}
