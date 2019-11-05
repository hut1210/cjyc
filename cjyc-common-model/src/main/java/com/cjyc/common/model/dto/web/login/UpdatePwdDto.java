package com.cjyc.common.model.dto.web.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 修改密码请求信息
 */
@Data
@ApiModel
@Validated
public class UpdatePwdDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotEmpty(message = "账号不能为空")
    @ApiModelProperty(value = "账号")
    private String account;
    @NotEmpty(message = "初始密码不能为空")
    @ApiModelProperty(value = "初始密码")
    private String initPwd;
    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String newPwd;
}
