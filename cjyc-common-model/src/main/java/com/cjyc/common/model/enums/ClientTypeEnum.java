package com.cjyc.common.model.enums;

/**
 * 客户端类型枚举
 * @author JPG
 */
public enum ClientTypeEnum {

    /***/
    WEB_SERVER("WEB管理后台", 0),
    APP_SALESMAN("业务员APP", 1),
    APP_DRIVER("业务员APP",2),
    APP_CUSTOMER("业务员APP",3);

    public String name;
    public int code;

    ClientTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
