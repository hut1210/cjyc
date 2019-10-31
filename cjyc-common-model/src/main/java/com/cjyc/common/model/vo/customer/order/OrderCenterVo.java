package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCenterVo implements Serializable {
    private static final long serialVersionUID = -5418843717123216878L;
    @ApiModelProperty("订单编号")
    private String no;

    @ApiModelProperty(value = "出发城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "订单总价")
    private String totalFee;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "订单状态")
    private String state;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "车辆信息列表")
    private List<OrderCarCenterVo> orderCarCenterVos;

}