package com.cjyc.common.model.dto.web.salesman;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 角色部门关系信息（非业务中心）
 */
@ApiModel
@Data
@Validated
public class RoleDeptCodeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;
    @NotEmpty
    @ApiModelProperty(value = "机构Code列表", required = true)
    private List<String> deptCodeList;
}
