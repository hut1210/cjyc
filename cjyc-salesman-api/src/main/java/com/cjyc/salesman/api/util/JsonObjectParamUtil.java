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
        return getString(params, key, false);
    }

    public static String getString(JSONObject params, String key, Boolean required) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String value = params.getString(key);
        ValidateParam(key, value, required);
        return value;
    }

    public static Long getLong(JSONObject params, String key) {
        return getLong(params, key, false);
    }

    public static Long getLong(JSONObject params, String key, Boolean required) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Long value = params.getLong(key);
        ValidateParam(key, value, required);
        return value;
    }


    public static Integer getInt(JSONObject params, String key) {
        return getInt(params, key, false);
    }

    public static Integer getInt(JSONObject params, String key, Boolean required) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Integer value = params.getInteger(key);
        ValidateParam(key, value, required);
        return value;
    }

    public static Double getDouble(JSONObject params, String key) {
        return getDouble(params, key, false);
    }

    public static Double getDouble(JSONObject params, String key, Boolean required) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Double value = params.getDouble(key);
        ValidateParam(key, value, required);
        return value;
    }


    public static BigDecimal getBigDecimal(JSONObject params, String key) {
        return getBigDecimal(params, key, false);
    }

    public static BigDecimal getBigDecimal(JSONObject params, String key, Boolean required) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        BigDecimal value = params.getBigDecimal(key);
        ValidateParam(key, value, required);
        return value;
    }

    private static void ValidateParam(String key, Object value, Boolean required) throws ParameterException {
        if (required) {
            if (null == value || "".equals(value)) {
                throw new ParameterException("{0}参数不能为空", key == null ? "" : key);
            }
        }
    }

}
