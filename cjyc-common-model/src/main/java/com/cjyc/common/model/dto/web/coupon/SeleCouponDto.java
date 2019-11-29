package com.cjyc.common.model.dto.web.coupon;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SeleCouponDto extends BasePageDto {

    private static final long serialVersionUID = 1549905775177738505L;
    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty(value = "优惠券审核状态 0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty(value = "创建开始时间")
    private Long startTime;

    @ApiModelProperty(value = "创建结束时间")
    private Long endTime;

    @ApiModelProperty(value = "创建人")
    private String createName;
}