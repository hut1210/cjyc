package com.cjyc.common.model.enums.waybill;

/**
 * 运单提车类型枚举
 * @author JPG
 */
public enum WaybillTakeTypeEnum {
    /***/
    SELF("自送/自提", 1),
    PICK("上门提车", 2);

    public String name;
    public int code;

    WaybillTakeTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
