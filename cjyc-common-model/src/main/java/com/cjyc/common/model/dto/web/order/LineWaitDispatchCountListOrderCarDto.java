package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class LineWaitDispatchCountListOrderCarDto {

    @NotNull
    @ApiModelProperty(value = "城市编码", required = true)
    private String cityCode;

    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

}
