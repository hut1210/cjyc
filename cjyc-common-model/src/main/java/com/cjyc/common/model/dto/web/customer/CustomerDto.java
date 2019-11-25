package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = -8088522140001277467L;

    @ApiModelProperty(value = "当前登陆用户id(loginId)",required = true)
    @NotNull(message = "当前登陆用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "C端用户id(主键id)")
    private Long customerId;

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称",required = true)
    @Pattern(regexp = RegexConstant.NAME,message = "请输入合法姓名")
    private String contactMan;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号",required = true)
    @Pattern(regexp = RegexConstant.REGEX_MOBILE_EXACT_LATEST,message = "电话号码格式不对")
    private String contactPhone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;
}