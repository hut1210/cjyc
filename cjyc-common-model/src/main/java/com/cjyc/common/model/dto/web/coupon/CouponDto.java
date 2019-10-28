package com.cjyc.common.model.dto.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CouponDto implements Serializable {

    public interface SaveCouponDto {
    }

    public interface UpaCouponDto{

    }

    @ApiModelProperty(value = "优惠券主键id")
    @NotNull(groups = {CouponDto.UpaCouponDto.class},message = "优惠券主键id不能为空")
    private Long id;

    @ApiModelProperty(value = "优惠券名称")
    @NotBlank(groups = {CouponDto.SaveCouponDto.class},message = "优惠券名称不能为空")
    @NotBlank(groups = {CouponDto.UpaCouponDto.class},message = "优惠券名称不能为空")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型 0：满减  1：直减  2：折扣")
    @NotBlank(groups = {CouponDto.SaveCouponDto.class},message = "优惠券类型不能为空")
    @NotBlank(groups = {CouponDto.UpaCouponDto.class},message = "优惠券类型不能为空")
    private Integer couponType;

    @ApiModelProperty(value = "满额价")
    private BigDecimal fullAmount;

    @ApiModelProperty(value = "减额值")
    private BigDecimal cutAmount;

    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "发放张数")
    @NotBlank(groups = {CouponDto.SaveCouponDto.class},message = "发放张数不能为空")
    @NotBlank(groups = {CouponDto.UpaCouponDto.class},message = "发放张数不能为空")
    private Integer grantNum;

    @ApiModelProperty(value = "是否永久  false：否  true：是")
    @NotNull(groups = {CouponDto.SaveCouponDto.class},message = "是否永久不能为空")
    @NotNull(groups = {CouponDto.UpaCouponDto.class},message = "是否永久不能为空")
    private Boolean isForever;

    @ApiModelProperty(value = "优惠券有效起始时间")
    private String startPeriodDate;

    @ApiModelProperty(value = "优惠券有效结束时间")
    private String endPeriodDate;
}