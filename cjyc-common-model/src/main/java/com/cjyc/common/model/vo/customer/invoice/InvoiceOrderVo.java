package com.cjyc.common.model.vo.customer.invoice;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long finishTime;

    @ApiModelProperty(value = "车辆数")
    private int carNum;

    public String getOrderNo() {
        return StringUtils.isBlank(orderNo) ? "" : orderNo;
    }

    public String getStartCity() {
        return StringUtils.isBlank(startCity) ? "" : startCity;
    }

    public String getEndCity() {
        return StringUtils.isBlank(endCity) ? "" : endCity;
    }

    public Long getFinishTime() {
        return finishTime == null ? 0 : finishTime;
    }

    public BigDecimal getTotalFee() {
        return totalFee == null ? new BigDecimal(0) : totalFee;
    }
}
