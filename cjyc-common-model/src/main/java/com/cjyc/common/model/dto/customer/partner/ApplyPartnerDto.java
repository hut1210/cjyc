package com.cjyc.common.model.dto.customer.partner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ApplyPartnerDto implements Serializable {

    private static final long serialVersionUID = 8991222858674030585L;

    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Long id;

    @ApiModelProperty("用户userId")
    @NotNull(message = "用户userId不能为空")
    private Long userId;

    @ApiModelProperty("合伙人名称")
    @NotBlank(message = "合伙人名称不能为空")
    private String name;

    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不能为空")
    private String contactMan;

    @ApiModelProperty("联系人手机号")
    @NotBlank(message = "联系人手机号不能为空")
    private String contactPhone;

    @ApiModelProperty("统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("实际详细地址")
    @NotBlank(message = "实际详细地址不能为空")
    private String contactAddress;

    @ApiModelProperty("是否一般纳税人 0：否  1：是")
    @NotNull(message = "纳税人不能为空")
    private Integer isTaxpayer;

    @ApiModelProperty("是否可以开票 0：否 1：是")
    @NotNull(message = "是否可以开票不能为空")
    private Integer isInvoice;

    @ApiModelProperty("结算方式：0:时付，1:账期")
    @NotNull(message = "结算方式不能为空")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty("卡类型:1公户，2私户")
    @NotNull(message = "卡类型不能为空")
    private Integer cardType;

    @ApiModelProperty("开户名称")
    @NotBlank(message = "开户名称不能为空")
    private String cardName;

    @ApiModelProperty("银行账号")
    @NotBlank(message = "银行账号不能为空")
    private String cardNo;

    @ApiModelProperty("开户行名称")
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

    @ApiModelProperty("法人身份证复印件正面")
    @NotBlank(message = "法人身份证复印件正面不能为空")
    private String legalIdcardFrontImg;

    @ApiModelProperty("法人身份证复印件反面")
    @NotBlank(message = "法人身份证复印件反面不能为空")
    private String legalIdcardBackImg;

    @ApiModelProperty("联系人身份证正面")
    @NotBlank(message = "联系人身份证正面不能为空")
    private String linkmanIdcardFrontImg;

    @ApiModelProperty("联系人身份证反面")
    @NotBlank(message = "联系人身份证反面不能为空")
    private String linkmanIdcardBackImg;

    @ApiModelProperty("授权书正面")
    private String authorizationFrontImg;

    @ApiModelProperty("授权书反面")
    private String authorizationBackImg;
}