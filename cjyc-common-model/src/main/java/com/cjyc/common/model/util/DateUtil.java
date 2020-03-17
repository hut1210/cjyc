package com.cjyc.common.model.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date parseDate(String source) {
        if(source == null || source.isEmpty()){
            return null;
        }

        int index = 0;
        StringBuffer pattern = new StringBuffer();
        char[] chars = source.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                switch (index) {
                    case 0:
                        pattern.append("y");
                        break;
                    case 1:
                        pattern.append("M");
                        break;
                    case 2:
                        pattern.append("d");
                        break;
                    case 3:
                        pattern.append("H");
                        break;
                    case 4:
                        pattern.append("m");
                        break;
                    case 5:
                        pattern.append("s");
                        break;
                    case 6:
                        pattern.append("S");
                        break;
                    default:
                }
            } else{
                pattern.append(chars[i]);
                index++;
            }
        }

        try {
            return new SimpleDateFormat(pattern.toString()).parse(source);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(parseDate("2016年12月11日").getTime());
        System.out.println(parseDate("16年2月11日").getTime());
        System.out.println(parseDate("16年2月11日11点22分").getTime());
        System.out.println(parseDate("16-2-11 11点22分50秒").getTime());
        System.out.println(parseDate("016/2_11/11/22/50").getTime());
        System.out.println(parseDate("2016/02/11日 11-22-50").getTime());
        System.out.println(parseDate("2016/02/11添加干扰文字试试").getTime());
    }
}
