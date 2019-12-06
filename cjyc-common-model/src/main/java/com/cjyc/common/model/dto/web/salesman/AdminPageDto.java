package com.cjyc.common.model.dto.web.salesman;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class AdminPageDto extends BasePageDto {
    @ApiModelProperty(value = "登录角色ID", required = true)
    private Long roleId;

    @ApiModelProperty(value = "条件-业务中心ID", required = true)
    private Long storeId;

    @ApiModelProperty(value = "条件-姓名")
    private String name;

    @ApiModelProperty(value = "条件-角色名称", required = true)
    private String roleName;

    @ApiModelProperty(value = "条件-电话")
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$",message = "电话号码格式不对")
    private String phone;

}
