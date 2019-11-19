package com.cjyc.common.model.dto.web.task;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CrTaskDto extends BasePageDto {

    @ApiModelProperty(value = "任务编号")
    private String userId;

    @ApiModelProperty(value = "任务编号")
    private String taskNo;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;

}
