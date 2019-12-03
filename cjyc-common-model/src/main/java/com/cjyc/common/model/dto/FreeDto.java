package com.cjyc.common.model.dto;

import com.cjyc.common.model.dto.driver.AppDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class FreeDto extends AppDriverDto {

    private static final long serialVersionUID = 5883906486927136906L;
    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("司机名字")
    private String realName;

    @ApiModelProperty("承运商id,不需要传")
    private Long carrierId;
}