package com.cjyc.common.model.keys;

import com.cjyc.common.model.entity.Customer;

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
    private final static String SMS_COUNT_KEY = "sms:count";

    private final static String DISPATCH_CAR_LOCK_KEY = "dispatch:car:lock";
    private final static String ALLOT_CAR_LOCK_KEY = "allot:car:lock";

    private final static String USER_KEY = "user";

    /**---------------salesman-------------------------------------------------------------------*/
    /**验证码*/
    private final static String CAPTCHA_KEY = "cjyc:sale";
    /**---------------driver-------------------------------------------------------------------*/


    /**---------------customer-------------------------------------------------------------------*/
    public static String getSalesmanCaptchaKeyByPhone(String phone, int type) {
        return SALESMAN_PREFIX + I + CAPTCHA_KEY + I + type + I + phone;
    }

    public static String getSmsCountKey(String date, String phone) {
        return PROJECT_PREFIX + I + SMS_COUNT_KEY + I + date + I + phone;
    }

    public static String getDispatchLock(String orderCarNo) {
        return PROJECT_PREFIX + I + DISPATCH_CAR_LOCK_KEY + I + orderCarNo;
    }

    public static String getAllotTaskKey(String orderCarNo) {
        return PROJECT_PREFIX + I + ALLOT_CAR_LOCK_KEY + I + orderCarNo;
    }

    public static String getUserKey(Long userId) {
        return PROJECT_PREFIX + I + USER_KEY + I + userId;
    }
}
