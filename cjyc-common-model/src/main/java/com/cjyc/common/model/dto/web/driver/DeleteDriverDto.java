package com.cjyc.common.model.dto.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class DeleteDriverDto implements Serializable {
    private static final long serialVersionUID = 5831805380986911167L;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long driverId;

    @ApiModelProperty(value = "司机手机号",required = true)
    @NotBlank(message = "司机手机号不能为空")
    private String phone;
}