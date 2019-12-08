package com.cjyc.common.model.vo.web.driver;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DriverVo implements Serializable {

    private static final long serialVersionUID = 873178028316877563L;
    @ApiModelProperty("承运商id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("司机id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty("结算方式：0时付，1账期")
    private Integer settleType;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("持卡人")
    private String cardName;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("身份证正面")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    private String idCardBackImg;

    @ApiModelProperty("驾驶证正面")
    private String driverLicenceFrontImg;

    @ApiModelProperty("驾驶证反面")
    private String driverLicenceBackImg;

    @ApiModelProperty("行驶证正面")
    private String travelLicenceFrontImg;

    @ApiModelProperty("从业证正面")
    private String qualifiCertFrontImg;

    @ApiModelProperty("营运证正面")
    private String taxiLicenceFrontImg;

    @ApiModelProperty("账号来源：1App注册，2Applet注册，3业务员创建，4承运商管理员创建，11掌控接口，12otm接口")
    private Integer source;

    @ApiModelProperty("运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;

    @ApiModelProperty("总运量(台)")
    private Integer carNum;

    @ApiModelProperty("总收入")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalIncome;

    @ApiModelProperty("状态：0待审核，2审核通过，4已驳回(审核不通过)，7已冻结")
    private Integer state;

    @ApiModelProperty("最后操作时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long operatTime;

    @ApiModelProperty("操作人")
    private String operator;
}