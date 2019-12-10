package com.cjyc.common.model.dto.salesman.mine;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StockCarDto extends BasePageDto {
    @ApiModelProperty(value = "登陆业务员id",required = true)
    @NotNull(message = "登陆业务员id不能为空")
    private Long loginId;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "起始地")
    private String startCity;
    @ApiModelProperty(value = "目的地")
    private String endCity;
    @ApiModelProperty(value = "提车起始日期")
    private Long startTime;
    @ApiModelProperty(value = "提车结束日期")
    private Long endTime;
}