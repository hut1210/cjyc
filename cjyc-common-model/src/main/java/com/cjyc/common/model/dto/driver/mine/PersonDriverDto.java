package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class PersonDriverDto implements Serializable {
    private static final long serialVersionUID = 8361099293402284327L;

    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "userId不能为空",required = true)
    @NotNull(message = "userId不能为空")
    private Long userId;

    @ApiModelProperty(value = "司机姓名",required = true)
    @NotBlank(message = "司机姓名不能为空")
    @Pattern(regexp = RegexConstant.NAME,message = "请输入合法姓名")
    private String realName;

    @ApiModelProperty(value = "司机手机号",required = true)
    @NotBlank(message = "司机手机号不能为空")
    @Pattern(regexp = RegexConstant.REGEX_MOBILE_EXACT_LATEST,message = "电话号码格式不对")
    private String phone;

    @ApiModelProperty(value = "身份证号",required = true)
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = RegexConstant.REGEX_ID_CARD18,message = "身份证号码格式不对")
    private String idCard;

    @ApiModelProperty(value = "车辆id",required = true)
    @NotNull(message = "车辆id不能为空")
    private Long vehicleId;

    @ApiModelProperty(value = "车牌号",required = true)
    @NotBlank(message = "车牌号不能为空")
    @Pattern(regexp = RegexConstant.PLATE_NO,message = "车牌号格式不对")
    private String plateNo;

    @ApiModelProperty(value = "车位数",required = true)
    @NotNull(message = "车位数不能为空")
    private Integer defaultCarryNum;

    @ApiModelProperty(value = "承运方式：2 : 代驾  3 : 干线   4：拖车",required = true)
    @NotNull(message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty(value = "身份证正面",required = true)
    @NotBlank(message = "身份证正面不能为空")
    private String idCardFrontImg;
    @ApiModelProperty(value = "身份证反面",required = true)
    @NotBlank(message = "身份证反面不能为空")
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