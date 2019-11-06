package com.cjyc.common.model.dto.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class CarrierDto implements Serializable {

    private static final long serialVersionUID = 8035192987370988017L;

    public interface SaveCarrierDto {
    }

    public interface UpdateCarrierDto {
    }

    @ApiModelProperty("承运商id")
    @NotNull(groups = {CarrierDto.UpdateCarrierDto.class},message = "承运商id不能为空")
    private Long id;

    @ApiModelProperty("当前登陆用户userId")
    @NotNull(groups = {CarrierDto.SaveCarrierDto.class},message = "当前登陆用户userId不能为空")
    private Long userId;

    @ApiModelProperty("企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String name;

    @ApiModelProperty("是否开发票 0：否  1：是")
    @NotNull(message = "是否开发票不能为空")
    private Integer isInvoice;

    @ApiModelProperty("法人姓名")
    @NotBlank(message = "法人姓名不能为空")
    private String legalName;

    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String legalIdCard;

    @ApiModelProperty("联系人姓名")
    @NotBlank(message = "联系人姓名不能为空")
    private String linkman;

    @ApiModelProperty("联系人手机号(作为登陆账号)")
    @NotBlank(message = "联系人手机号不能为空")
    private String linkmanPhone;

    @ApiModelProperty("结算类型 0:时付，1:账期")
    @NotNull(message = "结算类型不能为空")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty("业务范围")
    @NotEmpty(message = "业务范围不能为空")
    private List<String> codes;

    @ApiModelProperty("承运方式：2 : 代驾 3 : 干线 4：拖车  5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    @NotNull(message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty("卡类型:1公户，2私户")
    private Integer cardType;

    @ApiModelProperty("持卡人姓名/企业名称")
    @NotBlank(message = "持卡人姓名/企业名称不能为空")
    private String cardName;

    @ApiModelProperty("开户行")
    @NotBlank(message = "开户行不能为空")
    private String bankName;

    @ApiModelProperty("银行卡号")
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;

    @ApiModelProperty("营业执照正面")
    @NotBlank(message = "营业执照正面不能为空")
    private String busLicenseFrontImg;

    @ApiModelProperty("营业执照反面")
    @NotBlank(message = "营业执照反面不能为空")
    private String busLicenseBackImg;

    @ApiModelProperty("道路运输许可证正面")
    @NotBlank(message = "道路运输许可证正面不能为空")
    private String transportLicenseFrontImg;

    @ApiModelProperty("道路运输许可证反面")
    @NotBlank(message = "道路运输许可证反面不能为空")
    private String transportLicenseBackImg;

    @ApiModelProperty("银行开户证明正面")
    @NotBlank(message = "银行开户证明正面不能为空")
    private String bankOpenFrontImg;

    @ApiModelProperty("银行开户证明反面")
    @NotBlank(message = "银行开户证明反面不能为空")
    private String bankOpenBackImg;
}