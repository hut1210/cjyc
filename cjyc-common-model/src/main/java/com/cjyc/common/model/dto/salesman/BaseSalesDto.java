package com.cjyc.common.model.dto.salesman;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class BaseSalesDto implements Serializable {
    private static final long serialVersionUID = -1560969906609895793L;
    @ApiModelProperty(value = "登陆业务员id",required = true)
    @NotNull(message = "登陆业务员id不能为空")
    private Long loginId;
}