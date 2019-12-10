package com.cjyc.common.model.vo.salesman.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class StockTaskVo implements Serializable {
    private static final long serialVersionUID = 5898205613471851417L;
    @ApiModelProperty(value = "提车任务单号")
    private String taskNo;
    @ApiModelProperty(value = "运单类型：1提车运单，2干线运单，3送车运单")
    private Integer type;
    @ApiModelProperty(value = "提车城市")
    private String startCity;
    @ApiModelProperty(value = "交车城市")
    private String endCity;

    @ApiModelProperty(value = "承运商名称")
    private String carrierName;
    @ApiModelProperty(value = "司机名称")
    private String driverName;
    @ApiModelProperty(value = "车牌号")
    private String vehiclePlateNo;
    @ApiModelProperty(value = "司机手机号")
    private String driverPhone;

    @ApiModelProperty(value = "提车联系人")
    private String loadLinkName;
    @ApiModelProperty(value = "提车联系人电话")
    private String loadLinkPhone;
    @ApiModelProperty(value = "提车地址")
    private String startAddress;
    @ApiModelProperty(value = "收车联系人")
    private String unloadLinkName;
    @ApiModelProperty(value = "收车联系人电话")
    private String unloadLinkPhone;
    @ApiModelProperty(value = "卸/交车地址")
    private String endAddress;
}