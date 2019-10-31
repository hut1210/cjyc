package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
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

    @ApiModelProperty(value = "车辆信息列表")
    private List<OrderCarCenterVo> orderCarCenterVos;

    public String getNo() {
        return no == null ? "" : no;
    }

    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }

    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }

    public String getState() {
        return state == null ? "-1" : state;
    }

    public String getTotalFee() {
        return totalFee == null ? "0.00" : totalFee;
    }

    public Integer getCarNum() {
        return carNum == null ? 0 : carNum;
    }

    public List<OrderCarCenterVo> getOrderCarCenterVos() {
        return orderCarCenterVos == null ? new ArrayList<>(0) : orderCarCenterVos;
    }
}