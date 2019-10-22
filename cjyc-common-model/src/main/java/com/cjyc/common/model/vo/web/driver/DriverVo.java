package com.cjyc.common.model.vo.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class DriverVo implements Serializable {

    @ApiModelProperty("司机id")
    private Long id;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：0 ：代驾 1：干线司机  2：拖车司机 4全支持")
    private String mode;

    @ApiModelProperty("结算方式：1时付，2账期")
    private String settleType;

    @ApiModelProperty("账期/天")
    private Integer settlePeriod;

    @ApiModelProperty("是否开发票 0：否  1：是")
    private String isInvoice;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    private String idCardBackImg;

    @ApiModelProperty("驾驶证正面")
    private String driverLicenceFrontImg;

    @ApiModelProperty("行驶证正面")
    private String travelLicenceFrontImg;

    @ApiModelProperty("从业证正面")
    private String qualifiCertFrontImg;

    @ApiModelProperty("营运证正面")
    private String taxiLicenceFrontImg;

    @ApiModelProperty("账号来源：1App注册，2Applet注册，3业务员创建，4承运商管理员创建，11掌控接口，12otm接口")
    private String source;

    @ApiModelProperty("营运状态：0营运中，1停运中")
    private String businessState;

    @ApiModelProperty("总运量(台)")
    private Integer carNum;

    @ApiModelProperty("总收入")
    private String totalIncome;

    @ApiModelProperty("状态：0待审核，2审核通过，4已驳回(审核不通过)，7已冻结")
    private String state;
}