package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.CustomerPoint;
import com.cjyc.common.model.dao.ICustomerPointDao;
import com.cjyc.salesman.api.service.ICustomerPointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户积分表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class CustomerPointServiceImpl extends ServiceImpl<ICustomerPointDao, CustomerPoint> implements ICustomerPointService {

}
