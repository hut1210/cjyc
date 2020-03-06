package com.cjyc.common.model.util;

import java.math.BigDecimal;

public class MoneyUtil {

    public static BigDecimal yuanToFen(BigDecimal fee) {
        return fee == null ? null : fee.multiply(new BigDecimal(100));
    }

    public static BigDecimal fenToYuan(BigDecimal fee) {
        return fee == null ? null : fee.divide(new BigDecimal(100));
    }
}
