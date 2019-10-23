package com.cjyc.common.model.dto.web.carrier;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class SeleVehicleDto extends BasePageDto implements Serializable {

    public interface SelectVehicleDto {
    }

    @ApiModelProperty("承运商id")
    @NotNull(groups = {SelectVehicleDto.class},message = "承运商id不能为空")
    private Long id;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机姓名")
    private String phone;
}