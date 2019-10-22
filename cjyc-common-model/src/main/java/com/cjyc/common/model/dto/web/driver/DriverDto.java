package com.cjyc.common.model.dto.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
public class DriverDto implements Serializable {

    public interface SaveDriverDto {
    }

    @ApiModelProperty("登陆人id")
    @NotBlank(groups = {DriverDto.SaveDriverDto.class},message = "登录人id不能为空")
    private String userId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：0 ：代驾 1：干线司机  2：拖车司机 4全支持")
    private Integer mode;

    @ApiModelProperty("全国code")
    private String countryCode;

    @ApiModelProperty("大区code")
    private List<String> largeAreaCode;

    @ApiModelProperty("省/直辖市code")
    private List<String> provinceCode;

    @ApiModelProperty("市code")
    private List<String> cityCode;

    @ApiModelProperty("区/县code")
    private List<String> areaCode;

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

    @ApiModelProperty("车辆信息id")
    private String id;

}