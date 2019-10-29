package com.cjyc.common.model.enums;

public enum DictionaryEnum {

    /***/
    INSURANCE_AMOUNT("基础保险额度", "INSURANCE", "BASE_AMOUNT");
    public String name;
    public String item;
    public String key;

    DictionaryEnum(String name, String item, String key) {
        this.name = name;
        this.item = item;
        this.key = key;
    }
}
