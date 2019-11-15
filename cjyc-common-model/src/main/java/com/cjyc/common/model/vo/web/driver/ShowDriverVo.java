package com.cjyc.common.model.vo.web.driver;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class ShowDriverVo implements Serializable {

    private static final long serialVersionUID = -4902283401893145744L;

    @ApiModelProperty("承运商id(carrierId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("司机id(driverId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机姓名")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("司机业务范围")
    private List<LinkedHashMap> mapCodes;

    @ApiModelProperty("持卡人")
    private String cardName;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNo;

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

    @ApiModelProperty("行驶证反面")
    private String travelLicenceBackImg;

    @ApiModelProperty("营运证正面")
    private String taxiLicenceFrontImg;

    @ApiModelProperty("营运证反面")
    private String taxiLicenceBackImg;

    @ApiModelProperty("从业证正面")
    private String qualifiCertFrontImg;

    @ApiModelProperty("从业证反面")
    private String qualifiCertBackImg;

    @ApiModelProperty("车辆id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private String defaultCarryNum;
}