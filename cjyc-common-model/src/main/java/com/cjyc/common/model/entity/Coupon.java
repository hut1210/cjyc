package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券发放和使用明细表
 * </p>
 *
 * @author JPG
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_coupon")
@ApiModel(value="Coupon对象", description="优惠券发放和使用明细表")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "优惠券编码")
    private String couponNo;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型 0：满减  3：直减  5：折扣")
    private Integer couponType;

    @ApiModelProperty(value = "满额价")
    private BigDecimal fullAmount;

    @ApiModelProperty(value = "减额值")
    private BigDecimal cutAmount;

    @ApiModelProperty(value = "折扣")
    private String discount;

    @ApiModelProperty(value = "发放张数")
    private Integer grantNum;

    @ApiModelProperty(value = "领取张数")
    private Integer receiveNum;

    @ApiModelProperty(value = "消耗张数")
    private Integer consumeNum;

    @ApiModelProperty(value = "到期作废张数")
    private Integer expireDeleNum;

    @ApiModelProperty(value = "剩余可用张数")
    private Integer surplusAvailNum;

    @ApiModelProperty(value = "是否永久  0：否  1：是")
    private Integer isForever;

    @ApiModelProperty(value = "优惠券有效起始时间")
    private Long startPeriodDate;

    @ApiModelProperty(value = "优惠券有效结束时间")
    private Long endPeriodDate;

    @ApiModelProperty(value = "优惠券审核状态 0：待审核  3：审核通过  5：审核不通过  7：已作废")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;


}
