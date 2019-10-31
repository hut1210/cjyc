package com.cjyc.common.model.dto.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class OperateDto implements Serializable {

    @ApiModelProperty("操作人userId")
    @NotNull(message ="操作人userId不能为空")
    private Long userId;

    @ApiModelProperty("主键id")
    @NotNull(message = "主键id不能为空")
    private Long id;

    @ApiModelProperty("操作标志 3：审核通过 4：审核拒绝 5：冻结 6：解冻 7：删除 8：作废 9:关闭开关 10：打开开关")
    @NotNull(message = "操作标志不能为空")
    private Integer state;

}