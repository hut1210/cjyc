package com.cjyc.common.model.vo.salesman.task;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DateLongSerizlizer;
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
public class TaskWaybillVo implements Serializable {
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

    @ApiModelProperty(value = "运单编号")
    private String waybillNo;

    @ApiModelProperty(value = "任务状态：5待提车，55待交车，100已交付")
    private Integer taskState;

    @ApiModelProperty(value = "库存状态：15待出库，90待入库，45已出库，100已入库")
    private Integer storageState;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "运单总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "接单时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "交车时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long completeTime;

    @ApiModelProperty(value = "入库时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long unloadTime;

    @ApiModelProperty(value = "出库时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long loadTime;

    @ApiModelProperty(value = "车辆数")
    private Integer carNum;

    public Long getWaybillId() {
        return waybillId == null ? 0 : waybillId;
    }
    public Long getTaskId() {
        return taskId == null ? 0 : taskId;
    }
    public Long getTaskCarId() {
        return taskCarId == null ? 0 : taskCarId;
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
    public Integer getCarNum() {
        return carNum == null ? 0 : carNum;
    }
    public String getGuideLine() {
        return guideLine == null ? "" : guideLine;
    }
    public Long getUnloadTime() {
        return unloadTime ==null ? 0 : unloadTime;
    }
    public Long getLoadTime() {
        return loadTime ==null ? 0 : loadTime;
    }
    public Integer getTaskState() {
        return taskState == null ? -1 : taskState;
    }
    public Integer getStorageState() {
        return storageState == null ? -1 : storageState;
    }
    public Long getCompleteTime() {
        return completeTime ==null ? 0 : completeTime;
    }
}
