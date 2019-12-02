package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
public class CarrierDto implements Serializable {

    private static final long serialVersionUID = 8035192987370988017L;
    @ApiModelProperty("承运商id(carrierId)")
    private Long carrierId;

    @ApiModelProperty(value = "当前登陆用户id(loginId)",required = true)
    @NotNull(message = "当前登陆用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "企业名称",required = true)
    @NotBlank(message = "企业名称不能为空")
    @Pattern(regexp = RegexConstant.NAME,message = "请输入合法企业名称")
    private String name;

    @ApiModelProperty(value = "是否开发票 0：否  1：是",required = true)
    @NotNull(message = "是否开发票不能为空")
    private Integer isInvoice;

    @ApiModelProperty(value = "法人姓名",required = true)
    @NotBlank(message = "法人姓名不能为空")
    private String legalName;

    @ApiModelProperty(value = "身份证号",required = true)
    @NotBlank(message = "身份证号不能为空")
    private String legalIdCard;

    @ApiModelProperty(value = "联系人姓名",required = true)
    @NotBlank(message = "联系人姓名不能为空")
    @Pattern(regexp = RegexConstant.NAME,message = "请输入合法联系人")
    private String linkman;

    @ApiModelProperty(value = "联系人手机号(作为登陆账号)",required = true)
    @NotBlank(message = "联系人手机号不能为空")
    private String linkmanPhone;

    @ApiModelProperty(value = "结算类型 0:时付，1:账期",required = true)
    @NotNull(message = "结算类型不能为空")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty(value = "业务城市",required = true)
    @NotEmpty(message = "业务城市不能为空")
    private List<String> codes;

    @ApiModelProperty(value = "承运方式：2 : 代驾 3 : 干线 4：拖车  5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车",required = true)
    @NotNull(message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty(value = "卡类型:1公户，2私户",required = true)
    @NotNull(message = "卡类型不能为空")
    private Integer cardType;

    @ApiModelProperty(value = "银行卡户主",required = true)
    @NotBlank(message = "银行卡户主不能为空")
    @Pattern(regexp = RegexConstant.NAME,message = "请输入合法银行卡户主")
    private String cardName;

    @ApiModelProperty(value = "开户行",required = true)
    @NotBlank(message = "开户行不能为空")
    private String bankName;

    @ApiModelProperty(value = "银行卡号",required = true)
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;

    @ApiModelProperty("营业执照正面")
    private String busLicenseFrontImg;

    @ApiModelProperty("营业执照反面")
    private String busLicenseBackImg;

    @ApiModelProperty("道路运输许可证正面")
    private String transportLicenseFrontImg;

    @ApiModelProperty("道路运输许可证反面")
    private String transportLicenseBackImg;

    @ApiModelProperty("银行开户证明正面")
    private String bankOpenFrontImg;

    @ApiModelProperty("银行开户证明反面")
    private String bankOpenBackImg;
}