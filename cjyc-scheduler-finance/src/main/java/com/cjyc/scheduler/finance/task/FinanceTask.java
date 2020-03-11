package com.cjyc.scheduler.finance.task;

import com.cjyc.common.system.service.ICsTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: Hut
 * @Date: 2020/03/08 9:30
 */
@Component
@EnableScheduling
@Configuration
@Slf4j
public class FinanceTask {
    @Autowired
    private ICsTransactionService csTransactionService;
    //@Scheduled(cron = "0/90 * * * * ?")
    private void task() {

        log.info("定时更新订单是支付中账单是待支付的");
        //查询订单更新支付中单子状态
        try{
            csTransactionService.getPayingOrder();
        }catch (Exception e){
            log.error("查询订单中单子失败 {}",e.getMessage());
        }

    }
}
