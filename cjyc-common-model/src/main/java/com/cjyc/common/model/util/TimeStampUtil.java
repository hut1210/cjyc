package com.cjyc.common.model.util;

import com.cjyc.common.model.constant.TimeConstant;

public class TimeStampUtil {

    public static Long addDays(Long time, int days) {
        return time + days * TimeConstant.MILLS_OF_ONE_DAY;
    }

    /**
     * 功能描述: 给传入的时间加上23小时59分59秒的毫秒值
     * @author liuxingxiang
     * @date 2019/12/16
     * @param time
     * @return java.lang.Long
     */
    public static Long convertEndTime(Long time) {
        return time + TimeConstant.MILLS_OF_ONE_DAY_1;
    }

    /**
     * 获取前一天开始时间
     * @param time
     * @param days
     * @return
     */
    public static Long subtractDays(Long time, int days) {
        return time - days * TimeConstant.MILLS_OF_ONE_DAY;
    }
}
