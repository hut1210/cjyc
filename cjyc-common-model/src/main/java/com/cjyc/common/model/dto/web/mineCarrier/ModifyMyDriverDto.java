package com.cjyc.common.model.dto.web.mineCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ModifyMyDriverDto extends ModifyMyCarDto implements Serializable {
    private static final long serialVersionUID = -2484034830988498379L;

    @ApiModelProperty(value = "登陆人id(loginId)",required = true)
    @NotNull(message = "登陆人id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "司机真实姓名",required = true)
    @NotBlank(message = "司机真实姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "司机手机号",required = true)
    @NotBlank(message = "司机手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "司机身份证号",required = true)
    @NotBlank(message = "司机身份证号不能为空")
    private String idCard;

    @ApiModelProperty(value = "承运方式：2 : 代驾  3 : 干线   4：拖车",required = true)
    @NotNull(message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty("身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    private String idCardBackImg;
}