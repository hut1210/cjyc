package com.cjyc.customer.api.task;

import com.cjkj.common.utils.DateUtil;
import com.cjyc.customer.api.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author:Hut
 * @Date:2019/12/04 13:47
 */
@Component
@EnableScheduling
@Configuration
public class TradeBillTask {

    @Autowired
    private ITransactionService transactionService;

    //@Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void task() {
        transactionService.cancelExpireTrade();
    }
}
