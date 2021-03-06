package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.Waybill;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkMainListWaybillVo extends Waybill {
    @ApiModelProperty("外部状态")
    private String outterState;

    @ApiModelProperty("编号")
    private String wtNo;
    @ApiModelProperty("司机ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long driverId;
    @ApiModelProperty("司机名称")
    private String driverName;
    @ApiModelProperty("手机号")
    private String driverPhone;
    @ApiModelProperty("车牌号")
    private String vehiclePlateNo;
    @ApiModelProperty("车位总数")
    private String carryCarNum;
    @ApiModelProperty("被占车位")
    private String occupiedCarNum;

}
