package com.cjyc.common.model.annotations;

import com.cjyc.common.model.constant.ArgsConstant;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Digits(integer = ArgsConstant.INT_MAX, fraction = ArgsConstant.FRACTION_MAX, message = "金额整数最多8位，小数最多2位")
@DecimalMin(value = ArgsConstant.DECIMAL_ZERO, message = "金额不能小于0")
public @interface IsFee {
}
