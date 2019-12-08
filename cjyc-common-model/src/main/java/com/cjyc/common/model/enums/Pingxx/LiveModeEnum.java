package com.cjyc.common.model.enums.Pingxx;

public enum LiveModeEnum {
    /***/
    TEST("test", false),
    LIVE("live", true);

    private String tag;
    private boolean code;

    LiveModeEnum(String tag, boolean code) {
        this.tag = tag;
        this.code = code;
    }

    public String getTag() {
        return tag;
    }

    public boolean isCode() {
        return code;
    }
}
