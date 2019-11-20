package com.cjyc.common.model.dto.web.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 修改角色-菜单列表请求信息
 */

@Data
@ApiModel
public class ModifyRoleMenusDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识", required = true)
    private Long id;
    @NotEmpty(message = "菜单id列表不能为空")
    @ApiModelProperty(value = "角色标识列表", required = true)
    private List<Long> menuIdList;
}
