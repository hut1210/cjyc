package com.cjyc.common.model.enums.finance;

/**
 * 应收账款结算状态枚举
 * @author RenPL
 */
public enum ReceiveSettlementStateEnum {

    UNNEEDED_INVOICE("不需要开票", 0),
    APPLY_INVOICE("已申请发票", 1),
    CONFIRM_INVOICE("已确认开票", 2),
    VERIFICATION("已核销", 3);

    public String name;
    public int code;

    ReceiveSettlementStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
