package com.cjyc.common.model.util;

import java.util.Random;

/**
 * 随机数工具
 * @author JPG
 */
public class RandomUtil {


    public static String getRandNumber (int length){
        //第一位随机数
        String temp = "123456789";
        int len = temp.length();
        int p;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        p = r.nextInt(len);
        sb.append(temp.substring(p, p + 1));

        //除第一位以外其他随机数
        for(int i=0;i<length-1;i++){
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }
}