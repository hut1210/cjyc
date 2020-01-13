package com.cjyc.common.model.dto.web;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class PageWebDto extends BasePageDto {
    @ApiModelProperty("登录人ID")
    private Long loginId;
    @ApiModelProperty("角色ID")
    private Long roleId;
    @ApiModelProperty(value = "业务范围(无需传参)", hidden = true)
    private Set<Long> bizScope;
}
