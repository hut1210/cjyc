package com.cjyc.common.model.keys;

/**
 * RedisKey
 * @author JPG
 */
public class RedisKeys {
    /**通用分隔符：英文冒号**/
    private final static String I = ":";

    /**
     * 一级前缀
     */
    /**项目前缀：长久韵车首字母**/
    private final static String PROJECT_PREFIX = "cjyc";
    /**项目前缀：web缩写**/
    private final static String WEB_PREFIX = "cjyc:web";
    /**项目前缀：salesman缩写**/
    private final static String SALESMAN_PREFIX = "cjyc:sale";
    /**业务项目前缀：driver缩写**/
    private final static String DRIVER_PREFIX = "cjyc:drv";
    /**业务项目前缀：customer缩写**/
    private final static String CUSTOMER_PREFIX = "cjyc:csr";



    /**---------------common-------------------------------------------------------------------*/
    /**验证码*/
    private final static String SMS_COUNT_KEY = "cjyc:sms:count";

    private final static String DISPATCH_CAR_LOCK_KEY = "dispatch:car:lock";

    /**---------------salesman-------------------------------------------------------------------*/
    /**验证码*/
    private final static String CAPTCHA_KEY = "cjyc:sale:";
    /**---------------driver-------------------------------------------------------------------*/


    /**---------------customer-------------------------------------------------------------------*/



    public static String getSalesmanCaptchaKeyByPhone(String phone, int type) {
        return SALESMAN_PREFIX + I + CAPTCHA_KEY + I + type + I + phone;
    }

    public static String getSmsCountKey(String phone) {
        return PROJECT_PREFIX + I + SMS_COUNT_KEY + I;
    }

    public static String getDispatchLock(Long orderCarId) {
        return PROJECT_PREFIX + I + DISPATCH_CAR_LOCK_KEY + I + orderCarId;
    }
}
