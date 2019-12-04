package com.cjyc.common.system.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PingOrderModel extends PingOrder {
    private String chargeType;	//支付类型 类型：1物流费预付，2物流费全款到付，3物流费分车支付， 11运费支付、12居间服务费支付
    private BigDecimal deductFee;	//扣费金额
    private String clientType;	//客户端类型
    private String loginId; //当前app登陆人的Id
    private String orderId;
    private List<String> sourceNoList;
}
