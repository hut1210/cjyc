package com.cjyc.common.model.keys;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.BankInfoDto;
import com.cjyc.common.model.dto.web.payBank.PayBankDto;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import org.apache.commons.lang3.StringUtils;

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
    private final static String CJYC = "cjyc";
    /**项目前缀：web缩写**/
    private final static String CJYC_WEB = "cjyc:web";
    /**项目前缀：salesman缩写**/
    private final static String CJYC_SALESMAN = "cjyc:sale";
    /**业务项目前缀：driver缩写**/
    private final static String CJYC_DRIVER = "cjyc:drv";
    /**业务项目前缀：customer缩写**/
    private final static String CJYC_CUSTOMER = "cjyc:csr";



    /**---------------common-------------------------------------------------------------------*/
    /**验证码*/
    private final static String SMS_COUNT_KEY = "sms:count";

    /**锁**/
    private final static String DISPATCH_LOCK_CAR_KEY = "dispatch:lock:car";
    private final static String UNLOAD_LOCK_KEY = "unload:lock";
    private final static String LOAD_LOCK_KEY = "load:lock";
    private final static String RECEIPT_LOCK_KEY = "receipt:lock";
    private final static String ORDER_LOCK_KEY = "order:lock";
    private final static String CANCEL_LOCK_ORDER_KEY = "cancel:lock:order";
    private final static String IN_STORE_LOCK_KEY = "in:store:lock";
    private final static String OUT_STORE_LOCK_KEY = "out:store:lock";
    private final static String DISPATCH_LOCK_ORDER_UPDATE = "dispatch:lock:order:update";
    private final static String ALLOT_CAR_LOCK_KEY = "allot:car:lock";
    private final static String WL_PAY_LOCK = "wl:pay:lock";
    /**循环业务人员*/
    private final static String LOOP_ALLOT_ADMIN_KEY = "loop:allot:admin";
    /**验证vin重复*/
    private final static String CHECK_VIN_SET = "check:vin:set";
    /**验证车牌号重复*/
    private final static String CHECK_PLATENO_SET = "check:plateno:set";

    private final static String CAR_SERIES = "car:series";
    private final static String THREE_CITY = "three:city";
    private final static String BANK_INFO = "bankInfo:bankName";
    private final static String CITY_TREE = "city:tree";
    private final static String KEYWORD_CITY_TREE = "keyword:city:tree";
    private final static String KEYWORD_POSTAL_CODE = "keyword:postal:code";
    private final static String PAY_BANK_INFO = "pay:bank:info";

    private final static String USER_KEY = "user";

    private final static String ROLE_BIZ_SCOPE_KEY = "role:biz:scope";

    private final static String NEW_TASK_NO_KEY = "new:task:no";



    /**---------------driver-------------------------------------------------------------------*/


    /**---------------customer-------------------------------------------------------------------*/
    public static String getCaptchaKey(ClientEnum clientEnum, String phone, CaptchaTypeEnum captchaTypeEnum) {
        return getPreixByCilent(clientEnum) + I + captchaTypeEnum.getCode() + I + phone;
    }

    public static String getSmsCountKey(String date, String phone) {
        return CJYC + I + SMS_COUNT_KEY + I + date + I + phone;
    }


    public static String getUserKey(Long userId) {
        return CJYC + I + USER_KEY + I + userId;
    }

    public static String getRoleBizScopeKey(ClientEnum clientEnum, Long roleId) {
        return getPreixByCilent(clientEnum) + I + ROLE_BIZ_SCOPE_KEY + I + roleId;
    }

    public static String getNewTaskNoKey(String waybillNo) {
        return CJYC + I + NEW_TASK_NO_KEY + I + waybillNo;
    }

    public static String getWlPayLockKey(String no) {
        return  CJYC + I + WL_PAY_LOCK + I + no;
    }

    /**
     * 根据客户端判断前缀
     * @param clientEnum
     * @return
     */
    private static String getPreixByCilent(ClientEnum clientEnum) {
        String prefix;
        if(clientEnum == ClientEnum.WEB_SERVER){
            prefix = CJYC_WEB;
        }else if(clientEnum == ClientEnum.APP_SALESMAN){
            prefix = CJYC_SALESMAN;
        }else if(clientEnum == ClientEnum.APP_DRIVER){
            prefix = CJYC_DRIVER;
        }else if(clientEnum == ClientEnum.APP_CUSTOMER || clientEnum == ClientEnum.APPLET_CUSTOMER){
            prefix = CJYC_CUSTOMER;
        }else{
            prefix = CJYC;
        }
        return prefix;
    }

    public static String getDispatchLockForOrderUpdate(String orderNo) {
        return CJYC + I + DISPATCH_LOCK_ORDER_UPDATE + I + orderNo;
    }

    public static String getLoopAllotAdminKey(Long startStoreId) {
        return CJYC + I + LOOP_ALLOT_ADMIN_KEY + I + startStoreId;
    }

    public static String getCarSeriesKey(String keyword){
        return CJYC + I + CAR_SERIES + I + keyword;
    }

    public static String getPostalKey(String keyword){
        return CJYC + I + KEYWORD_POSTAL_CODE + I + keyword;
    }

    public static String getThreeCityKey(String keyword){
        String key = CJYC + I + THREE_CITY;
        if(StringUtils.isNotBlank(keyword)){
            key = key + I + keyword;
        }
        return key;
    }

    public static String getAppBankInfoKey(BankInfoDto dto){
        return CJYC + I + BANK_INFO + I + dto.getCurrentPage() + I + dto.getPageSize() + I + dto.getKeyword();
    }

    public static String getPayBankInfoKey(PayBankDto dto){
        return CJYC + I + PAY_BANK_INFO + I + dto.getCurrentPage() + I + dto.getPageSize() + I + dto.getBankCode() + I + dto.getSubBankName() + I +dto.getPayBankNo();
    }

    public static String getWebBankInfoKey(KeywordDto dto){
        return CJYC + I + BANK_INFO + I + dto.getKeyword();
    }

    public static String getCityTreeKey(Integer rootLevel, Integer minLeafLevel){
        return CJYC + I + CITY_TREE + I + rootLevel + I + minLeafLevel;
    }

    public static String getKeywordCityTreeKey(String keyword){
        return CJYC + I + KEYWORD_CITY_TREE + I + keyword;
    }

    public static String getCarrierPayKey(Long waybillId){
        return "cjyc:carrier:pay:"+waybillId;
    }

    public static String getOrderRefundKey(Long orderId){
        return "cjyc:order:refund:"+orderId;
    }

    public static String getDispatchLock(String orderCarNo) {
        return CJYC + I + DISPATCH_LOCK_CAR_KEY + I + orderCarNo;
    }
    public static String getAllotTaskKey(Long waybillCarId) {
        return CJYC + I + ALLOT_CAR_LOCK_KEY + I + waybillCarId;
    }
    public static String getLoadLockKey(Long taskCarId) {
        return CJYC + I + LOAD_LOCK_KEY + I + taskCarId;
    }
    public static String getUnloadLockKey(Long waybillCarId) {
        return CJYC + I + UNLOAD_LOCK_KEY + I + waybillCarId;
    }
    public static String getCancelLockKey(Long orderId) {
        return CJYC + I + CANCEL_LOCK_ORDER_KEY + I + orderId;
    }

    public static String getInStoreLockKey(Long taskCarId) {
        return CJYC + I + IN_STORE_LOCK_KEY + I + taskCarId;
    }
    public static String getOutStoreLockKey(Long taskCarId) {
        return CJYC + I + OUT_STORE_LOCK_KEY + I + taskCarId;
    }
    public static String getReceiptLockKey(String no) {
        return CJYC + I + RECEIPT_LOCK_KEY + I + no;
    }
    public static String getOrderLockKey(Long id) {
        return CJYC + I + ORDER_LOCK_KEY + I + id;
    }

    public static String getCheckOrderVin(String orderNo) {
        return CJYC + I + CHECK_VIN_SET + I + orderNo;
    }

    public static String getCheckOrderPlateNo(String orderNo, String plateNo) {
        return CJYC + I + CHECK_PLATENO_SET + I + orderNo + I + plateNo;
    }
}
