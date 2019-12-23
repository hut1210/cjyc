package com.cjyc.common.model.vo.web.task;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 我的任务列表响应对象
 * @Author Liu Xing Xiang
 * @Date 2019/12/20 13:55
 **/
@Data
public class TaskPageVo implements Serializable {
    private static final long serialVersionUID = -3319496238205089705L;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "任务编号")
    private String taskNo;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;

    @ApiModelProperty(value = "vin码")
    private String vin;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "任务单ID")
    private Long taskId;

    @ApiModelProperty(value = "任务单车辆ID")
    private Long taskCarId;

    @ApiModelProperty(value = "运单车辆状态：0待指派，2已指派(弃)，5待装车，15待装车确认，45已装车，" +
            "90待收车确认, 100确认收车, 105待重连，113已取消，120已重连")
    private Integer waybillCarState;
}
