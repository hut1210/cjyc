package com.cjyc.common.model.vo.salesman.dispatch;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WaitDispatchCarListVo {

    @ApiModelProperty(value = "订单ID")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private Integer isMove;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private Integer isNew;
    @ApiModelProperty(value = "状态：0待路由，5待提车调度，10待提车，12待自送交车，15提车中（待交车），25待干线调度<循环>（提车入库），35待干线提车<循环>，40干线中<循环>（待干线交车），45待配送调度（干线入库），50待配送提车，55配送中（待配送交车），70待自取提车，100已签收")
    private Integer state;
    @ApiModelProperty(value = "车辆应收提车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal pickFee;
    @ApiModelProperty(value = "车辆应收送车费")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal backFee;


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
