package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerContactDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContact;
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
    private ICustomerContactDao customerContactDao;

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

    /**
     * 测试分页
     * @return
     */
    @Override
    public PageInfo<Customer> pageList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> selectList = customerDao.selectList(new QueryWrapper<>());
        PageInfo<Customer> pageInfo = new PageInfo<>(selectList);
        return pageInfo;
    }
}
