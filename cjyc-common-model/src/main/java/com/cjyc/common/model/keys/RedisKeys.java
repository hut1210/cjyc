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
    private final static String PROJECT_1_PREFIX = "cjyc";
    /**项目前缀：web缩写**/
    private final static String WEB_1_PREFIX = PROJECT_1_PREFIX + I + "web";
    /**项目前缀：salesman缩写**/
    private final static String SALESMAN_1_PREFIX = PROJECT_1_PREFIX + I + "sale";
    /**业务项目前缀：driver缩写**/
    private final static String DRIVER_1_PREFIX = PROJECT_1_PREFIX + I + "drv";
    /**业务项目前缀：customer缩写**/
    private final static String CUSTOMER_1_PREFIX = PROJECT_1_PREFIX + I + "csr";


    /**
     * 二级前缀
     */
    /**所有项目公用-前缀：韵车首字母**/
    private final static String PUBLIC_2_LEVEL = "pub";
    /**本项目公用-前缀：韵车首字母**/
    private final static String PROTECTED_2_LEVEL = "pro";
    /**本项目私用-前缀：韵车首字母**/
    private final static String PRIVATE_2_LEVEL = "pvt";

    /**---------------common-------------------------------------------------------------------*/
    /**验证码*/
    private final static String CAPTCHA_KEY = "captcha:";

    /**---------------salesman-------------------------------------------------------------------*/
    /**---------------driver-------------------------------------------------------------------*/
    /**---------------customer-------------------------------------------------------------------*/



    public static String getSalesmanCaptchaKeyByPhone(String phone, int type) {
        return SALESMAN_1_PREFIX + I + PUBLIC_2_LEVEL + I + CAPTCHA_KEY + I + type + I + phone;
    }
}
