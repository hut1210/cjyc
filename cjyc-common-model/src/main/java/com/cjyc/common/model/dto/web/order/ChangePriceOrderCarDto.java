package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.constant.ArgsConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class ChangePriceOrderCarDto {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "车辆应收提车费")
    @DecimalMax(value = ArgsConstant.DECIMAL_MAX)
    @DecimalMin(ArgsConstant.DECIMAL_ZERO)
    private BigDecimal pickFee;

    @ApiModelProperty(value = "车辆应收干线费")
    @DecimalMax(value = ArgsConstant.DECIMAL_MAX)
    @DecimalMin(ArgsConstant.DECIMAL_ZERO)
    private BigDecimal trunkFee;

    @ApiModelProperty(value = "车辆应收送车费")
    @DecimalMax(value = ArgsConstant.DECIMAL_MAX)
    @DecimalMin(ArgsConstant.DECIMAL_ZERO)
    private BigDecimal backFee;

    @ApiModelProperty(value = "车辆应收保险费")
    @DecimalMax(value = ArgsConstant.DECIMAL_MAX)
    @DecimalMin(ArgsConstant.DECIMAL_ZERO)
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "保额/万")
    private Integer addInsuranceAmount;
}
