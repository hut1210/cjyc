package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.Set;

public interface ICsOrderCarLogService {

    /**
     * 车辆日志
     * @author JPG
     * @since 2019/12/9 19:59
     * @param orderCar 车辆
     * @param logTypeEnum 日志类型
     * @param log 日志内容
     * @param userInfo 用户信息
     */
    void asyncSave(OrderCar orderCar, OrderCarLogEnum logTypeEnum, String[] log, UserInfo userInfo);

    /**
     * 车辆日志
     * @author JPG
     * @since 2019/12/9 19:59
     * @param orderCarList 车辆列表
     * @param logTypeEnum 日志类型
     * @param log 日志内容
     * @param userInfo 用户信息
     */
    void asyncSaveBatch(Collection<WaybillCar> wcs, OrderCarLogEnum logTypeEnum, String[] log, UserInfo userInfo);

    /**
     * 功能描述: 根据订单车辆编号查询车辆运输日志
     * @author liuxingxiang
     * @date 2020/4/2
     * @param orderCarNo
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.customer.order.OutterLogVo>
     */
    ResultVo<OutterLogVo> getOrderCarLog(String orderCarNo);

    @Async
    void asyncSaveBatchForReleaseCar(Set<String> orderCarNoSet, Integer type);
}
