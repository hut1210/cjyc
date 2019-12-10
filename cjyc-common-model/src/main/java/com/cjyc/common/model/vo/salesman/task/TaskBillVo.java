package com.cjyc.common.model.vo.salesman.task;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 任务单返回实体
 * @Author Liu Xing Xiang
 * @Date 2019/11/19 11:34
 **/
@Data
public class TaskBillVo implements Serializable {
    private static final long serialVersionUID = -5688740696634356580L;
    @ApiModelProperty(value = "运单id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long waybillId;

    @ApiModelProperty(value = "任务单id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long taskId;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "任务状态：5待提车，55待交车")
    private Integer taskState;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "运单总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "接单时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "车辆数")
    private Integer carNum;
}
