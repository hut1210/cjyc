package com.cjyc.common.model.vo.web.task;

import com.cjyc.common.model.entity.Task;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListByWaybillTaskVo extends Task {

    @ApiModelProperty("总车位数")
    private Integer carryCarNum;
    @ApiModelProperty("已占用车位数")
    private Integer occupiedCarNum;
}
