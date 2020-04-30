package com.cjyc.common.model.dto.web.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DriverLoginCountExportDto {
    @ApiModelProperty("开始时间")
    private Long startDate;
    @ApiModelProperty("结束时间")
    private Long endDate;
    @ApiModelProperty("查询类型: 0自定义接口信息，1Web登录信息，2业务员端登录信息，3司机端登录信息，4用户端登录信息，")
    private int queryType = 0;
    @ApiModelProperty("接口名称: queryType为0时有效")
    private String interfaceName;

    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
}
