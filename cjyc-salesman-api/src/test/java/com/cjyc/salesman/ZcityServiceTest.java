package com.cjyc.salesman;

import com.cjyc.salesman.api.SalesmanApiApplication;
import com.cjyc.salesman.api.service.ZcityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesmanApiApplication.class)
public class ZcityServiceTest {

    @Resource
    private ZcityService zcityService;

    @Test
    public void run(){
        zcityService.changeArea();

    }

}