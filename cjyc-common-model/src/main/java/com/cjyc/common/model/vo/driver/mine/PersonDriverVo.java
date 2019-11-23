package com.cjyc.common.model.vo.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class PersonDriverVo implements Serializable {
    private static final long serialVersionUID = 2046690065713438041L;
    @ApiModelProperty(value = "司机姓名")
    private String realName;

    @ApiModelProperty(value = "司机手机号")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty("车辆id")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty(value = "承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty(value = "身份证正面")
    private String idCardFrontImg;
    @ApiModelProperty(value = "身份证反面")
    private String idCardBackImg;
    @ApiModelProperty("驾驶证")
    private String driverLicenceFrontImg;
    @ApiModelProperty("从业资格证")
    private String qualifiCertFrontImg;
    @ApiModelProperty("营运证")
    private String taxiLicenceFrontImg;
    @ApiModelProperty("行驶证")
    private String travelLicenceFrontImg;
}