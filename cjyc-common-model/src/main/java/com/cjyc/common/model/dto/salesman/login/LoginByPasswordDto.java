package com.cjyc.common.model.dto.salesman.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 入参对象
 * </p>
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LoginInDto对象", description="/login/in")
public class LoginByPasswordDto {

    @NotNull
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotNull
    @ApiModelProperty(value = "密码", required = true)
    private String password;

}
