package com.cjyc.common.model.vo.web.task;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
@Data
public class DriverCarCountVo implements Serializable {
    private static final long serialVersionUID = 8068240596162913108L;
    @ApiModelProperty("任务id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long taskId;

    @ApiModelProperty("司机id")
    private Long driverId;

    @ApiModelProperty("订单车辆id")
    private Long orderCarId;
}