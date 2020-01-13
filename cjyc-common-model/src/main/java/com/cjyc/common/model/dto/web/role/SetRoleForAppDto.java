package com.cjyc.common.model.dto.web.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@Validated
public class SetRoleForAppDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识", required = true)
    private Long roleId;
    @ApiModelProperty(value = "是否可以登录APP 1: 可以登录 2：不可登录")
    private Integer loginApp;
    @ApiModelProperty(value = "APP权限按钮控制，1：分配订单 2：提车调度 3：干线调度 4：送车调度")
    private List<Integer> appBtnList;
}
