package com.cjyc.common.model.vo.customer;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCenterVo extends BasePageDto implements Serializable {

    @ApiModelProperty("订单编号")
    private String no;

    @ApiModelProperty(value = "市")
    private String startCity;

    @ApiModelProperty(value = "市")
    private String endCity;

    @ApiModelProperty(value = "订单总价")
    private String totalFee;

    @ApiModelProperty(value = "车辆总数")
    private String carNum;

    List<OrderCarCenterVo> orderCarCenterVos;
}