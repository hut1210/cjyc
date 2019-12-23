package com.cjyc.common.model.vo.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CrWaybillVo {
    @ApiModelProperty(value = "ID")
    private Long waybillId;
    @ApiModelProperty(value = "运单编号")
    private String waybillNo;
    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer waybillType;
    @ApiModelProperty(value = "指导线路")
    private String guideLine;
    @ApiModelProperty(value = "车数量")
    private Integer carNum;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "调度人")
    private String createUser;
    @ApiModelProperty(value = "已分配数量")
    private String hasAllottedNum;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "运单状态：0待分配承运商（竞抢），15待承运商承接任务，55运输中，100已完成，111超时关闭，113已取消，115已拒接")
    private Integer state;

}
