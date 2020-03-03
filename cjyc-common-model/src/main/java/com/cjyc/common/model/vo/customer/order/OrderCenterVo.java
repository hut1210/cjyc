package com.cjyc.common.model.vo.customer.order;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCenterVo implements Serializable {
    private static final long serialVersionUID = -5418843717123216878L;
    @ApiModelProperty("订单主键ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("订单编号")
    private String no;

    @ApiModelProperty(value = "出发城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "订单总价")
    @JsonSerialize(using= BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "车辆总数")
    private Integer carNum;

    @ApiModelProperty(value = "线路ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long lineId;

    @ApiModelProperty(value = "订单状态：0待提交，2待分配，5待确认，10待复确认，15待预付款，25已确认，55运输中，" +
            "88待付款，100已完成，111原返（待），112异常结束，113取消（待），114作废（待）")
    private String state;

    @ApiModelProperty(value = "车辆信息列表")
    private List<OrderCarCenterVo> orderCarCenterVoList;

    public Long getLineId() {
        return lineId == null ? 0 : lineId;
    }
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
    public BigDecimal getTotalFee() {
        return totalFee == null ? new BigDecimal(0) : totalFee;
    }
    public Integer getCarNum() {
        return carNum == null ? 0 : carNum;
    }
    public List<OrderCarCenterVo> getOrderCarCenterVoList() {
        return orderCarCenterVoList == null ? new ArrayList<>(0) : orderCarCenterVoList;
    }
}