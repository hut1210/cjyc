package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.salesman.api.service.IOrderCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细（车辆表） 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class OrderCarServiceImpl extends ServiceImpl<IOrderCarDao, OrderCar> implements IOrderCarService {

}
