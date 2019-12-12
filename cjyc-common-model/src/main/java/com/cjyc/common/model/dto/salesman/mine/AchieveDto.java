package com.cjyc.common.model.dto.salesman.mine;

import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AchieveDto extends BaseSalesDto {
    @ApiModelProperty(value = "日期,格式为yyyy-MM-dd",required = true)
    @NotBlank(message = "日期不能为空")
    private String date;
}