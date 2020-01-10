package com.cjyc.common.model.vo.web.order;

import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DispatchCarVo extends WaybillCar {

    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "型号")
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "状态：0待路由，5待提车调度，10待提车，12待自送交车，15提车中（待交车），25待干线调度<循环>（提车入库），35待干线提车<循环>，40干线中<循环>（待干线交车），45待配送调度（干线入库），50待配送提车，55配送中（待配送交车），70待自取提车，100已签收")
    private Integer orderCarState;

    @ApiModelProperty(value = "提车状态(调度状态)：1待调度，5已调度，7无需调度")
    private Integer pickState;
    @ApiModelProperty(value = "干线状态(调度状态)：1待调度，2节点调度，5已调度，7无需调度")
    private Integer trunkState;
    @ApiModelProperty(value = "送车状态(调度状态)：1待调度，5已调度，7无需调度")
    private Integer backState;

    @ApiModelProperty(value = "实际提车类型")
    private Integer pickType;
    @ApiModelProperty(value = "实际送车类型")
    private Integer backType;

}
