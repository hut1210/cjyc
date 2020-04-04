package com.cjyc.common.model.vo.web.customer;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.SecondLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerPartnerVo implements Serializable {
    private static final long serialVersionUID = -3153565792285453161L;

    @ApiModelProperty(value = "合伙人id(customerId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long customerId;

    @ApiModelProperty(value = "合伙人userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "客户编号")
    private String customerNo;

    @ApiModelProperty(value = "合伙人名称")
    private String name;

    @ApiModelProperty(value = "状态  0：待审核 1：审核中  2:已审核 4：已取消  5：已冻结  7：已拒绝   9:停用")
    private Integer state;

    @ApiModelProperty(value = "联系人")
    private String contactMan;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value = "是否一般纳税人 0：否  1：是")
    private Integer isTaxpayer;

    @ApiModelProperty(value = "是否可以开票 0：否 1：是")
    private Integer isInvoice;

    @ApiModelProperty(value = "结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty(value = "账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "总单量")
    private Integer totalOrder;

    @ApiModelProperty(value = "总运车量")
    private Integer totalCar;

    @ApiModelProperty(value = "订单总金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty(value = "注册时间")
    @JsonSerialize(using = SecondLongSerizlizer.class)
    private Long registerTime;

    @ApiModelProperty(value = "创建人名称")
    private String createUserName;
}