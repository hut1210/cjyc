package com.cjyc.common.model.enums;

public enum ChargeTypeEnum {
    /***/
    PREPAY("预付款支付", "prepay", 1),
    PREPAY_QRCODE("预付款二维码收款", "prepay_qrcode", 2),
    COLLECT_PAY("到付支付", "collect_pay", 5),
    COLLECT_QRCODE("到付二维码收款", "collect_pay", 6),
    DRIVER_COLLECT_QRCODE("司机端二维码收款", "driver_collect_qrcode", 3),
    SALESMAN_COLLECT_QRCODE("业务员端二维码收款", "salesman_collect_qrcode", 4),

    WEB_PREPAY_QRCODE("后台预付二维码收款","web_prepay_qrcode",7),
    WEB_OUT_STOCK_QRCODE("后台确认出库二维码收款","web_out_stock_qrcode",8),

    SALES_PREPAY_QRCODE("业务员端预付款二维码收款", "salesman_prepay_qrcode", 9),
    UNION_PAY("通联代付给承运商打款","union_pay",20);
    private String name;
    private String tag;
    private int code;

    ChargeTypeEnum(String name, String tag, int code) {
        this.name = name;
        this.tag = tag;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public int getCode() {
        return code;
    }
}
