package com.cjyc.common.model.dto.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(groups = {CarrierDto.UpdateCarrierDto.class},message = "承运商id不能为空")
    private Long id;

    @ApiModelProperty("当前登陆用户userId")
    @NotBlank(groups = {CarrierDto.SaveCarrierDto.class},message = "当前登陆用户userId不能为空")
    private Long userId;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private Integer isInvoice;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("身份证号")
    private String legaIdCard;

    @ApiModelProperty("联系人姓名")
    private String linkman;

    @ApiModelProperty("联系人手机号(作为登陆账号)")
    private String linkmanPhone;

    @ApiModelProperty("结算类型 0:时付，1:账期")
    private Integer settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty("业务范围")
    private List<String> codes;

    @ApiModelProperty("承运方式：2 : 代驾 3 : 干线 4：拖车  5：代驾+干线  6：代驾+拖车  7：干线+拖车  9：代驾+干线+拖车")
    private Integer mode;

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