package com.cjyc.common.model.vo.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WaitDispatchCarListVo {
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "车系")
    private String model;
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @ApiModelProperty(value = "车辆ID")
    private String orderCarId;
    @ApiModelProperty(value = "提车日期")
    private Long pickDate;
    @ApiModelProperty(value = "订单-出发城市")
    private String orderStartCity;
    @ApiModelProperty(value = "订单-目的城市")
    private String orderEndCity;
    @ApiModelProperty(value = "提车方式: 1自送，2代驾上门，3拖车上门，4物流上门")
    private String pickType;
    @ApiModelProperty(value = "提车状态：1待调度，5已调度，7无需调度")
    private String pickState;
    @ApiModelProperty(value = "节点")
    private List<String> nodes;
    @ApiModelProperty(value = "干线状态：1待调度，2节点调度，5已调度，7无需调度")
    private String trunkState;
    @ApiModelProperty(value = "送车方式: 1自送，2代驾上门，3拖车上门，4物流上门")
    private String backType;
    @ApiModelProperty(value = "送车状态：1待调度，5已调度，7无需调度")
    private String backState;

    @ApiModelProperty(value = "品牌logo图片路径")
    private String logoImgPath;
}
