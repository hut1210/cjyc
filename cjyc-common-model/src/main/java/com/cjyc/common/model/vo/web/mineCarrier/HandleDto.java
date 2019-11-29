package com.cjyc.common.model.vo.web.mineCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class HandleDto implements Serializable {
    private static final long serialVersionUID = -2990896777049404058L;

    @ApiModelProperty(value = "登陆用户id",required = true)
    @NotNull(message = "登陆用户id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long driverId;

    @ApiModelProperty(value = "操作标志  5：冻结 6：解冻 " +
            "11：设为管理员 12：解除管理员",required = true)
    @NotNull(message = "操作标志不能为空")
    private Integer flag;
}