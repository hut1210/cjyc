package com.cjyc.common.model.vo.customer.invoice;

import com.cjyc.common.model.util.BigDecimalSerizlizer;
import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

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
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal totalFee;

    @ApiModelProperty(value = "订单完结时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long finishTime;

    @ApiModelProperty(value = "车辆数")
    private int carNum;

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }

    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }
}
