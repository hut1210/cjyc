package com.cjyc.common.model.enums;

/**
 * 调度方式
 * @author JPG
 */
public enum DispatchTypeEnum {

    /***/
    SELF("自己处理", 1),
    MANUAL("人工调度",2),
    BID("竞抢调度",3),
    BID_APPOINT("竞抢指派",4);

    public String name;
    public int code;

    DispatchTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
