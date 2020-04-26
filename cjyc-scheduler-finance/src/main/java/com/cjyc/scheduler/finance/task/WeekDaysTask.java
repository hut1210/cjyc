package com.cjyc.scheduler.finance.task;

import com.cjyc.common.system.service.IWeekDaysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * @Package: com.cjyc.scheduler.finance.task
 * @Description: 首先初始化当前年的工作日和节假日，下一年的除周六日的工作日
 * @Author: Yang.yanfei
 * @Date: 2020/4/16
 * @Version: V1.0
 * @Copyright: 2019 - 2020 - ©长久科技
 */
@Component
@EnableScheduling
@Configuration
@Slf4j
public class WeekDaysTask {
    @Autowired
    private IWeekDaysService weekDaysService;

    /**
     * 每年12月1号更新当前年加1年的工作日信息
     */
   // @Scheduled(cron = "0 0 1 1 12 ?")
    private void updateWeekDaysTypeInfo() {
        // 调用百度提供的接口修改下一年的工作日
        log.info("开始调用百度提供的节假日查询接口更新下一年的节假日");
        weekDaysService.updateWeekDaysTypeInfo(LocalDateTime.now().getYear() + Calendar.FEBRUARY);
        log.info("调用百度提供的节假日查询接口更新下一年的节假日结束");
    }

    /**
     * 新增当前年+2年的日期
     * 初始化的时候插入的是当前年+1年的日期
     * 正式环境的话是需要在当前年12月1号更新了下一年的节假日后执行下下年的
     */
    // @Scheduled(cron = "0 0 3 1 12 ?")
    private void insertWeekDaysInfo() {
        log.info("开始执行在f_weekdays表插入当前日期第二年的所有日期的任务");
        weekDaysService.insertWeekDaysInfo(LocalDateTime.now().getYear() + Calendar.LONG);
        log.info("执行在f_weekdays表插入当前日期第二年的所有日期的任务结束");
    }

    /**
     * 修改当前年下下一年的周六日
     */
    // @Scheduled(cron = "0 0 4 1 12 ?")
    private void updateDateSun() {
        log.info("开始执行修改当前年下下年为周六日的时间为休息日");
        weekDaysService.updateDateSun(2);
        log.info("执行修改当前年下下年为周六日的时间为休息日结束");
    }


}
