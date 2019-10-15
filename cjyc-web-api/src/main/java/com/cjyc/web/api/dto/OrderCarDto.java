package com.cjyc.web.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @auther litan
 * @description: 订单车辆入参实体类
 * @date:2019/10/8
 */
@Data
public class OrderCarDto implements Serializable {

    @ApiModelProperty(value = "品牌",required = true)
    private String brand;
    @ApiModelProperty(value = "型号",required = true)
    private String model;
    @ApiModelProperty(value = "车牌号")
    private String plateNo;//车牌号
    @ApiModelProperty(value = "vin码")
    private String vin;
    @ApiModelProperty(value = "是否能动 0-否 1-是")
    private int isMove;
    @ApiModelProperty(value = "是否新车 0-否 1-是")
    private int isNew;
    @ApiModelProperty(value = "估值/分",required = true)
    private int valuation;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "保费 单位：分")
    private int insuranceFee;
    @ApiModelProperty(value = "保额 单位：分")
    private int insuranceCoverageAmount;


}
