package com.pingplusplus.net;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.InvalidRequestException;

import java.io.UnsupportedEncodingException;

public abstract class AppBasedResource extends APIResource {
    /**
     * @param clazz
     * @return singleClassURL
     * @throws InvalidRequestException
     */
    protected static String singleClassURL(Class<?> clazz) throws InvalidRequestException {
        if (Pingpp.appId == null) {
            throw new InvalidRequestException("Please set app_id using Pingpp.appId = <APP_ID>", "app_id", null);
        }
        return String.format("%s/v1/apps/%s/%s", Pingpp.getApiBase(), Pingpp.appId, className(clazz));
    }

    /**
     * @param clazz
     * @return classURL
     * @throws InvalidRequestException
     */
    protected static String classURL(Class<?> clazz) throws InvalidRequestException {
        return String.format("%ss", singleClassURL(clazz));
    }

    /**
     * @param clazz
     * @param id
     * @return instanceURL
     * @throws InvalidRequestException
     */
    protected static String instanceURL(Class<?> clazz, String id) throws InvalidRequestException {
        try {
            return String.format("%s/%s", classURL(clazz), urlEncode(id));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
        }
    }

    /**
     * @param objectName
     * @return customURL
     * @throws InvalidRequestException
     */
    protected static String customURL(String objectName) throws InvalidRequestException {
        if (Pingpp.appId == null) {
            throw new InvalidRequestException("Please set app_id using Pingpp.appId = <APP_ID>", "app_id", null);
        }
        return String.format("%s/v1/apps/%s/%s", Pingpp.getApiBase(), Pingpp.appId, objectName);
    }
}
