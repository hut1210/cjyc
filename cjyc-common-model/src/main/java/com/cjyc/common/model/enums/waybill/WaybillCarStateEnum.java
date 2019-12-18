package com.cjyc.common.model.enums.waybill;

/**
 * 运单车辆状态
 *
 * @author JPG
 */
public enum WaybillCarStateEnum {

    /**
     * 0待指派，2已指派，5待装车，15待装车交接，45已装车，70已卸车，90确认交车, 100确认收车, 105待重连
     */
    WAIT_ALLOT("待指派", 0),
    //ALLOTED("已指派", 2),
    WAIT_LOAD("待装车", 5),
    WAIT_LOAD_CONFIRM("待装车交接,待确认出库", 15),
    LOADED("已装车", 45),
    //UNLOADED("已卸车", 70),
    WAIT_UNLOAD_CONFIRM("确认交车", 90),
    UNLOADED("确认收车", 100),
    WAIT_CONNECT("待重连", 105),
    F_CANCEL("已取消", 113);

    public String name;
    public int code;

    WaybillCarStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
