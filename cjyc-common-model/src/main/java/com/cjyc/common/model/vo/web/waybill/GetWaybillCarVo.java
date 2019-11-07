package com.cjyc.common.model.vo.web.waybill;

import com.cjyc.common.model.entity.WaybillCar;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GetWaybillCarVo extends WaybillCar {

    @ApiModelProperty(value = "状态")
    private String outterState;

    @ApiModelProperty(value = "承运商ID")
    private Long carrierId;
    @ApiModelProperty(value = "承运商类型：0承运商，1业务员，2客户自己")
    private Integer carrierType;
    private String carrierName;

    @ApiModelProperty(value = "司机名称")
    private String driverName;
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "司机ID(userId)")
    private Long driverId;
    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;


}
