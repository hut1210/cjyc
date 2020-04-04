package com.cjyc.common.model.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:com.jzg.vin.provider.utils
 * </p>
 * <p>
 * Class_Name:VinCheckUtils
 * </p>
 * <p>
 * Create_Date:2018/1/8
 * </p>
 * <p>
 * Company:JZG
 * </p>
 * <p>
 * Author:yangyf
 * </p>
 */
public class VinCheckUtil {
	private static final String STATUS = "status";
	private VinCheckUtil() {}
    /***************************** 车辆VIN验证 *******************************************/
    public static Map<String, Object> checkVin(String vin) {
        Map<String, Object> map = new HashMap<>();
        if (vin.trim().length() == 17) {
             vin = vin.trim().toLowerCase();
            if (vin.contains("o")) {
                vin = vin.replaceAll("o", "0");
            }
            if (vin.toUpperCase().contains("I") || vin.toUpperCase().contains("Q")) {
                map.put(STATUS, false);
                map.put("msg", "您填写的VIN码中可能包含I、Q 等特殊字符，请重新填写。");
                return map;
            }
            char[] codes = vin.toCharArray();
            int resultInCode = 0;
            if ("0123456789".contains(vin.subSequence(8, 9))) {
                resultInCode = Integer.valueOf(vin.subSequence(8, 9).toString());
            } else {
                if ("x".equals(vin.subSequence(8, 9))) {
                    resultInCode = 10;
                }
            }
            int total = 0;
            for (int i = 1; i < codes.length + 1; i++) {
                char code = codes[i - 1];
                if (kv.containsKey(code)) {
                    if (9 == i) {
                        continue;
                    } else {
                        total += kv.get(code) * wv.get(i);
                    }
                } else {
                    map.put("msg", "验证VIN码出现异常。");
                    map.put(STATUS, false);
                    return map;
                }
            }
            boolean sd = resultInCode == total % 11;
            if (!sd) {
                map.put(STATUS, false);
                map.put("msg", "您填写的VIN码不满足第九位校验规则，请重新填写。");
                return map;
            }
            map.put(STATUS, true);
            return map;
        } else {
            map.put(STATUS, false);
            map.put("msg", "您填写的VIN码不足十七位或者多于十七位，请重新填写。");
            return map;
        }
    }

    private static final Map<Character, Integer> kv = new HashMap<Character, Integer>();

    private static final Map<Integer, Integer> wv = new HashMap<Integer, Integer>();

    static {
        for (int i = 0; i < 10; i++) {
            kv.put(String.valueOf(i).charAt(0), i);
        }
        kv.put('a', 1);

        kv.put('b', 2);

        kv.put('c', 3);

        kv.put('d', 4);

        kv.put('e', 5);

        kv.put('f', 6);

        kv.put('g', 7);

        kv.put('h', 8);

        kv.put('j', 1);

        kv.put('k', 2);

        kv.put('l', 3);

        kv.put('m', 4);

        kv.put('n', 5);

        kv.put('p', 7);

        kv.put('q', 8);

        kv.put('r', 9);

        kv.put('s', 2);

        kv.put('t', 3);

        kv.put('u', 4);

        kv.put('v', 5);

        kv.put('w', 6);

        kv.put('x', 7);

        kv.put('y', 8);

        kv.put('z', 9);

        wv.put(1, 8);

        wv.put(2, 7);

        wv.put(3, 6);

        wv.put(4, 5);

        wv.put(5, 4);

        wv.put(6, 3);

        wv.put(7, 2);

        wv.put(8, 10);

        wv.put(10, 9);

        wv.put(11, 8);

        wv.put(12, 7);

        wv.put(13, 6);

        wv.put(14, 5);

        wv.put(15, 4);

        wv.put(16, 3);

        wv.put(17, 2);
    }
}
