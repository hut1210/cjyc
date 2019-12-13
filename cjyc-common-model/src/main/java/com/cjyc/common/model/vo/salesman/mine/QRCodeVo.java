package com.cjyc.common.model.vo.salesman.mine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
@Data
public class QRCodeVo implements Serializable {
    private static final long serialVersionUID = 465999743376429547L;
    @ApiModelProperty(value = "业务员id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long salesmanId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "二维码地址")
    private String qrCodeUrl;

}