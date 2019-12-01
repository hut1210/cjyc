package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class AppItemDto implements Serializable {
    private static final long serialVersionUID = 7595434258738045862L;
    @ApiModelProperty(value = "用户端 item传system_picture_customer,司机端item传system_picture_driver,业务员端item传system_picture_sale",required = true)
    @NotBlank(message = "字典项熊为空")
    private String systemPicture;
}