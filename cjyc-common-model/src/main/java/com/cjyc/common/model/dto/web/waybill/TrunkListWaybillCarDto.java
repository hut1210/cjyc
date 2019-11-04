package com.cjyc.common.model.dto.web.waybill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkListWaybillCarDto {

    @ApiModelProperty(value = "运单单号", required = true)
    private String waybillNo;
    @ApiModelProperty(value = "指导线路")
    private String guideLine;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;



    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;
}
