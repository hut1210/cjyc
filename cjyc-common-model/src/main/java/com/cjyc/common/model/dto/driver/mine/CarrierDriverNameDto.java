package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.driver.AppDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CarrierDriverNameDto extends AppDriverDto {
    private static final long serialVersionUID = 3522955250907832783L;

    @ApiModelProperty("司机姓名")
    private String realName;
}