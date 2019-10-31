package com.cjyc.common.model.vo.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCenterVo implements Serializable {
    private static final long serialVersionUID = -9089194226578465711L;
    @ApiModelProperty("订单编号")
    private String no;

    @ApiModelProperty(value = "出发城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "订单总价")
    private String totalFee;

    @ApiModelProperty(value = "车辆总数")
    private String carNum;

    @ApiModelProperty(value = "订单状态")
    private String state;

    @ApiModelProperty(value = "车辆信息")
    private List<OrderCarCenterVo> orderCarCenterVos;

}