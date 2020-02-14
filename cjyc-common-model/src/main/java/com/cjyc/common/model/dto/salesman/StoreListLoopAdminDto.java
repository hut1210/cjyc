package com.cjyc.common.model.dto.salesman;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StoreListLoopAdminDto {

    @ApiModelProperty(value = "登陆业务员id",required = true)
    @NotNull(message = "登陆业务员id不能为空")
    private Long loginId;


    @ApiModelProperty(value = "登陆业务员id",required = true)
    private String name;

}
