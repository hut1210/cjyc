package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CustomerCouponDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 5479763520601490989L;

    @ApiModelProperty(value = "客户id",required = true)
    @NotNull(message = "客户id不能为空")
    private Long customerId;

    @ApiModelProperty(value = "是否可用 0：不可用 1：可用",required = true)
    @NotNull(message = "是否可用不能为空")
    private Integer isUsable;

    @ApiModelProperty("有效期开始时间")
    private Long startPeriodTime;

    @ApiModelProperty("有效期结束时间")
    private Long endPeriodTime;

    @ApiModelProperty("类型 0：满减  1：直减  2：折扣")
    private Integer type;

}