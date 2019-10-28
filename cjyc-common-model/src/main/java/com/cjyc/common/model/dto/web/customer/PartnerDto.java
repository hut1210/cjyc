package com.cjyc.common.model.dto.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class PartnerDto implements Serializable {

    public interface SaveOrUpdatePartnerDto {
    }

    @ApiModelProperty("保存更新标志 1：保存 2：更新")
    private Integer flag;

    @ApiModelProperty("大客户id")
    @NotNull(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "大客户id不能为空")
    private Long id;

    @ApiModelProperty("登陆用户userId")
    @NotNull(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "登陆用户userId不能为空")
    private Long userId;

    @ApiModelProperty("合伙人名称")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "合伙人名称不能为空")
    private String name;

    @ApiModelProperty("联系人")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "联系人不能为空")
    private String contactMan;

    @ApiModelProperty("联系人手机号")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "联系人手机号不能为空")
    private String contactPhone;

    @ApiModelProperty("统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("实际详细地址")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "实际详细地址不能为空")
    private String contactAddress;

    @ApiModelProperty("是否一般纳税人 0：否  1：是")
    private int isTaxpayer;

    @ApiModelProperty("是否可以开票 0：否 1：是")
    private int isInvoice;

    @ApiModelProperty("结算方式：1时付，2账期")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "结算方式不能为空")
    private int settleType;

    @ApiModelProperty("账期/天")
    private int settlePeriod;

    @ApiModelProperty("卡类型:1公户，2私户")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "卡类型不能为空")
    private int cardType;

    @ApiModelProperty("对公开户名称/对私开户行名称")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "对公开户名称/对私开户行名称不能为空")
    private String cardName;

    @ApiModelProperty("开户银行账号/网点银行卡号")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "开户银行账号/网点银行卡号不能为空")
    private String cardNo;

    @ApiModelProperty("开户行名称")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "开户行名称不能为空")
    private String bankName;

    @ApiModelProperty("持卡人身份证号")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "持卡人身份证号不能为空")
    private String idCard;

    @ApiModelProperty("银行开户许可证")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "银行开户许可证不能为空")
    private String bankLicence;

    @ApiModelProperty("备注/描述")
    private String description;

    @ApiModelProperty("企业执照正面")
    private String businessLicenseFrontImg;

    @ApiModelProperty("企业执照反面")
    private String businessLicenseBackImg;

    @ApiModelProperty("法人身份证复印件正面")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "法人身份证复印件正面不能为空")
    private String legalIdCardFrontImg;

    @ApiModelProperty("法人身份证复印件反面")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "法人身份证复印件反面不能为空")
    private String legalIdCardBackImg;

    @ApiModelProperty("联系人身份证正面")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "联系人身份证正面不能为空")
    private String linkmanIdCardFrontImg;

    @ApiModelProperty("联系人身份证反面")
    @NotBlank(groups = {PartnerDto.SaveOrUpdatePartnerDto.class},message = "联系人身份证反面不能为空")
    private String linkmanIdCardBackImg;

    @ApiModelProperty("授权书正面")
    private String authorizationFrontImg;

    @ApiModelProperty("授权书反面")
    private String authorizationBackImg;

}