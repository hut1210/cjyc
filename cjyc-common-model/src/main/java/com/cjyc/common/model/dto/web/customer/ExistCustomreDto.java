package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class ExistCustomreDto implements Serializable {
    private static final long serialVersionUID = 6452401960061880843L;

    @ApiModelProperty("客户id(customerId)，新增时不需要传，修改时要传")
    private Long customerId;

    @ApiModelProperty(value = "客户手机号",required = true)
    @NotBlank(message = "客户手机号不能未空")
    @Pattern(regexp = RegexConstant.REGEX_MOBILE_EXACT_LATEST,message = "电话号码格式不对")
    private String phone;

}