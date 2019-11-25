package com.cjyc.common.model.enums;

/**
 * 验证码类型枚举
 * @author JPG
 */
public enum CaptchaTypeEnum {
    /**1登录，2忘记登录密码，3忘记安全密码，4修改银行卡*/
    LOGIN("注册登录", 1),
    FORGET_LOGIN_PWD("忘记登录密码", 2),
    FORGET_SAFE_PWD("忘记安全密码", 3),
    UPDATE_BANK_CAR("修改银行卡", 4),
    CONFIRM_RECEIPT("收车码", 5);


    private String name;
    private int code;

    CaptchaTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static CaptchaTypeEnum valueOf(int code) {
        for (CaptchaTypeEnum codeEnum : values()) {
            if (codeEnum.code == code) {
                return codeEnum;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }

}
