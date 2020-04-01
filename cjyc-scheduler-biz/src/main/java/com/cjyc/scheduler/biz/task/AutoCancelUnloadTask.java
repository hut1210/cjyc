package com.cjyc.scheduler.biz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 半小时未入库，自动取消交车
 * @author JPG
 */
@Component
@Slf4j
public class AutoCancelUnloadTask {


    @Scheduled(cron = "0 0/30 * * * ?")
    public void autoCancelUnload(){

    }

}
