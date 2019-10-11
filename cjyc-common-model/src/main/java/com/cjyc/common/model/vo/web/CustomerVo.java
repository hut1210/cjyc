package com.cjyc.common.model.vo.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerVo implements Serializable {

    @ApiModelProperty(value = "账号（手机号）")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证人像")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面（国徽）")
    private String idCardBackImg;

    @ApiModelProperty(value = "注册时间")
    private String registerTime;
}