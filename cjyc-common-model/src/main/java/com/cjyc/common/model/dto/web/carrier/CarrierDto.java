package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.entity.BusinessCityCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CarrierDto extends BusinessCityCode implements Serializable {

    public interface SaveCarrierDto {
    }

    public interface UpdateCarrierDto {
    }

    @ApiModelProperty("承运商id")
    @NotBlank(groups = {CarrierDto.UpdateCarrierDto.class},message = "承运商id不能为空")
    private Long id;

    @ApiModelProperty("登录人id")
    @NotBlank(groups = {CarrierDto.SaveCarrierDto.class},message = "登录人id不能为空")
    private Long userId;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("身份证号")
    private String legaIdCard;

    @ApiModelProperty("联系人姓名")
    private String linkman;

    @ApiModelProperty("承运商手机号")
    private String linkmanPhone;

    @ApiModelProperty("结算类型 1时付，2账期")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private Integer isInvoice;

    @ApiModelProperty("卡类型:1公户，2私户")
    private Integer cardType;

    @ApiModelProperty("持卡人姓名/企业名称")
    private String cardName;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("营业执照正面")
    private String busLicenseFrontImg;

    @ApiModelProperty("营业执照反面")
    private String busLicenseBackImg;

    @ApiModelProperty("道路运输许可证正面照片")
    private String transportLicenseFrontImg;

    @ApiModelProperty("道路运输许可证反面照片")
    private String transportLicenseBackImg;

    @ApiModelProperty("银行开户证明正面")
    private String bankOpenFrontImg;

    @ApiModelProperty("银行开户证明反面")
    private String bankOpenBackImg;
}