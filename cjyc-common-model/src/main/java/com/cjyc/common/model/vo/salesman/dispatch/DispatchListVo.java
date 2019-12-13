package com.cjyc.common.model.vo.salesman.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 调度列表实体
 */

@Data
@ApiModel
public class DispatchListVo {
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "车系")
    private String model;
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "车辆编号")
    private String carNo;
    @ApiModelProperty(value = "提车日期")
    private Long pickDate;
    @ApiModelProperty(value = "订单-出发城市")
    private String orderStartCity;
    @ApiModelProperty(value = "订单-目的城市")
    private String orderEndCity;
    @ApiModelProperty(value = "运单标识")
    private Long wayBillId;
    @ApiModelProperty(value = "运单编号")
    private String wayBillNo;
    @ApiModelProperty(value = "提车方式")
    private String liftCarMode;
    @ApiModelProperty(hidden = true)
    private String trunkMode;
    @ApiModelProperty(value = "送车方式 0：未调度 1：已调度")
    private String deliveryMode;
    @ApiModelProperty(value = "提车状态 0：未调度 1：已调度")
    private String liftCarState;
    @ApiModelProperty(hidden = true)
    private String trunkState;
    @ApiModelProperty(value = "送车状态")
    private String deliveryState;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "干线调度节点列表")
    private List<String> trunkModeList;
    @ApiModelProperty(value = "干线调度节点状态列表 0：未调度 1：已调度")
    private List<String> trunkStateList;
    @ApiModelProperty(value = "品牌logo图片路径")
    private String logoImgPath;
}
