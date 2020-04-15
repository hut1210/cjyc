package com.cjyc.common.model.dto.web;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseWebDto extends BaseLoginDto {
    @ApiModelProperty("角色ID")
    private Long roleId;
}
