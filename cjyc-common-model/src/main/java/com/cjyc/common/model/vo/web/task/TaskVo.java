package com.cjyc.common.model.vo.web.task;

import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaskVo extends Task {
    @ApiModelProperty("车辆列表")
    private List<WaybillCarVo> list;
}
