package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.dao.ICustomerDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.customer.api.service.ICustomerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表（登录用户端APP用户） 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-18
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<ICustomerDao, Customer> implements ICustomerService {

}
