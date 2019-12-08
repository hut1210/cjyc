package com.Pingxx.model;

import lombok.Data;

@Data
public class PingOrderModel extends Order {
    /**
     * 支付类型 类型：1物流费按订单预付，2物流费按订单到付，3物流费按车辆到付， 11运费支付，12居间服务费支付
     */
    private MetaDataEntiy metaDataEntiy;
}
