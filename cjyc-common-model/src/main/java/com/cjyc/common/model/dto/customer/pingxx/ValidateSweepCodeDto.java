package com.cjyc.common.model.dto.customer.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ValidateSweepCodeDto {

    @ApiModelProperty(value = "用户ID",required = true)
    private Long loginId;

    @ApiModelProperty(value = "任务Id")
    private Long taskId;

    @NotEmpty(message = "任务车辆ID")
    @ApiModelProperty(value = "任务车辆ID")
    private List<String> taskCarIdList;

    @ApiModelProperty(value = "收车码",required = true)
    @NotBlank(message = "收车码")
    private String code;

}
