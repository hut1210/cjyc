package com.cjyc.common.model.vo.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDetailVo implements Serializable {

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("下单日期")
    private String createTime;

    @ApiModelProperty("提车日期")
    private String expectStartDate;

    @ApiModelProperty(value = "发车人")
    private String pickContactName;

    @ApiModelProperty(value = "发车人联系方式")
    private String pickContactPhone;

    @ApiModelProperty(value = "提车方式:1 自送，2代驾上门，3拖车上门")
    private String pickType;

    @ApiModelProperty(value = "出发地详细地址")
    private String startAddress;

    @ApiModelProperty(value = "收车人")
    private String backContactName;

    @ApiModelProperty(value = "收车人联系方式")
    private String backContactPhone;

    @ApiModelProperty(value = "目的地详细地址")
    private String endAddress;

    @ApiModelProperty(value = "送车方式： 1 自提，2代驾上门，3拖车上门")
    private String backType;

    @ApiModelProperty(value = "应收提车费")
    private String pickFee;

    @ApiModelProperty(value = "应收干线费")
    private String trunkFee;

    @ApiModelProperty(value = "应收配送费")
    private String backFee;

    @ApiModelProperty(value = "应收保险费")
    private String insuranceFee;

    @ApiModelProperty(value = "应收订单定金（保留字段）")
    private String depositFee;

    @ApiModelProperty(value = "总价")
    private String totalFee;

    @ApiModelProperty(value = "订单状态")
    private String state;

    private List<OrderCarCenterVo> orderCarCenterVos;
}