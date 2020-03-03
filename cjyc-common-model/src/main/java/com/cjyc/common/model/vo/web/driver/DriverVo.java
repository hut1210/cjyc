package com.cjyc.common.model.vo.web.driver;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DriverVo implements Serializable {

    private static final long serialVersionUID = 873178028316877563L;
    @ApiModelProperty(value = "承运商id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "司机id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty(value = "司机姓名")
    private String realName;

    @ApiModelProperty(value = "司机手机号")
    private String phone;

    @ApiModelProperty(value = "承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty(value = "结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "持卡人")
    private String cardName;

    @ApiModelProperty(value = "开户行")
    private String bankName;

    @ApiModelProperty(value = "银行卡号")
    private String cardNo;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;

    @ApiModelProperty(value = "驾驶证正面")
    private String driverLicenceFrontImg;

    @ApiModelProperty(value = "驾驶证反面")
    private String driverLicenceBackImg;

    @ApiModelProperty(value = "行驶证正面")
    private String travelLicenceFrontImg;

    @ApiModelProperty(value = "从业证正面")
    private String qualifiCertFrontImg;

    @ApiModelProperty(value = "营运证正面")
    private String taxiLicenceFrontImg;

    @ApiModelProperty(value = "账号来源：1App注册，2Applet注册，3业务员创建，4承运商管理员创建，11掌控接口，12otm接口")
    private Integer source;

    @ApiModelProperty(value = "运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;

    @ApiModelProperty(value = "营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;

    @ApiModelProperty(value = "总运量(台)")
    private Integer carNum;

    @ApiModelProperty(value = "总收入")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalIncome;

    @ApiModelProperty(value = "状态：0待审核，1:审核中 2已审核，4取消，5冻结  7已驳回(审核拒绝)，9已停用")
    private Integer state;

    @ApiModelProperty(value = "最后操作时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long operatTime;

    @ApiModelProperty(value = "操作人")
    private String operator;
}