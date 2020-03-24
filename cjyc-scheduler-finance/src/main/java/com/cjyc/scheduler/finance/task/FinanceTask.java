package com.cjyc.scheduler.finance.task;

import com.cjyc.common.system.service.ICsOrderService;
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

    @Autowired
    private ICsOrderService orderService;

    //@Scheduled(cron = "0/90 * * * * ?")
    private void task() {

        log.info("定时更新订单是支付中账单是待支付的");
        //查询订单更新支付中单子状态
        try {
            csTransactionService.getPayingOrder();
        } catch (Exception e) {
            log.error("查询订单中单子失败 {}", e.getMessage());
        }

    }

    /**
     * 每天结束时【凌晨】定时把所有订单的剩余账期(天)减一天
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void paymentDaysTask() {
        log.info("每天结束时定时把所有订单的剩余账期(天)减一天");
        try {
            //orderService.paymentDaysSubtraction();
        } catch (Exception e) {
            log.error("剩余账期(天)减一天失败!", e);
        }

    }
}
