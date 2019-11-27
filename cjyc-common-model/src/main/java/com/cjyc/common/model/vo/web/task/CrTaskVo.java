package com.cjyc.common.model.vo.web.task;

import com.cjyc.common.model.entity.Task;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CrTaskVo extends Task {
    @ApiModelProperty(value = "车数量")
    private Integer waybillCarNum;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "运单类型")
    private String waybillType;
    @ApiModelProperty(value = "非空车位")
    private String occupiedCarNum;
    @ApiModelProperty(value = "承运数")
    private String carryCarNum;


}
