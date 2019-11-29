package com.cjyc.common.model.enums;

/**
 * 运营状态
 * @author JPG
 */
public enum BizStateEnum {
    /***/
    BUSINESS("营运中", 0),
    REST("休息中", 1);
    public String name;
    public int code;

    BizStateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
