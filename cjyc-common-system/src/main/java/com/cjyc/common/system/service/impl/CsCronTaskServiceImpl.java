package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.web.task.DriverCarCountVo;
import com.cjyc.common.system.service.ICsCronTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CsCronTaskServiceImpl implements ICsCronTaskService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private ICustomerCountDao customerCountDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IDriverCarCountDao driverCarCountDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public void saveCustomerOrder() {
        log.info("保存前一天客户下单统计开始执行。。。。。");
        //查询前一天所有下的单
        List<Order> dayOrders = findBeforeDayOrder();
        if(!CollectionUtils.isEmpty(dayOrders)){
            for(Order order : dayOrders){
                CustomerCount count = new CustomerCount();
                count.setCustomerId(order.getCustomerId());
                count.setOrderNo(order.getNo());
                count.setCreateTime(NOW);
                customerCountDao.insert(count);
            }
        }
    }

    @Override
    public void saveDriverCar() {
        log.info("保存前一天司机完成运车统计开始执行。。。。。");
        //获取当天的开始时间
        LocalDateTime dayStartByLong = LocalDateTimeUtil.getDayStartByLong(NOW);
        //获取前一天开始时间
        Long beforeStartDay = TimeStampUtil.subtractDays(LocalDateTimeUtil.getMillisByLDT(dayStartByLong),1);
        //获取前一天结束时间
        Long beforeEndDay = LocalDateTimeUtil.getMillisByLDT(dayStartByLong) - 1;
        List<DriverCarCountVo> driverCars = taskDao.findDriverCarCount(beforeStartDay, beforeEndDay);
        if(!CollectionUtils.isEmpty(driverCars)){
            for(DriverCarCountVo vo : driverCars){
                OrderCar orderCar = orderCarDao.selectById(vo.getOrderCarId());
                DriverCarCount dcc = new DriverCarCount();
                dcc.setCarNum(1);
                dcc.setDriverId(vo.getDriverId());
                dcc.setIncome(orderCar.getTotalFee());
                dcc.setCreateTime(NOW);
                driverCarCountDao.insert(dcc);
            }
        }
    }

    /**
     * 获取前一天所有订单
     * @return
     */
    private List<Order> findBeforeDayOrder(){
        //获取当天的开始时间
        LocalDateTime dayStartByLong = LocalDateTimeUtil.getDayStartByLong(NOW);
        //获取前一天开始时间
        Long beforeStartDay = TimeStampUtil.subtractDays(LocalDateTimeUtil.getMillisByLDT(dayStartByLong),1);
        //获取前一天结束时间
        Long beforeEndDay = LocalDateTimeUtil.getMillisByLDT(dayStartByLong) - 1;
        //查询前一天所有下的单
        return orderDao.findDayOrder(beforeStartDay, beforeEndDay);
    }
}