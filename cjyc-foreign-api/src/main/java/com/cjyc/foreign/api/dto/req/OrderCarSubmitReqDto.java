package com.cjyc.foreign.api.dto.req;

import com.cjyc.common.model.constant.ArgsConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 订单车辆信息
 * @Author Liu Xing Xiang
 * @Date 2020/3/12 11:40
 **/
@Data
public class OrderCarSubmitReqDto implements Serializable {
    private static final long serialVersionUID = -3441938847460577267L;

    @NotBlank(message = "品牌不能为空")
    @ApiModelProperty(value = "品牌", required = true)
    private String brand;

    @NotBlank(message = "型号不能为空")
    @ApiModelProperty(value = "型号", required = true)
    private String model;

    @Pattern(regexp = "(^$)|(^\\S{1,20}$)", message = "车牌号格式不正确，请检查")
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @Pattern(regexp = "(^$)|(^[0-9a-zA-Z]{17}$)", message = "vin码格式不正确，请检查")
    @ApiModelProperty(value = "vin码")
    private String vin;

    @NotNull(message = "是否能动标识不能为空")
    @ApiModelProperty(value = "是否能动 0-否 1-是", required = true)
    private Integer isMove;

    @NotNull(message = "是否新车标识不能为空")
    @ApiModelProperty(value = "是否新车 0-否 1-是", required = true)
    private Integer isNew;

    @NotNull(message = "车值不能为空")
    @ApiModelProperty(value = "车值/万", required = true)
    private Integer valuation;

    @ApiModelProperty(value = "保险费/元",hidden = true)
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal addInsuranceFee;

    @ApiModelProperty(value = "车辆应收干线费",hidden = true)
    @Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
    @DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
    private BigDecimal trunkFee;
}
