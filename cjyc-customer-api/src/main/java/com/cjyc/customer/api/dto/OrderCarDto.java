package com.cjyc.customer.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther litan
 * @description: 订单车辆入参实体类
 * @date:2019/10/8
 */
@Data
public class OrderCarDto implements Serializable {
    private int orderId;
    private String orderNo;
    private String no;
    private String brand;
    private String model;
    private String plateNo;//车牌号
    private String vin;
    private int valuation;
    private int state;
    private String description;
    private int pickFee;
    private int trunkFee;
    private int backFee;
    private int insuranceFee;
    private int insuranceCoverageAmount;
    private int agencyFee;
    private int wl_pay_state;

}
