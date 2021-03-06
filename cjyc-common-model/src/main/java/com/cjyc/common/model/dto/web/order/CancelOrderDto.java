package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 入参
 * @author JPG
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CancelOrderDto extends BaseWebDto {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @ApiModelProperty(value = "原因", required = true)
    private String reason;
}

