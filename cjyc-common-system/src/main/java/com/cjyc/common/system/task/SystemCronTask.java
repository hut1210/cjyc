package com.cjyc.common.system.task;

import com.cjyc.common.system.service.ICsCronTaskService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableScheduling
@Configuration
public class SystemCronTask {

    @Resource
    private ICsCronTaskService csCronTaskService;

    /**
     * 每天凌晨一点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    void saveCustomerOrder(){
        csCronTaskService.saveCustomerOrder();
    }

    /**
     * 每天凌晨两点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    void saveDriverCar(){
        csCronTaskService.saveDriverCar();
    }

    /**
     * 每天凌晨三点执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    void saveCustomerLine(){
        csCronTaskService.saveCustomerLine();
    }
}