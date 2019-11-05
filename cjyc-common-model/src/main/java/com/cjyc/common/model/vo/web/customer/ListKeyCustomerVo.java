package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ListKeyCustomerVo implements Serializable {

    @ApiModelProperty(value = "大客户id")
    private Long id;

    @ApiModelProperty(value = "大客户userId")
    private Long userId;

    @ApiModelProperty("大客户编号")
    private String customerNo;

    @ApiModelProperty(value = "大客户客户全称")
    private String name;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "客户类型  0：电商 1：租赁 2：金融公司 3：经销商 4：其他")
    private String customerNature;

    @ApiModelProperty("账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty("总单量")
    private Integer totalOrder;

    @ApiModelProperty("总运车量")
    private Integer totalCar;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "注册时间")
    private String registerTime;

    @ApiModelProperty("创建人userId")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;
}