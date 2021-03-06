package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.driver.AppDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class AppCarrierVehicleDto extends AppDriverDto  {
    private static final long serialVersionUID = -345793247315486853L;

    @ApiModelProperty("车牌id")
    private Long vehicleId;

    @ApiModelProperty(value = "车牌号",required = true)
    @NotBlank(message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty(value = "车位数",required = true)
    @NotNull(message = "车位数不能为空")
    @Min(value = 1)
    private Integer defaultCarryNum;

    @ApiModelProperty(value = "司机id",required = true)
    private Long driverId;

    @ApiModelProperty(value = "司机姓名",required = true)
    private String realName;

    @ApiModelProperty(value = "司机手机号",required = true)
    private String phone;
}