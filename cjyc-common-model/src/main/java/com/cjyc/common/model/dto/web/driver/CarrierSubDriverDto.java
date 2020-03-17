package com.cjyc.common.model.dto.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CarrierSubDriverDto implements Serializable {
    private static final long serialVersionUID = -3687348924020148884L;

    @ApiModelProperty(value = "社会司机要转的承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long changeCarrierId;

    @ApiModelProperty(value = "社会司机手机号不能为空",required = true)
    @NotBlank(message = "社会司机手机号不能为空")
    private String phone;


}