package com.cjyc.common.model.enums.role;

/**
 * 角色所属机构级别
 */

public enum RoleLevelEnum {
    /***/
    COUNTRY_LEVEL("全国", 1), REGION_LEVEL("大区", 2),
    PROVINCE_LEVEL("省", 3), CITY_LEVEL("城市", 4),
    BIZ_CENTER_LEVEL("业务中心", 5);
    private String name;
    private int level;
    private RoleLevelEnum(String name, int level) {
        this.name = name;
        this.level = level;
    }
    public String getName() {
        return name;
    }
    public int getLevel() {
        return level;
    }
}
