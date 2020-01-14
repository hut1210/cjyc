package com.cjyc.common.model.enums.role;

import com.cjyc.common.model.util.YmlProperty;

public enum RoleTitleEnum {

    PERSON_DRIVER(YmlProperty.get("cjkj.carrier_personal_driver_role_name"),0),
    SUBROD_DRIVER(YmlProperty.get("cjkj.carrier_sub_driver_role_name"), 1),
    CARRIER_ADMINSTRATOR(YmlProperty.get("cjkj.carrier_common_role_name"), 2),
    SUPER_ADMINSTRATOR(YmlProperty.get("cjkj.carrier_super_admin_role_name"), 3),
    CLIENT_CUSTOMER("C端客户",4),
    KEY_CUSTOMER("大客户",5),
    COPARTNER("合伙人",6);

    private String name;
    private int code;

    private RoleTitleEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
