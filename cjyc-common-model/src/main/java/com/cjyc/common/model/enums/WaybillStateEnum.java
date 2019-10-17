package com.cjyc.common.model.enums;

/**
 * 运单状态枚举
 * @author JPG
 */
public enum WaybillStateEnum {
    /***/
    WAIT_ALLOT("待分配承运商（竞抢）",0),
    WAIT_ALLOT_CONFIRM("待承运商承接任务",15),
    TRANSPORTING("运输中",30),
    FINISHED("已完成",100),
    F_CANCEL("已取消",102),
    F_REJECTED("已拒接",103),
    F_TIMEOUT("超时关闭",111);

    public String name;
    public int code;

    WaybillStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
