package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.salesman.api.dto.OrderDto;
import com.cjyc.salesman.api.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表(客户下单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Override
    public boolean confirm(OrderDto orderDto) {
        return false;
    }
}
