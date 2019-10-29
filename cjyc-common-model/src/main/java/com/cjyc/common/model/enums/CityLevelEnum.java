package com.cjyc.common.model.enums;

public enum CityLevelEnum {

    HIGHEST_LEVEL("最高级", -1),
    LARGE_LEVEL("大区级", 0),
    PROVINCE_LEVEL("省/直辖市级", 1),
    CITY_LEVEL("城市级别",2),
    AREA_LEVEL("区县级别",3);

    private final String name;
    private final int level;

    public int getLevel(){
        return level;
    }

    public String getName(){
        return name;
    }

    private CityLevelEnum(String name, int level) {
        this.name = name;
        this.level = level;
    }
}
