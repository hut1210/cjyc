package com.cjyc.common.model.vo.driver.task;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 任务详情
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 11:34
 **/
@Data
public class TaskDetailVo implements Serializable {
    private static final long serialVersionUID = -6460478260449465114L;
    @ApiModelProperty(value = "运单状态(待分配明细使用)：0待分配承运商，15待承运商承接任务，20已承接, 55运输中，100已完成，111超时关闭，113已取消，115已拒接；" +
            "运单车辆状态(已分配的明细使用)：0待指派，5待装车，15待出库(业务中心)，45已装车，90待入库(业务中心）, 100已交付，113已取消")
    private Integer state;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "接单时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long createTime;

    @ApiModelProperty(value = "交车时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long completeTime;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "运单总运费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal freightFee;

    @ApiModelProperty(value = "承运商ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty(value = "车辆信息列表")
    private List<CarDetailVo> carDetailVoList;

    public Integer getState() {
        return state == null ? -1 : state;
    }
    public Long getCompleteTime() {
        return completeTime ==null ? 0 : completeTime;
    }
    public Long getCreateTime() {
        return createTime == null ? 0 : createTime;
    }
    public Long getCarrierId() {
        return carrierId == null ? 0 : carrierId;
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
}
