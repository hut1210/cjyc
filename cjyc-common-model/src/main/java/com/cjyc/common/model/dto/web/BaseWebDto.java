package com.cjyc.common.model.dto.web;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class BaseWebDto {
    @ApiModelProperty("登录人ID")
    private Long loginId;
    @ApiModelProperty("角色ID")
    private Long roleId;
    @ApiModelProperty(value = "登录人ID", hidden = true)
    private String loginName;
    @ApiModelProperty(value = "登录人手机号", hidden = true)
    private String loginPhone;
    @ApiModelProperty(value = "登录人手机号", hidden = true)
    private UserTypeEnum loginType;
    @ApiModelProperty(value = "业务范围(无需传参)", hidden = true)
    private Set<Long> bizScope;


    public BaseWebDto(Long loginId, Long roleId) {
        this.loginId = loginId;
        this.roleId = roleId;
    }
}
