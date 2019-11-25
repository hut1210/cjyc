package com.cjyc.common.model.vo.web.coupon;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CouponVo implements Serializable {

    private static final long serialVersionUID = 1331863052699915068L;
    @ApiModelProperty(value = "优惠券主键id(couponId)")
    private Long couponId;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty(value = "满额价")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal fullAmount;

    @ApiModelProperty(value = "减额值")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal cutAmount;

    @ApiModelProperty(value = "折扣")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
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
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long startPeriodDate;

    @ApiModelProperty(value = "优惠券有效结束时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long endPeriodDate;

    @ApiModelProperty(value = "优惠券审核状态： 0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty("创建人")
    private String createName;
}