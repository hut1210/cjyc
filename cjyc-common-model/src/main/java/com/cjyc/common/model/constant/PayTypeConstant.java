package com.cjyc.common.model.constant;

/**
 * 支付回调类型常量
 *
 * @author RenPL
 */
public class PayTypeConstant {

    /**
     * ping++订单支付成功
     */
    public static final String ORDER_SUCCEEDED = "order.succeeded";

    /**
     * ping++订单支付退款
     */
    public static final String ORDER_REFUNDED = "order.refunded";

    /**
     * ping++订单支付退款
     */
    public static final String CHARGE_SUCCEEDED = "charge.succeeded";

    /**
     * 通联代付成功
     */
    public static final String TRANSFER_SUCCEEDED = "transfer.succeeded";

    /**
     * 通联代付失败
     */
    public static final String TRANSFER_FAILED = "transfer.failed";

    /**
     *
     */
    public static final String REFUND_SUCCEEDED = "refund.succeeded";
}
