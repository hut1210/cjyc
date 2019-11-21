package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class FreeVehicleDto implements Serializable {

    private static final long serialVersionUID = 5883906486927136906L;
    @ApiModelProperty("承运商id(carrierId)")
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商/管理员(app中)")
    @NotNull
    private Integer type;
}