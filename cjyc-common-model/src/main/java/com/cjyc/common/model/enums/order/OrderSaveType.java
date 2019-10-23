package com.cjyc.common.model.enums.order;

/**
 * 订单保存方式
 * @author JPG
 */
public enum OrderSaveType {

    /***/
    SAVE("保存",1),
    COMMIT("提交",2),
    CHECK("审核",3);

    public String name;
    public int code;

    OrderSaveType(String name, int code) {
        this.name = name;
        this.code = code;
    }

}
