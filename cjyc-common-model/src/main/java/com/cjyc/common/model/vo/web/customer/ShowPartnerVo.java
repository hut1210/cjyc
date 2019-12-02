package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ShowPartnerVo implements Serializable {
    private static final long serialVersionUID = 7985906758582340941L;

    @ApiModelProperty("客户id")
    private Long customerId;

    @ApiModelProperty("合伙人名称")
    private String name;

    @ApiModelProperty("联系人")
    private String contactMan;

    @ApiModelProperty("联系电话")
    private String contactPhone;

    @ApiModelProperty("统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("实际地址")
    private String contactAddress;
    @ApiModelProperty("是否一般纳税人 0：否  1：是")
    private Integer isTaxpayer;
    @ApiModelProperty("是否可以开票 0：否 1：是")
    private Integer isInvoice;

    @ApiModelProperty("结算方式：0时付，1账期")
    private Integer settleType;
    @ApiModelProperty("账期/天")
    private Integer settlePeriod;


    @ApiModelProperty("卡类型:1公户，2私户")
    private Integer cardType;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("银行卡卡号")
    private String cardNo;
    @ApiModelProperty("银行卡户主")
    private String cardName;
    @ApiModelProperty("银行开户许可证")
    private String bankLicence;
    @ApiModelProperty("备注")
    private String description;
    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("企业执照正面")
    private String businessLicenseFrontImg;
    @ApiModelProperty("企业执照反面")
    private String businessLicenseBackImg;

    @ApiModelProperty("法人身份证复印件正面")
    private String legalIdcardFrontImg;
    @ApiModelProperty("法人身份证复印件反面")
    private String legalIdcardBackImg;

    @ApiModelProperty("联系人身份证正面")
    private String linkmanIdcardFrontImg;
    @ApiModelProperty("联系人身份证反面")
    private String linkmanIdcardBackImg;

    @ApiModelProperty("授权书正面")
    private String authorizationFrontImg;
    @ApiModelProperty("授权书反面")
    private String authorizationBackImg;
}