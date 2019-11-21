package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class CarrierDriverDto extends CarrierVehicleDto implements Serializable {

    private static final long serialVersionUID = 8758237912016845960L;

    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @ApiModelProperty("身份证正面")
    @NotBlank(message = "身份证正面不能为空")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    @NotBlank(message = "身份证反面不能为空")
    private String idCardBackImg;
}