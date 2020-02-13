package com.cjyc.common.model.dto.customer.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SimpleSaveOrderDto extends BaseLoginDto {
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
}
