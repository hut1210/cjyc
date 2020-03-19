package com.cjyc.foreign.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author zcm
 * @date 2020/3/18 14:50
 */
@Slf4j
public class MD5Util {

    public static String getMD5Str(String str) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            return new String(bytes, StandardCharsets.UTF_8);
        }catch (Exception e) {
            log.error("MD5加密失败，原因：{}", e);
        }
        return null;
    }
}
