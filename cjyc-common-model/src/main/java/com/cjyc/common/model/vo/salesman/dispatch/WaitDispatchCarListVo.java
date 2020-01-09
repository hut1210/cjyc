package com.cjyc.common.model.vo.salesman.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WaitDispatchCarListVo {

    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "状态：0待路由，5待提车调度，10待提车，12待自送交车，15提车中（待交车），25待干线调度<循环>（提车入库），35待干线提车<循环>，40干线中<循环>（待干线交车），45待配送调度（干线入库），50待配送提车，55配送中（待配送交车），70待自取提车，100已签收")
    private Integer state;

    @ApiModelProperty(value = "车辆编号")
    private String orderCarNo;
    @ApiModelProperty(value = "车辆ID")
    private String orderCarId;
    @ApiModelProperty(value = "提车方式: 1自送，2代驾上门，3拖车上门，4物流上门")
    private String pickType;
    @ApiModelProperty(value = "提车状态：1待调度，5已调度，7无需调度")
    private String pickState;
    @ApiModelProperty(value = "提车节点")
    private String pickNodes;
    @ApiModelProperty(value = "干线节点")
    private String trunkNodes;
    @ApiModelProperty(value = "送车节点")
    private String backNodes;
    @ApiModelProperty(value = "干线状态：1待调度，2节点调度，5已调度，7无需调度")
    private String trunkState;
    @ApiModelProperty(value = "送车方式: 1自送，2代驾上门，3拖车上门，4物流上门")
    private String backType;
    @ApiModelProperty(value = "送车状态：1待调度，5已调度，7无需调度")
    private String backState;

    @ApiModelProperty(value = "品牌logo图片路径")
    private String logoImgPath;

    private String startCity;
    private String startCityCode;
    private String startBelongStoreId;
    private String endCity;
    private String endCityCode;
    private String endBelongStoreId;
    private String expectStartDate;
}
