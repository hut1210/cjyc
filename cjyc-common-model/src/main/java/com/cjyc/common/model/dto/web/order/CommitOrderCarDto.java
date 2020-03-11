package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author JPG
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CommitOrderCarDto implements Serializable {

    @ApiModelProperty(value = "品牌",required = true)
    private String brand;
    @ApiModelProperty(value = "型号",required = true)
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @Pattern(regexp = "(^$)|(^\\S{1,20}$)", message = "vin码格式不正确")
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;
    @Pattern(regexp = "(^$)|(^[1-9]\\d*$)", message = "估值只支持正整数")
    @ApiModelProperty(value = "估值/分",required = true)
    private Integer valuation;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "车辆应收保险费")
    private BigDecimal addInsuranceFee;
    @ApiModelProperty(value = "保额/万")
    @Pattern(regexp = "(^$)|(^[1-9]\\d*$)", message = "保额只支持正整数")
    private Integer addInsuranceAmount;

    @ApiModelProperty(value = "车辆应收提车费 单位：分")
    private BigDecimal pickFee;
    @ApiModelProperty(value = "车辆应收干线费 单位：分")
    private BigDecimal trunkFee;
    @ApiModelProperty(value = "车辆应收送车费 单位：分")
    private BigDecimal backFee;
}
