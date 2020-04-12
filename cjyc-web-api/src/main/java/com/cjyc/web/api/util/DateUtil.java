package com.cjyc.web.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期工具类
 *
 * @Author: RenPL
 * @Date: 2020/04/10
 */
@Slf4j
public class DateUtil {

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat SAM_SDF = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 计算两个日期相差多少天
     *
     * @param endMilTime
     * @param startMilTime
     * @return
     */
    public static int dateDiff(Long endMilTime, Long startMilTime) {
        Long daysBetween = 0L;
        try {
            String startTimeStr = SDF.format(new Date(startMilTime));
            String endTimeStr = SDF.format(new Date(endMilTime));
            if (StringUtils.isEmpty(startTimeStr) || StringUtils.isEmpty(endTimeStr)) {
                throw new RuntimeException("账期剩余天数计算-日期不能为空");
            }
            startTimeStr = startTimeStr.trim().split(" ")[0];
            endTimeStr = endTimeStr.trim().split(" ")[0];
            Date startDate = SAM_SDF.parse(startTimeStr);
            Date endDate = SAM_SDF.parse(endTimeStr);
            daysBetween = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
        } catch (ParseException e) {
            log.error("账期剩余天数-两个日期相差天数计算发成异常", e);
        }
        //返回两个时间点的天数差
        return daysBetween.intValue();
    }

//    public static void main(String[] args) {
//        System.out.println(dateDiff(1586696400000L, 1586700000000L));
//    }
}
