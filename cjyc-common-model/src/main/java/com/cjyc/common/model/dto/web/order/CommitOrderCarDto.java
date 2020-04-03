package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.constant.ArgsConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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
    @Pattern(regexp = "(^$)|(^\\S{1,20}$)", message = "车牌号格式不正确，请检查")
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @Pattern(regexp = "(^$)|(^[0-9a-zA-Z]{17}$)", message = "vin码格式不正确")
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;
    @ApiModelProperty(value = "估值/分",required = true)
    private Integer valuation;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "车辆应收保险费")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal addInsuranceFee;
    @ApiModelProperty(value = "保额/万")
    private Integer addInsuranceAmount;

    @ApiModelProperty(value = "车辆应收提车费")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal pickFee;
    @ApiModelProperty(value = "车辆应收干线费")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal trunkFee;
    @ApiModelProperty(value = "车辆应收送车费")
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal backFee;
}
