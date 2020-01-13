package com.cjyc.common.model.dto.salesman.dispatch;

import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WaitCountDto extends BaseSalesDto {
    @NotNull(message = "调度类型不能为空")
    @ApiModelProperty(value = "调度类型 0：全部 1：提车调度 2：干线调度 3：送车调度")
    private Integer dispatchType;
}
