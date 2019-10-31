package com.cjyc.common.model.dto.web.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 添加角色dto
 */
@Data
@ApiModel
@Validated
public class AddRoleDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;
    @NotNull(message = "角色创建所属机构级别不能为空")
    @ApiModelProperty(value = "角色所属机构级别 1：全国 2：大区 3：省 4：城市 5：业务中心", required = true)
    private Integer level;
    @NotNull(message = "机构范围不能为空")
    @ApiModelProperty(value = "机构范围 1：内部 2：外部", required = true)
    private Integer range;
    @ApiModelProperty(value = "菜单id列表信息")
    private List<Long> menuIdList;
}
