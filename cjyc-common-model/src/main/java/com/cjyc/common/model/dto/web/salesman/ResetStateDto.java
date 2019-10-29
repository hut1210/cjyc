package com.cjyc.common.model.dto.web.salesman;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 启用、停用系统用户信息
 */
@Data
@ApiModel
@Validated
public class ResetStateDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "用户标识不能为空")
    @ApiModelProperty(value = "用户标识", required = true)
    private Long id;
    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(value = "用户状态 1：启用 2：停用", required = true)
    private Integer state;
}
