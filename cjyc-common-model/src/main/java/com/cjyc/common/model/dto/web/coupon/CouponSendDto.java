package com.cjyc.common.model.dto.web.coupon;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CouponSendDto extends BasePageDto implements Serializable {

    @ApiModelProperty("优惠券ids")
    private List<Long> ids;
}