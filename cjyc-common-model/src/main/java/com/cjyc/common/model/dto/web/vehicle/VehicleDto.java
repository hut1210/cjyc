package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class VehicleDto implements Serializable {

    @ApiModelProperty(value = "当前登陆用户id(loginId)",required = true)
    @NotNull(message = "当前登陆用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "车牌号",required = true)
    @NotBlank(message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty(value = "车位数",required = true)
    @NotNull(message = "车位数不能为空")
    private Integer defaultCarryNum;

}