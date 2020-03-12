package com.cjyc.server.pay;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

public class test {



    public static void main(String[] args) {
        BigDecimal a1 = new BigDecimal(1111);
        BigDecimal a2 = new BigDecimal(1000);
        BigDecimal a3 = new BigDecimal(1000);
        BigDecimal a4 = new BigDecimal(1000);
        BigDecimal a5 = new BigDecimal(1000);
        BigDecimal fee = new BigDecimal(2004);

        BigDecimal sum = a1.add(a2).add(a3);

        BigDecimal r1 = a1.divide(sum, 10, RoundingMode.DOWN);
        BigDecimal r2 = a2.divide(sum, 10, RoundingMode.DOWN);

        BigDecimal n1 = r1.multiply(fee).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal n2 = r2.multiply(fee).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal n3 = fee.subtract(n1).subtract(n2);
        System.out.println(n1);
        System.out.println(n2);
        System.out.println(n3);

        Map<String, Object> map = Maps.newHashMap();
        for(int i = 0 ; i < 5 ; i++ ){
            map.put("r" + i, 55);
        }
        //computeUnabsoluteAvg(new BigDecimal(1000), map);


    }

    private static void computeUnabsoluteAvg(BigDecimal total, TreeMap<String, BigDecimal> args) {
        BigDecimal sum = BigDecimal.ZERO;
        //args.values().stream().map(bigDecimal -> {}).reduce()
        for (Map.Entry<String, BigDecimal> entry : args.entrySet()) {
            sum = sum.add(entry.getValue() == null ? BigDecimal.ZERO : entry.getValue());
        }
        for (Map.Entry<String, BigDecimal> entry : args.entrySet()) {

        }
    }
}
