package com.cjyc.common.model.enums;

public enum ChargeTypeEnum {
    /***/
    PREPAY("预付款支付", "prepay", 1),
    PREPAY_QRCODE("预付款二维码收款", "prepay_qrcode", 2),
    COLLECT_PAY("到付支付", "collect_pay", 3),
    COLLECT_QRCODE("到付二维码收款", "collect_pay", 4);
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
