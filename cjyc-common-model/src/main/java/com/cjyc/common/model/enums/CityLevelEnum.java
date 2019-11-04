package com.cjyc.common.model.enums;

public enum CityLevelEnum {

    //-1 :中国 0：大区 1：省/直辖市 2：城市 3：区县
    HIGH_LEVEL("中国", -1),
    LARGE_LEVEL("大区", 0),
    PROVINCE_LEVEL("省/直辖市", 1),
    CITY_LEVEL("城市",2),
    AREA_LEVEL("区县",3);

    private final String name;
    private final int level;

    private CityLevelEnum(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName(){
        return name;
    }

    public int getLevel(){
        return level;
    }

}
