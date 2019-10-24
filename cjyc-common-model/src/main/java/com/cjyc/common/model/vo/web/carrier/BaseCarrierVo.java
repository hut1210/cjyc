package com.cjyc.common.model.vo.web.carrier;

import com.cjyc.common.model.entity.BusinessCityCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BaseCarrierVo extends BusinessCityCode implements Serializable {

    @ApiModelProperty("承运商id")
    private Long id;

    @ApiModelProperty("公司名称")
    private String name;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private String isInvoice;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("法人身份证号")
    private String legalIdCard;

    @ApiModelProperty("联系人姓名")
    private String linkman;

    @ApiModelProperty("联系人手机号")
    private String linkmanPhone;

    @ApiModelProperty("结算方式：1时付，2账期")
    private String settleType;

    @ApiModelProperty("账期/天")
    private String settlePeriod;

    @ApiModelProperty("承运方式：1代驾，2托运，3干线 4全支持")
    private String mode;

    @ApiModelProperty("卡类型:1公户，2私户")
    private String cardType;

    @ApiModelProperty("银行卡户主")
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