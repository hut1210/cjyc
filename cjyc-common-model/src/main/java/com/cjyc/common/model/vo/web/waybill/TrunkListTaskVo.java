package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Task;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkListTaskVo extends Task {

    @ApiModelProperty(value = "承运数")
    private Integer carryCarNum;

    @ApiModelProperty(value = "空车位")
    private Integer emptyCarNum;


}
