package com.cjyc.common.model.enums.order;

/**
 * 同城调度状态
 * @author JPG
 */
public enum OrderCarLocalStateEnum {

    /***/
    WAIT_DISPATCH("待调度",1),
    DISPATCHED("已调度",5),
    F_SELF("自提自送",7),
    F_WL("物流上门",10);

    public String name;
    public int code;

    OrderCarLocalStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
