package com.cjyc.common.model.enums.order;

/**
 * 同城调度状态
 * @author JPG
 */
public enum OrderCarTrunkStateEnum {

    /***/
    WAIT_DISPATCH("待调度",1),
    WAIT_LOAD("待装车",2),
    WAIT_UNLOAD("待交付",3),
    NODE_FINISHED("已交付",9),
    FINISHED("已完结",10);

    public String name;
    public int code;

    OrderCarTrunkStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
