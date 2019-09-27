package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.customer.api.dao.CustomerMapper;
import com.cjyc.customer.api.entity.Customer;
import com.cjyc.customer.api.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by leo on 2019/7/23.
 */
@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 新增客户信息
     *
     * */
    @Override
    public void addUser() {
        Customer c = Customer.getInstance();
        c.setToken("1111");
        c.setOpenId("dfdf");
        c.setAlias("sdfdf");
        c.setName("oaofd");
        c.setFirstLetter("F");
        c.setPhone("123123132132");
        c.setPwd("10000");
        customerMapper.insert(c);
    }

    /**
     * 测试分页
     * @return
     */
    @Override
    public PageInfo<Customer> pageList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Customer> selectList = customerMapper.selectList(new QueryWrapper<>());
        PageInfo<Customer> pageInfo = new PageInfo<>(selectList);
        return pageInfo;
    }


}
