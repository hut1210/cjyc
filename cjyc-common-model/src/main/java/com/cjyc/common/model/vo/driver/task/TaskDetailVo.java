package com.cjyc.common.model.vo.driver.task;

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
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "运单状态：0待分配承运商（竞抢），15待承运商承接任务，55运输中，100已完成，111超时关闭，113已取消，115已拒接")
    private Integer state;

    @ApiModelProperty(value = "运单编号")
    private String no;

    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;

    @ApiModelProperty(value = "接单时间")
    private Long createTime;

    @ApiModelProperty(value = "指导线路")
    private String guideLine;

    @ApiModelProperty(value = "运单总运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;

    @ApiModelProperty(value = "车辆信息列表")
    private List<CarDetailVo> carDetailVoList;
}
