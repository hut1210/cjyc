package com.cjyc.common.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerVo implements Serializable {

    /**
     * 账号（手机号）
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证人像
     */
    private String idCardFrontImg;

    /**
     * 身份证反面（国徽）
     */
    private String idCardBackImg;

    /**
     * 注册时间
     */
    private String registerTime;
}