package com.cjyc.common.model.dto.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class AppCustomerDto implements Serializable {
    private static final long serialVersionUID = 3499636425265703228L;
    @ApiModelProperty(value = "客户id",required = true)
    @NotNull(message = "客户id不能为空")
    private Long loginId;
}