package com.cjyc.common.model.enums;

/**
 * 客户端类型枚举
 * @author JPG
 */
public enum ClientEnum {

    /**1WEB管理后台, 2业务员APP, 3业务员小程序, 4业务员APP, 5业务员小程序, 6用户端APP, 7用户端小程序*/
    WEB_SERVER("WEB管理后台", 1),
    APP_SALESMAN("业务员APP", 2),
    //APPLET_SALESMAN("业务员小程序", 3),
    APP_DRIVER("业务员APP",4),
    //APPLET_DRIVER("业务员小程序",5),
    APP_CUSTOMER("用户端APP",6),
    APPLET_CUSTOMER("用户端小程序",7);

    public String name;
    public int code;

    ClientEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
