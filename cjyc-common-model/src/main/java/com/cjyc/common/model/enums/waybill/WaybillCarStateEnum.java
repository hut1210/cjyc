package com.cjyc.common.model.enums.waybill;

/**
 * 运单车辆状态
 * @author JPG
 */
public enum WaybillCarStateEnum {
    /***/

    WAIT_ALLOT("待指派",0),
    ALLOTED("已指派",2),
    WAIT_LOAD("待装车",5),
    WAIT_UNLOAD("已装车",7),
    UNLOADED("已卸车",9),
    APPLY_CONFIRM("确认交车",90),
    CONFIRMED("确认收车",100);

    public String name;
    public int code;

    WaybillCarStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
