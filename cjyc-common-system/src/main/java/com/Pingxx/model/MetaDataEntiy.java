package com.Pingxx.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MetaDataEntiy {
    /**支付类型 类型：1物流费按订单预付，2物流费按订单到付，3物流费按车辆到付， 11运费支付，12居间服务费支付*/
    private String chargeType;
    /**支付支付渠道*/
    private String channel;
    /**扣费金额*/
    private BigDecimal deductFee;
    /**客户端类型*/
    private String clientId;
    /**当前app登陆人的Id*/
    private String loginId;
    /**当前app登陆人的名称*/
    private String loginName;
    /**当前app登陆人的名称*/
    private String loginType;
    /**支付订单来源编号，根据支付类型*/
    private String sourceNos;
}
