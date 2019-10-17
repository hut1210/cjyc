package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class OrderCarLineWaitDispatchCountListDto {

    @NotNull
    @ApiModelProperty(value = "城市编码", required = true)
    private String cityCode;

    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

}
