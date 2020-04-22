package com.cjyc.common.model.enums.order;

public enum OrderCarReleaseEnum {
    /**
     * 车辆放车状态
     * @author JPG
     * @since 2020/4/21 15:33
     * @param null
     */
    UNLIMIT("无限制",-1),
    UNRELEASE_UNPAID("未支付且不可交车",0),
    RELEASE_UNPAID("未支付但可交车",1),
    UNRELEASE_PAID("已支付但不可交车",2),
    RELEASE_PAID("已支付且可交车",9);

    public String name;
    public int code;

    OrderCarReleaseEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
