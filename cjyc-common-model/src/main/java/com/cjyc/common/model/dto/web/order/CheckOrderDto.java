package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CheckOrderDto extends BaseLoginDto {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

}
