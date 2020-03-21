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

    public static String getMD5ToBase64Str(String str) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < bytes.length; i++) {
                if (Integer.toHexString(0xFF & bytes[i]).length() == 1)
                    sb.append("0").append(Integer.toHexString(0xFF & bytes[i]));
                else
                    sb.append(Integer.toHexString(0xFF & bytes[i]));
            }

            return sb.toString();
        }catch (Exception e) {
            log.error("MD5加密失败，原因：{}", e);
        }
        return null;
    }
}
