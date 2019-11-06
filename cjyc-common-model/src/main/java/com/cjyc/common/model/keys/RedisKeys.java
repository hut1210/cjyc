package com.cjyc.common.model.keys;

import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;

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

    private final static String USER_BIZ_SCOPE_KEY = "user:biz:scope";

    /**---------------salesman-------------------------------------------------------------------*/
    /**验证码*/
    private final static String CAPTCHA_KEY = "cjyc:sale";
    /**---------------driver-------------------------------------------------------------------*/


    /**---------------customer-------------------------------------------------------------------*/
    public static String getCaptchaKey(ClientEnum clientEnum, String phone, CaptchaTypeEnum captchaTypeEnum) {
        return getPreixByCilent(clientEnum) + I + CAPTCHA_KEY + I + captchaTypeEnum.code + I + phone;
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

    public static String getUserBizScopeKey(ClientEnum clientEnum, Long userId) {
        return getPreixByCilent(clientEnum) + I + USER_KEY + I + userId;
    }

    /**
     * 根据客户端判断前缀
     * @param clientEnum
     * @return
     */
    private static String getPreixByCilent(ClientEnum clientEnum) {
        String prefix;
        if(clientEnum == ClientEnum.WEB_SERVER){
            prefix = WEB_PREFIX;
        }else if(clientEnum == ClientEnum.APP_SALESMAN){
            prefix = SALESMAN_PREFIX;
        }else if(clientEnum == ClientEnum.APP_DRIVER){
            prefix = DRIVER_PREFIX;
        }else if(clientEnum == ClientEnum.APP_CUSTOMER || clientEnum == ClientEnum.APPLET_CUSTOMER){
            prefix = CUSTOMER_PREFIX;
        }else{
            prefix = PROJECT_PREFIX;
        }
        return prefix;
    }
}
