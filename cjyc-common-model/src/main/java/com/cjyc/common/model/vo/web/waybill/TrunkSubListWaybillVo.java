package com.cjyc.common.model.vo.web.waybill;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "司机", orderNum = "4")
    @ApiModelProperty("司机名称")
    private String driverName;
    @Excel(name = "司机电话", orderNum = "5")
    @ApiModelProperty("手机号")
    private String driverPhone;
    @Excel(name = "车牌号", orderNum = "6")
    @ApiModelProperty("车牌号")
    private String plateNo;
    @ApiModelProperty("车位总数")
    private Integer carryCarNum;
    @Excel(name = "动态车位", orderNum = "7")
    @ApiModelProperty("被占车位")
    private Integer occupiedCarNum;

}
