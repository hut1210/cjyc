package com.cjyc.common.model.enums.order;

/**
 * 同城调度状态
 * @author JPG
 */
public enum OrderCarTrunkStateEnum {

    /***/
    WAIT_DISPATCH("待调度",1),
    WAIT_NEXT_DISPATCH("节点调度",2),
    DISPATCHED("已调度",5),
    F_NONE("无干线",10);

    public String name;
    public int code;

    OrderCarTrunkStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
