package com.cjyc.common.model.vo.salesman.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 任务信息
 * @Author Liu Xing Xiang
 * @Date 2020/4/10 11:45
 **/
@Data
public class TaskInfo implements Serializable {
    private static final long serialVersionUID = 7039780890261086872L;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "司机ID")
    private Long driverId;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
}
