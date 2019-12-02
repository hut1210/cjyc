package com.cjyc.common.model.dto.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class OperateDto implements Serializable {

    private static final long serialVersionUID = -1877805036648143698L;
    @ApiModelProperty(value = "操作人id(loginId)",required = true)
    @NotNull(message ="操作人id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "主键id",required = true)
    @NotNull(message = "主键id不能为空")
    private Long id;

    @ApiModelProperty("承运商id")
    private Long carrierId;

    @ApiModelProperty(value = "操作标志 3：审核通过 4：审核拒绝 5：冻结 6：解冻 7：删除 8：作废 9:关闭开关 10：打开开关" +
            "11：设为管理员 12：解除管理员",required = true)
    @NotNull(message = "操作标志不能为空")
    private Integer flag;

}