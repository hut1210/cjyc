package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.order.OrderLogTypeEnum;

import java.util.List;

public interface ICsOrderCarLogService {

    void asyncSave(List<OrderCar> orderCarList, OrderLogTypeEnum receipt, Object[] objects, UserInfo userInfo);
}
