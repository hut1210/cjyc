package com.cjyc.common.model.dto.customer.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SimpleSaveOrderDto extends BaseLoginDto {
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @ApiModelProperty(value = "订单金额", required = true)
    private BigDecimal totalFee;
}
