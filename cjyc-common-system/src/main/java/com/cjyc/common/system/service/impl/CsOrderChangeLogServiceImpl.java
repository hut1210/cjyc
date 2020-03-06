package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.entity.defined.FullOrder;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.system.service.ICsOrderChangeLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class CsOrderChangeLogServiceImpl implements ICsOrderChangeLogService {

    @Resource
    private IOrderChangeLogDao orderChangeLogDao;

    @Async
    @Override
    public void asyncSave(Order order, OrderChangeTypeEnum changeType, String[] content, UserInfo userInfo) {
        try {
            OrderChangeLog orderChangeLog = new OrderChangeLog();
            orderChangeLog.setOrderNo(order.getNo());
            orderChangeLog.setOrderId(order.getId());
            orderChangeLog.setName(changeType.name);
            orderChangeLog.setType(changeType.code);
            orderChangeLog.setOldContent(content[0]);
            orderChangeLog.setNewContent(content[1]);
            orderChangeLog.setReason(content[2]);
            orderChangeLog.setState(CommonStateEnum.CHECKED.code);
            orderChangeLog.setCreateTime(System.currentTimeMillis());
            orderChangeLog.setCreateUserId(userInfo.getId());
            orderChangeLog.setCreateUser(userInfo.getName());
            orderChangeLogDao.insert(orderChangeLog);
        } catch (NumberFormatException e) {
            LogUtil.error(e.getMessage(),e);
        }
    }

    @Async
    @Override
    public void asyncSaveForChangePrice(FullOrder oldOrder, FullOrder newOrder, String reason, UserInfo userInfo) {
        try {
            if(!validateIsChangePrivce(oldOrder, newOrder)){
                return;
            }

            OrderChangeLog orderChangeLog = new OrderChangeLog();
            orderChangeLog.setOrderNo(newOrder.getNo());
            orderChangeLog.setOrderId(newOrder.getId());
            orderChangeLog.setName(OrderChangeTypeEnum.CHANGE_FEE.name);
            orderChangeLog.setType(OrderChangeTypeEnum.CHANGE_FEE.code);
            orderChangeLog.setOldContent(JSON.toJSONString(oldOrder));
            orderChangeLog.setNewContent(JSON.toJSONString(newOrder));
            orderChangeLog.setReason(reason);
            orderChangeLog.setState(CommonStateEnum.CHECKED.code);
            orderChangeLog.setCreateTime(System.currentTimeMillis());
            if(userInfo != null){
                orderChangeLog.setCreateUserId(userInfo.getId());
                orderChangeLog.setCreateUser(userInfo.getName());
            }
            orderChangeLogDao.insert(orderChangeLog);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(),e);
        }

    }

    private boolean validateIsChangePrivce(FullOrder oldOrder, FullOrder newOrder) {
        if(oldOrder == null || newOrder == null){
            return false;
        }
        if(MoneyUtil.nullToZero(oldOrder.getTotalFee()).compareTo(MoneyUtil.nullToZero(newOrder.getTotalFee())) != 0){
            return true;
        }

        int oldSize = oldOrder.getList() == null ? 0 : oldOrder.getList().size();
        int newSize = newOrder.getList() == null ? 0 : newOrder.getList().size();
        if(oldSize != newSize){
            return true;
        }
        if(oldSize == 0){
            return false;
        }

        BigDecimal osumFee = BigDecimal.ZERO;
        for (OrderCar car : oldOrder.getList()) {
            osumFee = osumFee.add(getSumFee(car));
        }
        BigDecimal nsumFee = BigDecimal.ZERO;
        for (OrderCar car : newOrder.getList()) {
            osumFee = nsumFee.add(getSumFee(car));
        }
        if(osumFee.compareTo(nsumFee) == 0){
            return true;
        }
        return false;
    }

    private BigDecimal getSumFee(OrderCar car) {
        return BigDecimal.ZERO.add(car.getPickFee() == null ? BigDecimal.ZERO : car.getPickFee())
                .add(car.getTrunkFee() == null ? BigDecimal.ZERO : car.getTrunkFee())
                .add(car.getBackFee() == null ? BigDecimal.ZERO : car.getBackFee())
                .add(car.getAddInsuranceFee() == null ? BigDecimal.ZERO : car.getAddInsuranceFee());
    }


}
