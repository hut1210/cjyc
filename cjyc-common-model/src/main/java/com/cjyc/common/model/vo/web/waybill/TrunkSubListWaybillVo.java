package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Waybill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkSubListWaybillVo extends Waybill {


    @ApiModelProperty("任务编号")
    private String taskNo;
    @ApiModelProperty("任务车辆数量")
    private Integer taskCarNum;
    @ApiModelProperty("司机ID")
    private Long driverId;
    @ApiModelProperty("司机名称")
    private String driverName;
    @ApiModelProperty("手机号")
    private String driverPhone;
    @ApiModelProperty("车牌号")
    private String plateNo;
    @ApiModelProperty("车位总数")
    private Integer carryCarNum;
    @ApiModelProperty("被占车位")
    private Integer occupiedCarNum;

}
