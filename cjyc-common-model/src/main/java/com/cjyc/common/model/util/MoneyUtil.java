package com.cjyc.common.model.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyUtil {
    public static final String PATTERN_TWO = "#0.00#";
    private static final DecimalFormat DF_TWO = new DecimalFormat(PATTERN_TWO);

    public static BigDecimal yuanToFen(BigDecimal fee) {
        return fee == null ? null : fee.multiply(new BigDecimal(100));
    }
    public static BigDecimal fenToYuan(BigDecimal fee) {
        return fee == null ? null : fee.divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }

    public static String fenToYuan(BigDecimal fee, String pattern) {
        DecimalFormat df = PATTERN_TWO.equals(pattern) ? DF_TWO : new DecimalFormat(pattern);
        BigDecimal bd = fee == null ? BigDecimal.ZERO : fee.divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN);
        return df.format(bd);
    }

    public static BigDecimal nullToZero(BigDecimal fee) {
        return fee == null ? BigDecimal.ZERO : fee;
    }

}
