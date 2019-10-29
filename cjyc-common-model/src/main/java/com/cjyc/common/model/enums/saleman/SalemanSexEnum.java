package com.cjyc.common.model.enums.saleman;

/**
 * 性别
 */
public enum  SalemanSexEnum {
    MAN("女", 0), WOMAN("男", 1);
    private String desc;
    private int code;
    private SalemanSexEnum(String desc, int code){
        this.desc = desc;
        this.code = code;
    }
    public String getDesc() {
        return desc;
    }
    public int getCode() {
        return code;
    }
}
