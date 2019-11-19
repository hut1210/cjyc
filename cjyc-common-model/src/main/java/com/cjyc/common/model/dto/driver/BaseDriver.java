package com.cjyc.common.model.dto.driver;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class BaseDriver extends BasePageDto {
    private static final long serialVersionUID = -640959385420785099L;

    @ApiModelProperty("司机id")
    @NotNull(message = "司机id不能为空")
    private Long loginId;
}