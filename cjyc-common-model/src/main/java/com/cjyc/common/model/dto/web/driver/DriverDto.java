package com.cjyc.common.model.dto.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DriverDto implements Serializable {

    public interface SaveDriverDto {
    }

    @ApiModelProperty("司机id")
    private Long id;

    @ApiModelProperty("登陆用户userId")
    private Long userId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式/司机类型：0 ：代驾 1：干线司机  2：拖车司机 4全支持")
    private Integer mode;

    @ApiModelProperty("业务类型")
    private List<String> codes;

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

    @ApiModelProperty("车辆信息vehicleId")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

}