package com.cjyc.server.pay.task;

import com.cjyc.common.system.service.ICsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: Hut
 * @Date: 2020/03/08 9:48
 */

@Component
@EnableScheduling
@Configuration
@Slf4j
public class PayTask {

    @Autowired
    private ICsTransactionService csTransactionService;

    @Scheduled(cron = "0/60 * * * * ?")
    private void task() {
        log.info("定时给合伙人打款");
        try {
            csTransactionService.payToCooperator();
        }catch (Exception e){
            log.error("定时打款失败 {}",e.getMessage());
        }

    }
}
