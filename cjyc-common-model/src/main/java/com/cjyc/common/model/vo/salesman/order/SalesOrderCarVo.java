package com.cjyc.common.model.vo.salesman.order;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SalesOrderCarVo implements Serializable {
    private static final long serialVersionUID = -8119244731311513499L;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "品牌logo")
    private String logoImg;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;
    @ApiModelProperty(value = "估值/万")
    private Integer valuation;
    @ApiModelProperty(value = "追加保额/万")
    private Integer addInsuranceAmount;
    @ApiModelProperty(value = "保费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal addInsuranceFee;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "提车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal pickFee;
    @ApiModelProperty(value = "送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal backFee;
    @ApiModelProperty(value = "物流费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal trunkFee;
    @ApiModelProperty(value = "单车总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;
}