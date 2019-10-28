package com.cjyc.common.model.enums.waybill;

/**
 * 运单类型枚举
 * @author JPG
 */
public enum WaybillTypeEnum {
    /***/
    PICK("提车运单", 1),
    TRUNK("干线运单", 2),
    BACK("配送运单", 3);

    public String name;
    public int code;

    WaybillTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
