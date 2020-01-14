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
 * 角色分配dto (物流平台新集成方式)
 */
@Data
@ApiModel
@Validated
public class AssignRoleNewDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "当前登陆用户id(loginId)",required = true)
    @NotNull(message = "当前登陆用户id(loginId)不能为空")
    private Long loginId;
    @NotNull(message = "用户标识不能为空")
    @ApiModelProperty(value = "用户标识", required = true)
    private Long id;
    @NotNull(message = "角色标识不能为空")
    @ApiModelProperty(value = "角色标识", required = true)
    private Long roleId;
//    @NotNull(message = "机构类型不能为空")
//    @ApiModelProperty(value = "机构类型 1: 业务中心 2：非业务中心", required = true)
    @ApiModelProperty(value = "机构类型 1：全国 2：大区 3：省 4：城市 5：业务中心", required = true)
    private Integer deptType;
    @NotEmpty(message = "角色部门关联信息列表不能为空")
    @ApiModelProperty(value = "角色部门关联信息列表(1、2、3、4: 非业务中心 城市编码列表  5: 业务中心 业务中心id列表)", required = true)
    private List<String> deptIdList;
    @ApiModelProperty(value = "业务范围描述信息")
    private String bizDesc;
    @ApiModelProperty(value = "角色信息是否覆写 1：覆写 2：追加")
    private Integer overwriteFlag;
}
