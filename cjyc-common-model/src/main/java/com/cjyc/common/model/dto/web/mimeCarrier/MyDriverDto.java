package com.cjyc.common.model.dto.web.mimeCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class MyDriverDto implements Serializable {

    private static final long serialVersionUID = 8758237912016845960L;

    @ApiModelProperty("承运商id(carrierId)")
    @NotNull(message = "承运商id(carrierId)不能为空")
    private Long carrierId;

    @ApiModelProperty("司机真实姓名")
    @NotBlank(message = "司机真实姓名不能为空")
    private String realName;

    @ApiModelProperty("司机手机号")
    @NotBlank(message = "司机手机号不能为空")
    private String phone;

    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    @NotNull(message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty("车辆id(vehicleId)")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty("身份证正面")
    @NotNull(message = "身份证正面不能为空")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    @NotNull(message = "身份证反面不能为空")
    private String idCardBackImg;
}