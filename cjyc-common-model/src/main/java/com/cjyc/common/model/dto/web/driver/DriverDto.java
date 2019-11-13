package com.cjyc.common.model.dto.web.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DriverDto implements Serializable {

    private static final long serialVersionUID = 4826078276921799480L;
    public interface SaveDriverDto {}
    public interface UpdateDriverDto {}

    @ApiModelProperty("司机id(driverId)")
    @NotNull(groups = {UpdateDriverDto.class},message = "司机id(driverId)不能为空")
    private Long driverId;

    @ApiModelProperty("承运商id(carrierId)")
    @NotNull(groups = {UpdateDriverDto.class},message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("当前登陆用户id(loginId)")
    @NotNull(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "当前登陆用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty("司机姓名")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "司机姓名不能为空")
    private String realName;

    @ApiModelProperty("司机手机号")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "司机手机号不能为空")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    @NotNull(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty("业务城市")
    @NotEmpty(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "业务类型不能为空")
    private List<String> codes;

    @ApiModelProperty("身份证号")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "司机身份证号不能为空")
    private String idCard;

    @ApiModelProperty("身份证正面")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "身份证正面不能为空")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "身份证反面不能为空")
    private String idCardBackImg;

    @ApiModelProperty("驾驶证正面")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "驾驶证正面不能为空")
    private String driverLicenceFrontImg;

    @ApiModelProperty("驾驶证反面")
    @NotBlank(groups = {SaveDriverDto.class,UpdateDriverDto.class},message = "驾驶证反面不能为空")
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