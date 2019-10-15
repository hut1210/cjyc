package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.web.api.service.IOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;



    @Override
    public List<Map<String, Object>> waitDispatchCarCountList() {
        return orderCarDao.countListWaitDispatchCar();
    }

    @Override
    public int totalWaitDispatchCarCount() {
        return orderCarDao.countTotalWaitDispatchCar();
    }
}
