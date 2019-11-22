package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class PartnerDto implements Serializable {

    private static final long serialVersionUID = -1399830481710292738L;

    @ApiModelProperty(value = "升级标志 默认：false 升级：true",required = true)
    @NotNull(message = "升级标志不能为空")
    private Boolean flag;

    @ApiModelProperty("合伙人id(customerId)")
    private Long customerId;

    @ApiModelProperty(value = "登陆用户id(loginId)",required = true)
    @NotNull(message = "登陆用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "合伙人名称",required = true)
    @NotBlank(message = "合伙人名称不能为空")
    private String name;

    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank(message = "联系人不能为空")
    private String contactMan;

    @ApiModelProperty(value = "联系人手机号",required = true)
    @NotBlank(message = "联系人手机号不能为空")
    private String contactPhone;

    @ApiModelProperty("统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "实际详细地址",required = true)
    @NotBlank(message = "实际详细地址不能为空")
    private String contactAddress;

    @ApiModelProperty("是否一般纳税人 0：否  1：是")
    private Integer isTaxpayer;

    @ApiModelProperty("是否可以开票 0：否 1：是")
    private Integer isInvoice;

    @ApiModelProperty(value = "结算方式：0:时付，1:账期",required = true)
    @NotNull(message = "结算方式不能为空")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "卡类型:1公户，2私户",required = true)
    @NotNull(message = "卡类型不能为空")
    private Integer cardType;

    @ApiModelProperty(value = "账号名称",required = true)
    @NotBlank(message = "账号名称不能为空")
    private String cardName;

    @ApiModelProperty(value = "银行卡号",required = true)
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;

    @ApiModelProperty(value = "开户行名称",required = true)
    @NotBlank(message = "开户行名称不能为空")
    private String bankName;

    @ApiModelProperty("持卡人身份证号")
    private String idCard;

    @ApiModelProperty("银行开户许可证")
    private String bankLicence;

    @ApiModelProperty("备注/描述")
    private String description;

    @ApiModelProperty("企业执照正面")
    private String businessLicenseFrontImg;

    @ApiModelProperty("企业执照反面")
    private String businessLicenseBackImg;

    @ApiModelProperty(value = "法人身份证复印件正面",required = true)
    @NotBlank(message = "法人身份证复印件正面不能为空")
    private String legalIdcardFrontImg;

    @ApiModelProperty(value = "法人身份证复印件反面",required = true)
    @NotBlank(message = "法人身份证复印件反面不能为空")
    private String legalIdcardBackImg;

    @ApiModelProperty(value = "联系人身份证正面",required = true)
    @NotBlank(message = "联系人身份证正面不能为空")
    private String linkmanIdcardFrontImg;

    @ApiModelProperty(value = "联系人身份证反面",required = true)
    @NotBlank(message = "联系人身份证反面不能为空")
    private String linkmanIdcardBackImg;

    @ApiModelProperty("授权书正面")
    private String authorizationFrontImg;

    @ApiModelProperty("授权书反面")
    private String authorizationBackImg;

}