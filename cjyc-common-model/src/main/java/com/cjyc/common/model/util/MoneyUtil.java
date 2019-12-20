package com.cjyc.common.model.util;

import java.math.BigDecimal;

public class MoneyUtil {

    public static BigDecimal convertYuanToFen(BigDecimal fee) {
        return fee == null ? null : fee.multiply(new BigDecimal(100));
    }
}
