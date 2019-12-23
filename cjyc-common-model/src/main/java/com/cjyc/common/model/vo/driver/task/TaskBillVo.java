package com.cjyc.common.model.vo.driver.task;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 已交付任务返回实体
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

    @ApiModelProperty(value = "任务单车辆id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long taskCarId;

    @ApiModelProperty(value = "任务状态：0待承接(弃)，5待装车，55运输中，100已完成，113已取消，115已拒接")
    private Integer taskState;

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "车辆数")
    private Integer carNum;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "运单总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "提车时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long expectStartTime;

    @ApiModelProperty(value = "接单时间 或 分配时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "交车时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long completeTime;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;

    public Long getWaybillId() {
        return waybillId == null ? 0 : waybillId;
    }
    public Long getTaskCarId() {
        return taskCarId == null ? 0 : taskCarId;
    }
    public Integer getTaskState() {
        return taskState == null ? -1 : taskState;
    }
    public String getWaybillNo() {
        return waybillNo == null ? "" : waybillNo;
    }
    public Integer getType() {
        return type == null ? -1 : type;
    }
    public BigDecimal getFreightFee() {
        return freightFee == null ? new BigDecimal(0) : freightFee;
    }
    public Long getCreateTime() {
        return createTime == null ? 0 : createTime;
    }
    public Long getTaskId() {
        return taskId == null ? 0 : taskId;
    }
    public Long getExpectStartTime() {
        return expectStartTime == null ? 0 : expectStartTime;
    }
    public Long getCompleteTime() {
        return completeTime == null ? 0 : completeTime;
    }
    public String getDriverName() {
        return driverName == null ? "" : driverName;
    }
    public String getDriverPhone() {
        return driverPhone == null ? "" : driverPhone;
    }
    public String getVehiclePlateNo() {
        return vehiclePlateNo == null ? "" : vehiclePlateNo;
    }
    public String getGuideLine() {
        return guideLine == null ? "" : guideLine;
    }
    public Integer getCarNum() {
        return carNum == null ? 0 : carNum;
    }
}
