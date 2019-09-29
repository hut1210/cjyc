package com.cjyc.web.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CustomerDto implements Serializable {

    public interface SaveCustomerVo {
    }
    /**
     * 客户名称
     */
    @NotBlank(groups = {SaveCustomerVo.class},message = "客户名称不能为空。")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(groups = {SaveCustomerVo.class},message = "手机号不能为空。")
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