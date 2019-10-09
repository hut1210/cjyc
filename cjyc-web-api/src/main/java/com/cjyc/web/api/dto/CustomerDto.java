package com.cjyc.web.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CustomerDto implements Serializable {

    public interface SaveCustomerVo {
    }

    public interface UpdateCustomerVo {
    }

    @NotNull(groups = {UpdateCustomerVo.class},message = "id不能为空")
    private Long id;
    /**
     * 客户名称
     */
    @NotBlank(groups = {SaveCustomerVo.class},message = "客户名称不能为空")
    @NotBlank(groups = {UpdateCustomerVo.class},message = "客户名称不能为空")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(groups = {SaveCustomerVo.class},message = "手机号不能为空")
    @NotBlank(groups = {UpdateCustomerVo.class},message = "手机号不能为空")
    private String phone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证正面
     */
    private String idCardFrontImg;

    /**
     * 身份证反面
     */
    private String idCardBackImg;
}