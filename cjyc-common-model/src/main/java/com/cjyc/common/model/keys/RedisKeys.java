package com.cjyc.common.model.keys;

/**
 * RedisKey
 * @author JPG
 */
public class RedisKeys {
    /**通用分隔符：英文冒号**/
    private final String SEPARATOR = ":";

    /**
     * 一级前缀
     */
    /**项目前缀：长久韵车首字母**/
    private final String PROJECT_1_PREFIX = "cjyc";
    /**项目前缀：server缩写**/
    private final String SERVER_1_PREFIX = PROJECT_1_PREFIX + SEPARATOR + "svr";
    /**业务项目前缀：driver缩写**/
    private final String DRIVER_1_PREFIX = PROJECT_1_PREFIX + SEPARATOR + "drv";
    /**业务项目前缀：customer缩写**/
    private final String CUSTOMER_1_PREFIX = PROJECT_1_PREFIX + SEPARATOR + "csr";


    /**
     * 二级前缀
     */
    /**系统前缀：韵车首字母**/
    private final String SYSTEM_2_PREFIX = "sys";
    /**本项目公用前缀：韵车首字母**/
    private final String PUBLIC_2_PREFIX = "pub";
    /**本项目私用前缀：韵车首字母**/
    private final String PRIVATE_2_PREFIX = "pvt";







}
