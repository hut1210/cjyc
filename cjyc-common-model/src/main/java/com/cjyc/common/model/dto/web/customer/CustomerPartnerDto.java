package com.cjyc.common.model.dto.web.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CustomerPartnerDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = -4471541673400527556L;
    @ApiModelProperty(value = "标志 0:订单 1:客户")
    private Integer flag;

    @ApiModelProperty("客户编码")
    private String customerNo;

    @ApiModelProperty("合伙人名称")
    private String name;

    @ApiModelProperty("结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty("联系人")
    private String contactMan;

    @ApiModelProperty("联系电话")
    private String contactPhone;

    @ApiModelProperty("是否可以开票 0：否 1：是")
    private Integer isInvoice;

    @ApiModelProperty("账号来源：1：App注册，2：Applet注册，3：韵车后台 4：升级创建")
    private Integer source;

    @ApiModelProperty("统一社会信用代码")
    private String socialCreditCode;
}