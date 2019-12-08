package com.cjyc.common.model.dto.web.driver;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class DispatchDriverDto extends BasePageDto {
    private static final long serialVersionUID = -2185088130200464543L;

    @ApiModelProperty(value = "登陆人id(loginId)",required = true)
    @NotNull(message = "登陆人id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;
}