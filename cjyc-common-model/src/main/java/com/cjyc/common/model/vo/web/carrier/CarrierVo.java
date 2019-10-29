package com.cjyc.common.model.vo.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CarrierVo implements Serializable {

    @ApiModelProperty("承运商id")
    private Long id;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("公司联系人")
    private String linkman;

    @ApiModelProperty("公司联系人手机号")
    private String linkmanPhone;

    @ApiModelProperty("法人姓名")
    private String legalName;

    @ApiModelProperty("法人身份证号")
    private String idCard;

    @ApiModelProperty("计算类型 1时付，2账期")
    private Integer settleType;

    @ApiModelProperty("账期时间/天")
    private Integer settlePeriod;

    @ApiModelProperty("是否支持代驾  0 : 否  1 : 是")
    private Integer driverMode;

    @ApiModelProperty("是否支持拖车  0 ：否  1 ：是")
    private Integer trailerMode;

    @ApiModelProperty("是否支持干线  0：否 1：是")
    private Integer trunkMode;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private Integer isInvoice;

    @ApiModelProperty("卡类型:1公户，2私户")
    private Integer cardType;

    @ApiModelProperty("持卡人名称")
    private String cardName;

    @ApiModelProperty("开户银行")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("营业执照正面")
    private String busLicenseFrontImg;

    @ApiModelProperty("道路运输许可证正面照片")
    private String transportLicenseFrontImg;

    @ApiModelProperty("银行开户证明正面")
    private String bankOpenFrontImg;

    @ApiModelProperty("总运输台数")
    private Integer carNum;

    @ApiModelProperty("总收入")
    private BigDecimal totalIncome;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;
}