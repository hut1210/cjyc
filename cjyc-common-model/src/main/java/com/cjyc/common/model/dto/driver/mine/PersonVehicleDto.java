package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.constant.RegexConstant;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
public class PersonVehicleDto extends BaseDriverDto {
    private static final long serialVersionUID = -1081460106637979401L;

    @ApiModelProperty("车牌号id")
    private Long vehicleId;

    @ApiModelProperty(value = "车牌号",required = true)
    @NotBlank(message = "车牌号不能为空")
    @Pattern(regexp = RegexConstant.PLATE_NO,message = "车牌号格式不对")
    private String plateNo;

    @ApiModelProperty(value = "车位数",required = true)
    @Null(message = "车位数不能为空")
    private Integer defaultCarryNum;
}