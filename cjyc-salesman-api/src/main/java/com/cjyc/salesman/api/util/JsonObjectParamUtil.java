package com.cjyc.salesman.api.util;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.salesman.api.exception.ParameterException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * JsonObject参数获取和验证
 * @author JPG
 */
public class JsonObjectParamUtil {

    public static String getString(JSONObject params, String key) {
        return getString(params, key, false, null);
    }

    public static String getString(JSONObject params, String key, Boolean required) {
        return getString(params, key, required, null);
    }

    public static String getString(JSONObject params, String key, String defaultValue) {
        return getString(params, key, false, defaultValue);
    }

    public static String getString(JSONObject params, String key, Boolean required, String defaultValue) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String value = params.getString(key);
        if(value == null || "".equals(value)){
            value = defaultValue == null ? value : defaultValue;
        }
        ValidateParam(key, value, required);
        return value;
    }


    public static Long getLong(JSONObject params, String key) {
        return getLong(params, key, false, null);
    }

    public static Long getLong(JSONObject params, String key, Boolean required) {
        return getLong(params, key, required, null);
    }

    public static Long getLong(JSONObject params, String key, Long defaultValue) {
        return getLong(params, key, false, defaultValue);
    }

    public static Long getLong(JSONObject params, String key, Boolean required, Long defaultValue) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Long value = params.getLong(key);
        if(value == null){
            value = defaultValue == null ? value : defaultValue;
        }
        ValidateParam(key, value, required);
        return value;
    }


    public static Integer getInt(JSONObject params, String key) {
        return getInt(params, key, false, null);
    }

    public static Integer getInt(JSONObject params, String key, Boolean required) {
        return getInt(params, key, required, null);
    }

    public static Integer getInt(JSONObject params, String key, Integer defaultValue) {
        return getInt(params, key, false, defaultValue);
    }

    public static Integer getInt(JSONObject params, String key, Boolean required, Integer defaultValue) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Integer value = params.getInteger(key);
        if(value == null){
            value = defaultValue == null ? value : defaultValue;
        }
        ValidateParam(key, value, required);
        return value;
    }

    public static Double getDouble(JSONObject params, String key) {
        return getDouble(params, key, false);
    }

    public static Double getDouble(JSONObject params, String key, Boolean required) {
        return getDouble(params, key, required, null);
    }

    public static Double getDouble(JSONObject params, String key, Double defaultValue) {
        return getDouble(params, key, false, defaultValue);
    }

    public static Double getDouble(JSONObject params, String key, Boolean required, Double defaultValue) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Double value = params.getDouble(key);
        if(value == null){
            value = defaultValue == null ? value : defaultValue;
        }
        ValidateParam(key, value, required);
        return value;
    }


    public static BigDecimal getBigDecimal(JSONObject params, String key) {
        return getBigDecimal(params, key, false);
    }

    public static BigDecimal getBigDecimal(JSONObject params, String key, Boolean required) {
        return getBigDecimal(params, key, required, null);
    }

    public static BigDecimal getBigDecimal(JSONObject params, String key, BigDecimal defaultValue) {
        return getBigDecimal(params, key, false, defaultValue);
    }

    public static BigDecimal getBigDecimal(JSONObject params, String key, Boolean required, BigDecimal defaultValue) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        BigDecimal value = params.getBigDecimal(key);
        ValidateParam(key, value, required);
        return value;
    }

    private static void ValidateParam(String key, Object value, Boolean required) throws ParameterException {
        if(required == null){
            required = false;
        }
        if (required) {
            if (null == value || "".equals(value)) {
                throw new ParameterException("{0}参数不能为空", key == null ? "" : key);
            }
        }
    }

}
