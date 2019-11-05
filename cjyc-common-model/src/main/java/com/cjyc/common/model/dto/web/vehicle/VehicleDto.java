package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class VehicleDto implements Serializable {

    @ApiModelProperty("当前登陆用户userId")
    @NotNull(message = "当前登陆用户userId不能为空")
    private Long userId;

    @ApiModelProperty("车牌号")
    @NotBlank(message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty("车位数")
    @NotNull(message = "车位数不能为空")
    private Integer defaultCarryNum;

}