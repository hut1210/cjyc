package com.cjyc.common.model.dto.promote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 分享注册用户新增参数
 * @Author Liu Xing Xiang
 * @Date 2019/11/13 11:36
 **/
@Data
public class AdminPromoteDetailAddDto implements Serializable {
    private static final long serialVersionUID = 9143955104272494472L;
    @ApiModelProperty(value = "分享业务员账号")
    @NotNull(message = "分享业务员账号不能为空")
    private Long promoteUserId;

    @ApiModelProperty(value = "注册用户账号")
    @NotNull(message = "注册用户账号不能为空")
    private Long customerId;

    @ApiModelProperty(value = "注册用户名称")
    @NotBlank(message = "注册用户名称不能为空")
    private String customerName;

    @ApiModelProperty(value = "注册用户类型：2-司机；3-客户")
    @NotNull(message = "注册用户类型不能为空")
    private Integer type;
}
