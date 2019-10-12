package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerContactDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ICustomerLineDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.model.entity.CustomerLine;
import com.cjyc.common.service.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by leo on 2019/7/23.
 */
@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {

    @Resource
    private ICustomerDao customerDao;

    @Resource
    private ICustomerLineDao customerLineDao;

    /**
     * 新增客户信息
     *
     * */
    @Override
    public void addUser() {
        Customer c = new Customer();
        c.setAlias("sdfasf");
        customerDao.insert(c);
    }

    @Override
    public PageInfo<CustomerLine> getLineHistoryPage(Long customerId, Integer pageNum, Integer pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        PageHelper.startPage(pageNum, pageSize);
        queryWrapper.eq("customer_id",customerId);
        List<CustomerLine> selectList = customerLineDao.selectList(queryWrapper);
        PageInfo<CustomerLine> pageInfo = new PageInfo<>(selectList);
        return pageInfo;
    }
}
