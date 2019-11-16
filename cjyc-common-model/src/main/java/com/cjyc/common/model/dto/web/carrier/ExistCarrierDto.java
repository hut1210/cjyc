package com.cjyc.common.model.dto.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ExistCarrierDto implements Serializable {
    private static final long serialVersionUID = -547903369124502831L;

    @ApiModelProperty("承运商id(carrierId)")
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("联系人手机号")
    @NotBlank(message = "联系人手机号不能为空")
    private String linkmanPhone;
}