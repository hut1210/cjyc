package com.cjyc.common.model.dto.web.salesman;

import com.cjyc.common.model.constant.RegexConstant;
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
    @Pattern(regexp = RegexConstant.REGEX_MOBILE_EXACT,message = "电话号码格式不对")
    private String phone;

}
