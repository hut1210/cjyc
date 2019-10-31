package com.cjyc.common.model.vo.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CouponVo implements Serializable {

    @ApiModelProperty(value = "优惠券主键id")
    private Long id;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty(value = "满额价")
    private BigDecimal fullAmount;

    @ApiModelProperty(value = "减额值")
    private String cutAmount;

    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

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
    private String startPeriodDate;

    @ApiModelProperty(value = "优惠券有效结束时间")
    private String endPeriodDate;

    @ApiModelProperty(value = "优惠券审核状态： 0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}