package com.cjyc.customer.api.service;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.vo.customer.OrderCarCenterVo;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:35
 *  @Description:订单明细（车辆信息）
 */
public interface IOrderCarService {

    /**
     *  通过车辆id查看车辆信息
     * @param orderCarId
     * @return
     */
    OrderCarCenterVo getOrderCarInfoById(Long orderCarId);

}
