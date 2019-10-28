package com.cjyc.common.model.vo.web.waybill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class HistoryListWaybillVo {

    @ApiModelProperty("运单号")
    private String waybillNo;
    @ApiModelProperty("起始城市")
    private String startCity;
    @ApiModelProperty("起始城市编码")
    private String startCityCode;
    @ApiModelProperty("目的城市")
    private String endCity;
    @ApiModelProperty("目的城市编码")
    private String endCityCode;
    @ApiModelProperty("起始地址")
    private String startAddress;
    @ApiModelProperty("目的地址")
    private String endAddress;
    @ApiModelProperty("承运商ID")
    private String carrierId;
    @ApiModelProperty("承运商")
    private String carrierName;
    @ApiModelProperty("司机ID")
    private String driverId;
    @ApiModelProperty("司机名称")
    private String driverName;
    @ApiModelProperty("司机电话")
    private String driverPhone;
    @ApiModelProperty("提车联系人")
    private String pickContactName;
    @ApiModelProperty("提车联系人电话")
    private String pickContactPhone;
    @ApiModelProperty("指定提车日期")
    private String expectStartTime;
    @ApiModelProperty("预计到达日期")
    private String expectEndTime;
    @ApiModelProperty("实际装车时间")
    private String loadTime;
    @ApiModelProperty("实际卸车时间")
    private String unloadTime;
}
