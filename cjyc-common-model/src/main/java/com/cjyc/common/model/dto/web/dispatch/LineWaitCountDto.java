package com.cjyc.common.model.dto.web.dispatch;

import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LineWaitCountDto extends BaseWebDto {

    @NotNull
    @ApiModelProperty(value = "城市编码", required = true)
    private String cityCode;
}
