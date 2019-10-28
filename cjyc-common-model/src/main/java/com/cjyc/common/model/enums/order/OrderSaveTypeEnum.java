package com.cjyc.common.model.enums.order;

/**
 * 订单保存方式
 * @author JPG
 */
public enum OrderSaveTypeEnum {

    /***/
    SAVE("保存",1),
    COMMIT("提交",2),
    CHECK("审核",3);

    public String name;
    public int code;

    OrderSaveTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

}
