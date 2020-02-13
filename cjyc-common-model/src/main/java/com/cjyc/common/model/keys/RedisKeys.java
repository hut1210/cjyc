package com.cjyc.common.model.keys;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.BankInfoDto;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.google.common.collect.Lists;

import java.util.List;

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

    private final static String DISPATCH_LOCK_CAR_KEY = "dispatch:lock:car";
    private final static String DISPATCH_LOCK_ORDER_UPDATE = "dispatch:lock:order:update";
    private final static String ALLOT_CAR_LOCK_KEY = "allot:car:lock";
    private final static String LOOP_ALLOT_ADMIN_KEY = "loop:allot:admin";
    private final static String CAR_SERIES = "car:series";
    private final static String THREE_CITY = "three:city";
    private final static String BANK_INFO = "bankInfo:bankName";

    private final static String USER_KEY = "user";

    private final static String ROLE_BIZ_SCOPE_KEY = "role:biz:scope";

    private final static String NEW_TASK_NO_KEY = "new:task:no";
    private final static String WL_PRE_PAY_LOCK = "wl:pre:pay:lock";
    private final static String WL_COLLECT_PAY_LOCK = "wl:collect:pay:lock";

    /**---------------salesman-------------------------------------------------------------------*/
    /**验证码*/
    private final static String CAPTCHA_KEY = "cjyc:sale";
    /**---------------driver-------------------------------------------------------------------*/


    /**---------------customer-------------------------------------------------------------------*/
    public static String getCaptchaKey(ClientEnum clientEnum, String phone, CaptchaTypeEnum captchaTypeEnum) {
        return getPreixByCilent(clientEnum) + I + CAPTCHA_KEY + I + captchaTypeEnum.getCode() + I + phone;
    }

    public static String getSmsCountKey(String date, String phone) {
        return PROJECT_PREFIX + I + SMS_COUNT_KEY + I + date + I + phone;
    }

    public static String getDispatchLock(String orderCarNo) {
        return PROJECT_PREFIX + I + DISPATCH_LOCK_CAR_KEY + I + orderCarNo;
    }

    public static String getAllotTaskKey(Long waybillCarId) {
        return PROJECT_PREFIX + I + ALLOT_CAR_LOCK_KEY + I + waybillCarId;
    }

    public static String getUserKey(Long userId) {
        return PROJECT_PREFIX + I + USER_KEY + I + userId;
    }

    public static String getRoleBizScopeKey(ClientEnum clientEnum, Long roleId) {
        return getPreixByCilent(clientEnum) + I + ROLE_BIZ_SCOPE_KEY + I + roleId;
    }

    public static String getNewTaskNoKey(String waybillNo) {
        return PROJECT_PREFIX + I + NEW_TASK_NO_KEY + I + waybillNo;
    }

    public static String getWlCollectPayLockKey(String carNo) {
        return  PROJECT_PREFIX + I + WL_COLLECT_PAY_LOCK + I + carNo;
    }
    public static List<String> getWlCollectPayLockKeys(List<String> carNos) {
        List<String> list = Lists.newArrayList();
        for (String carNo : carNos) {
            list.add(PROJECT_PREFIX + I + WL_COLLECT_PAY_LOCK + I + carNo);
        }
        return list;
    }

    public static String getWlPrePayLock(String orderNo) {
        return PROJECT_PREFIX + I + WL_PRE_PAY_LOCK + I + orderNo;
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

    public static String getDispatchLockForOrderUpdate(String orderNo) {
        return PROJECT_PREFIX + I + DISPATCH_LOCK_ORDER_UPDATE + I + orderNo;
    }

    public static String getLoopAllotAdminKey(Long startStoreId) {
        return PROJECT_PREFIX + I + LOOP_ALLOT_ADMIN_KEY + I + startStoreId;
    }

    public static String getCarSeriesKey(String keyword){
        return PROJECT_PREFIX + I + CAR_SERIES + I + keyword;
    }

    public static String getThreeCityKey(String keyword){
        return PROJECT_PREFIX + I + THREE_CITY + I + keyword;
    }

    public static String getAppBankInfoKey(BankInfoDto dto){
        return PROJECT_PREFIX + I + BANK_INFO + I + dto.getCurrentPage() + I + dto.getPageSize() + I + dto.getKeyword();
    }

    public static String getWebBankInfoKey(KeywordDto dto){
        return PROJECT_PREFIX + I + BANK_INFO + I + dto.getKeyword();
    }
}
