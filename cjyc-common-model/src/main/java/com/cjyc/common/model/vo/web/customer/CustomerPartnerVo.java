package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerPartnerVo implements Serializable {
    private static final long serialVersionUID = -3153565792285453161L;

    @ApiModelProperty("合伙人id")
    private Long id;

    @ApiModelProperty("合伙人userId")
    private Long userId;

    @ApiModelProperty("客户编号")
    private String customerNo;

    @ApiModelProperty("合伙人名称")
    private String name;

    @ApiModelProperty("联系人")
    private String contactMan;

    @ApiModelProperty("联系人手机号")
    private String contactPhone;

    @ApiModelProperty("是否一般纳税人 0：否  1：是")
    private Integer isTaxpayer;

    @ApiModelProperty("是否可以开票 0：否 1：是")
    private Integer isInvoice;

    @ApiModelProperty("结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty("总单量")
    private Integer totalOrder;

    @ApiModelProperty("总运车量")
    private Integer totalCar;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("备注")
    private String description;

    @ApiModelProperty("账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty("注册时间")
    private String registerTime;
}