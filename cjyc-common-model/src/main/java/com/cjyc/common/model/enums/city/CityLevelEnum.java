package com.cjyc.common.model.enums.city;


public enum CityLevelEnum {
    /***/
    CHINA("中国", -1),
    REGION("大区", 0),
    PROVINCE("省", 1),
    CITY("市",2),
    AREA("区",3);

    public String name;
    public int code;

    CityLevelEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
