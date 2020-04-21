package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderCarLogDao;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.OrderCarLog;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OutterLogVo;
import com.cjyc.common.model.vo.customer.order.OutterOrderCarLogVo;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CsOrderCarLogServiceImpl implements ICsOrderCarLogService {
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderCarLogDao orderCarLogDao;

    /**
     * 车辆日志
     *
     * @param orderCar    车辆
     * @param logTypeEnum 日志类型
     * @param log         日志内容
     * @param userInfo    用户信息
     * @author JPG
     * @since 2019/12/9 19:59
     */
    @Async
    @Override
    public void asyncSave(OrderCar orderCar, OrderCarLogEnum logTypeEnum, String[] log, UserInfo userInfo) {
        LogUtil.debug("【车辆物流日志】------------>开始");
        if (orderCar == null) {
            return;
        }
        try {
            OrderCarLog orderCarLog = new OrderCarLog();
            orderCarLog.setOrderCarId(orderCar.getId());
            orderCarLog.setOrderCarNo(orderCar.getNo());
            orderCarLog.setType(logTypeEnum.getCode());
            orderCarLog.setOuterLog(String.valueOf(log[0]));
            orderCarLog.setInnerLog(String.valueOf(log[1]));
            orderCarLog.setRemark(log.length > 2 ? String.valueOf(log[2]) : null);
            orderCarLog.setCreateTime(System.currentTimeMillis());
            if (userInfo != null) {
                orderCarLog.setCreateUser(userInfo.getName());
                orderCarLog.setCreateUserId(userInfo.getId());
                orderCarLog.setCreateUserPhone(userInfo.getPhone());
                orderCarLog.setCreateUserType(userInfo.getUserType() == null ? null : userInfo.getUserType().code);
            }
            LogUtil.debug("【车辆物流日志】" + JSON.toJSONString(orderCarLog));
            orderCarLogDao.insert(orderCarLog);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void asyncSaveBatch(Set<OrderCar> orderCarList, OrderCarLogEnum logTypeEnum, String[] log, UserInfo userInfo) {
        if (CollectionUtils.isEmpty(orderCarList)) {
            return;
        }
        try {
            orderCarList.forEach(orderCar -> asyncSave(orderCar, logTypeEnum, log, userInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void asyncSaveBatch(List<WaybillCar> wcs, OrderCarLogEnum logTypeEnum, String[] log, UserInfo userInfo) {
        if (CollectionUtils.isEmpty(wcs)) {
            return;
        }
        List<OrderCarLog> ocls = Lists.newArrayList();
        for (WaybillCar wc : wcs) {
            OrderCarLog orderCarLog = new OrderCarLog();
            orderCarLog.setOrderCarId(wc.getOrderCarId());
            orderCarLog.setOrderCarNo(wc.getOrderCarNo());
            orderCarLog.setType(logTypeEnum.getCode());
            orderCarLog.setOuterLog(String.valueOf(log[0]));
            orderCarLog.setInnerLog(String.valueOf(log[1]));
            orderCarLog.setRemark(log.length > 2 ? String.valueOf(log[2]) : null);
            orderCarLog.setCreateTime(System.currentTimeMillis());
            if (userInfo != null) {
                orderCarLog.setCreateUser(userInfo.getName());
                orderCarLog.setCreateUserId(userInfo.getId());
                orderCarLog.setCreateUserPhone(userInfo.getPhone());
                orderCarLog.setCreateUserType(userInfo.getUserType() == null ? null : userInfo.getUserType().code);
            }
            ocls.add(orderCarLog);
        }

        saveBatch(ocls);
    }

    private void saveBatch(List<OrderCarLog> ocls) {
        orderCarLogDao.insertBatch(ocls);
    }

    @Override
    public ResultVo<OutterLogVo> getOrderCarLog(String orderCarNo) {
        OutterLogVo outterLogVo = new OutterLogVo();
        String state = orderCarDao.findOutterState(orderCarNo);
        outterLogVo.setOutterState(state);
        outterLogVo.setOrderCarNo(orderCarNo);
        List<OutterOrderCarLogVo> list = orderCarLogDao.findCarLogByOrderNoAndCarNo(orderCarNo.split("-")[0], orderCarNo);
        outterLogVo.setList(list);
        return BaseResultUtil.success(outterLogVo);
    }

    @Override
    public void asyncSaveBatchForReleaseCar(Set<String> orderCarNoSet, Integer type) {
        if (CollectionUtils.isEmpty(orderCarNoSet)) {
            return;
        }
        List<OrderCar> list = orderCarDao.findListByNos(orderCarNoSet);
        List<OrderCarLog> ocls = Lists.newArrayList();
        for (OrderCar oc : list) {
            OrderCarLog orderCarLog = new OrderCarLog();
            orderCarLog.setOrderCarId(oc.getId());
            orderCarLog.setOrderCarNo(oc.getNo());
            OrderCarLogEnum ocLogEnum;
            switch (type) {
                case 1:
                    ocLogEnum = OrderCarLogEnum.C_RELEASE_UNPAID;
                    break;
                case 2:
                    ocLogEnum = OrderCarLogEnum.C_UNRELEASE_PAID;
                    break;
                case 9:
                    ocLogEnum = OrderCarLogEnum.C_RELEASE_PAID;
                    break;
                default:
                    ocLogEnum = OrderCarLogEnum.C_UNRELEASE_UNPAID;
            }

            orderCarLog.setType(ocLogEnum.getCode());
            orderCarLog.setOuterLog(String.valueOf(ocLogEnum.getOutterLog()));
            orderCarLog.setInnerLog(String.valueOf(ocLogEnum.getInnerLog()));
            orderCarLog.setCreateTime(System.currentTimeMillis());
            ocls.add(orderCarLog);
        }

        saveBatch(ocls);
    }
}
