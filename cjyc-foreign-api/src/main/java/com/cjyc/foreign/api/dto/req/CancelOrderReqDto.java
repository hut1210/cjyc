package com.cjyc.foreign.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 取消订单请求dto
 */
@Data
@ApiModel
@Validated
public class CancelOrderReqDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "订单编号不能为空")
    @ApiModelProperty(name = "订单编号")
    private String orderNo;
}
