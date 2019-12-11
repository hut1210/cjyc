package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CommonDto extends BasePageDto implements Serializable {

    @ApiModelProperty(value = "客户id",required = true)
    private Long loginId;

}