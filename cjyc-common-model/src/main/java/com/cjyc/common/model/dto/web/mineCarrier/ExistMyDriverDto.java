package com.cjyc.common.model.dto.web.mineCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExistMyDriverDto implements Serializable {
    private static final long serialVersionUID = 5144426999921334864L;

    @ApiModelProperty("登陆系统用户id(loginId)")
    private Long loginId;

    @ApiModelProperty("承运商id(carrierId)")
    private Long carrierId;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;
}