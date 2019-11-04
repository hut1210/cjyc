package com.cjyc.common.model.dto.web.waybill;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TrunkListWaybillDto extends BasePageDto {

    @ApiModelProperty(value = "运单单号", required = true)
    private String waybillNo;
    @ApiModelProperty(value = "指导线路")
    private String guideLine;
    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "起始创建时间")
    private Long beginCreateTime;
    @ApiModelProperty(value = "截止创建时间")
    private Long endCreateTime;



    @ApiModelProperty(value = "司机名称")
    private String driverName;
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "运力车牌号")
    private String vehiclePlateNo;
}
