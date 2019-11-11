package com.cjyc.common.model.enums.waybill;

/**
 * 运单备注枚举
 * @author JPG
 */
public enum WaybillRemarkEnum {
    /***/
    MIDWAY_FINISH_NEW("中途完结并生成运单","当前运单中途结束，生成后续运单{}"),
    MIDWAY_FINISH_NONE("中途完结的运单不生成运单","当前运单中途结束,未生成后续运单"),
    MIDWAY_FINISH_CREATED("中途完结单创建的运单", "运单{}中途结束，生成当前运单");

    private String name;
    private String msg;

    WaybillRemarkEnum(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

}
