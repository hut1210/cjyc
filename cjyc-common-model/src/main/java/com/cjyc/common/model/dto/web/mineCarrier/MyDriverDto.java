package com.cjyc.common.model.dto.web.mineCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class MyDriverDto extends MyVehicleDto implements Serializable {

    private static final long serialVersionUID = 8758237912016845960L;

    @ApiModelProperty("身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    @NotNull(message = "承运方式不能为空")
    private Integer mode;

    @ApiModelProperty("身份证正面")
    @NotBlank(message = "身份证正面不能为空")
    private String idCardFrontImg;

    @ApiModelProperty("身份证反面")
    @NotBlank(message = "身份证反面不能为空")
    private String idCardBackImg;
}