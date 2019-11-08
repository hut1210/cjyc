package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = -8088522140001277467L;

    public interface SaveCustomerVo {
    }

    public interface UpdateCustomerVo {
    }

    @ApiModelProperty("当前登陆用户id(loginId)")
    @NotNull(groups = {UpdateCustomerVo.class},message = "当前登陆用户id(loginId)不能为空")
    private Long loginId;

    @NotNull(groups = {UpdateCustomerVo.class},message = "C端用户id不能为空")
    @ApiModelProperty(value = "C端用户id(主键id)",required = true)
    private Long customerId;

    @NotBlank(groups = {SaveCustomerVo.class},message = "客户名称不能为空")
    @NotBlank(groups = {UpdateCustomerVo.class},message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称",required = true)
    private String name;

    @NotBlank(groups = {SaveCustomerVo.class},message = "手机号不能为空")
    @NotBlank(groups = {UpdateCustomerVo.class},message = "手机号不能为空")
    @ApiModelProperty(value = "手机号",required = true)
    private String contactPhone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;
}