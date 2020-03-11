package com.cjyc.foreign.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dto.req.OrderSaveReqDto;
import com.cjyc.foreign.api.service.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResultVo<String> saveOrder(OrderSaveReqDto dto) {
        // 保存订单

        // 保存订单车辆信息
        return null;
    }
}
