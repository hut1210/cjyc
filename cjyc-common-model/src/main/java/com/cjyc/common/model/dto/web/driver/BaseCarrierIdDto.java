package com.cjyc.common.model.dto.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class BaseCarrierIdDto implements Serializable {
    private static final long serialVersionUID = 8845628382946361458L;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;
}