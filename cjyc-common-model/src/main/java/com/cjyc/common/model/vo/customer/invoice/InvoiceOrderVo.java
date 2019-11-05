package com.cjyc.common.model.vo.customer.invoice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 发票订单返回实体
 * @Author LiuXingXiang
 * @Date 2019/11/2 9:09
 **/
@Data
public class InvoiceOrderVo implements Serializable {
    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty(value = "出发城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "订单总价")
    private String totalFee;

    @ApiModelProperty(value = "订单完结时间")
    private Long finishTime;

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }
    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }
    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }
    public String getTotalFee() {
        return totalFee == null ? "" : totalFee;
    }
    public Long getFinishTime() {
        return finishTime == null ? 0L : finishTime;
    }
}