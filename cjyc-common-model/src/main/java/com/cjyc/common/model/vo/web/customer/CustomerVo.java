package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerVo implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户userId")
    private Long userId;

    @ApiModelProperty("C端客户编号")
    private String customerNo;

    @ApiModelProperty(value = "账号（手机号）")
    private String contactPhone;

    @ApiModelProperty(value = "联系人/姓名")
    private String contactMan;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证人像")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面（国徽）")
    private String idCardBackImg;

    @ApiModelProperty(value = "注册时间")
    private String registerTime;

    @ApiModelProperty("账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty("注册操作人")
    private String createUserName;

    @ApiModelProperty("注册操作人userId")
    private Long createUserId;

    @ApiModelProperty("总单量")
    private Integer totalOrder;

    @ApiModelProperty("总运车量")
    private Integer totalCar;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;
}