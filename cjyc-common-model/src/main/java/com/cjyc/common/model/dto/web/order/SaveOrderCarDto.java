package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author JPG
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class SaveOrderCarDto implements Serializable {

    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private int isMove;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private int isNew;
    @ApiModelProperty(value = "估值/分")
    private int valuation;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "车辆应收保险费")
    private BigDecimal addInsuranceFee;
    @ApiModelProperty(value = "保额/万")
    private Integer addInsuranceAmount;

    @ApiModelProperty(value = "车辆应收提车费 单位：分")
    private BigDecimal pickFee;
    @ApiModelProperty(value = "车辆应收干线费 单位：分")
    private BigDecimal trunkFee;
    @ApiModelProperty(value = "车辆应收送车费 单位：分")
    private BigDecimal backFee;
    @ApiModelProperty(value = "物流券抵消金额")
    private BigDecimal couponOffsetFee;
}
