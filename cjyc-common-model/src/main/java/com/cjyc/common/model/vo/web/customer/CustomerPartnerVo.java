package com.cjyc.common.model.vo.web.customer;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerPartnerVo implements Serializable {
    private static final long serialVersionUID = -3153565792285453161L;

    @ApiModelProperty("合伙人id(customerId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty("合伙人userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("客户编号")
    private String customerNo;

    @ApiModelProperty("合伙人名称")
    private String name;

    @ApiModelProperty("状态：0待审核，1审核中，2已审核，3审核拒绝， 7已冻结")
    private Integer state;

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
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAmount;

    @ApiModelProperty("统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("备注")
    private String description;

    @ApiModelProperty("账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty("注册时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long registerTime;

    @ApiModelProperty("创建人名称")
    private String createUserName;
}