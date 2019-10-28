package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
public class SelectCustomerDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("登陆用户userId")
    @NotNull(message = "当前登陆用户userId不能为空")
    private Long userId;

    @ApiModelProperty(value = "手机号(账号)")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard;
}