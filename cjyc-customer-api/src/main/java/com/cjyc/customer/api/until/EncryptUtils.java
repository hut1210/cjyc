package com.cjyc.customer.api.until;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Base64Utils;

/**
 * 加密工具类
 *
 */
public class EncryptUtils {

    /**
     * 获取md5加密值
     * @param value 待加密数据
     * */
    public static String MD5(String value){
        return DigestUtils.md5Hex(value);
    }

    /**
     * encode加密
     * @param data 待加密数据
     * */
    public static String encodeUTF8(String data) throws UnsupportedEncodingException{
        return URLEncoder.encode(data, "UTF-8");
    }

    /**
     * decode解密
     * @param data 待处理数据
     * */
    public static String decodeUTF8(String data) throws UnsupportedEncodingException{
        return URLDecoder.decode(data, "UTF-8");
    }

	/**
	 * md5加密2
	 * @param sourceStr
	 * @return
	 */
	public static String getMD5Str(String sourceStr) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(sourceStr.getBytes("UTF-8"));
            byte b[] = md5.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println(e);
        }
        return result;
    }
	/**
	 * sha1加密
	 * @param decript
	 * @return
	 */
	public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes("UTF-8"));
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return "";
    }
	
	/** 
	 * 
	 * base64加密返回String
	 * @param source
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String base64(String source)
            throws UnsupportedEncodingException {
        return new String(base64(source.getBytes("UTF-8")));
    }
	
	/**
	 * base64加密返回byte[]
	 * @param source
	 * @return
	 */
	public static byte[] base64(byte[] source){
		return Base64Utils.encode(source);
		//return Base64.getEncoder().encode(source);
	}
}
