package com.cjyc.common.model.dto.web.salesman;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 角色分配dto
 */
@Data
@ApiModel
@Validated
public class AssignRoleDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "用户标识不能为空")
    @ApiModelProperty(value = "用户标识", required = true)
    private Long id;
    @NotEmpty(message = "角色部门关联信息列表不能为空")
    @ApiModelProperty(value = "角色部门关联信息列表", required = true)
    private List<RoleDeptDto> roleDeptList;
    @ApiModelProperty(value = "业务范围描述信息")
    private String bizDesc;
}
