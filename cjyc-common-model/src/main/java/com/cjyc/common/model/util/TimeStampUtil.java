package com.cjyc.common.model.util;

import com.cjyc.common.model.constant.TimeConstant;

public class TimeStampUtil {

    public static Long addDays(Long time, int days) {
        return time + days * TimeConstant.MILLS_OF_ONE_DAY;
    }
}
