package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.salesman.api.service.IMineService;
import org.springframework.stereotype.Service;

@Service
public class MineServiceImpl extends ServiceImpl<IOrderCarDao, OrderCar> implements IMineService {
}