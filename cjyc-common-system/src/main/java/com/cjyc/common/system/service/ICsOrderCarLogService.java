package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.enums.log.OrderLogTypeEnum;

import java.util.List;

public interface ICsOrderCarLogService {

    /**
     * 车辆日志
     * @author JPG
     * @since 2019/12/9 19:59
     * @param orderCarList 车辆列表
     * @param logTypeEnum 日志类型
     * @param log 日志内容
     * @param userInfo 用户信息
     */
    void asyncSave(List<OrderCar> orderCarList, OrderCarLogEnum logTypeEnum, Object[] log, UserInfo userInfo);
}
