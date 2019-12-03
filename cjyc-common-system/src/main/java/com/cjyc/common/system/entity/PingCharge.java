package com.cjyc.common.system.entity;

import com.pingplusplus.model.Charge;

import java.math.BigDecimal;

public class PingCharge extends Charge {

    private String orderNo; //平台订单编号
    private String chargeType;	//支付类型 类型：1物流费预付，2物流费全款到付，3物流费分车支付， 11运费支付、12居间服务费支付
    private String batch;	//0:整体支付尾款	 1：批量支付尾款
    private BigDecimal deductFee;	//扣费金额
    private String clientType;	//客户端类型 customer:用户端	driver:司机端    user:业务员端
    private String pingAppId;
    private BigDecimal deposit;	//定金
    private String orderMan; //当前app登陆人的Id
    private String orderCarId;
    private String driver_code;
    private String order_type;
    private String driver_name;
    private String driver_phone;
    private String back_type;


}
