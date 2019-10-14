package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerContactDao;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.service.service.ICustomerContactService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service.impl
 * @date:2019/10/9
 */
@Service("customerContactService")
public class CustomerContactServiceImpl implements ICustomerContactService {

    @Resource
    private ICustomerContactDao customerContactDao;
    @Resource
    private IIncrementerDao incrementerDao;

    @Override
    public boolean addCustomerContact(CustomerContact entity) {
        String no = incrementerDao.getIncrementer("ie");
        System.out.print("+++++++++++>>"+no);
        //int re = customerContactDao.insert(entity);
        //return re > 0 ? true : false;
        return false;
    }

    @Override
    public boolean updCustomerContact(CustomerContact customerContact) {
        int re = customerContactDao.updateById(customerContact);
        return re > 0 ? true : false;
    }

    @Override
    public PageInfo<CustomerContact> getContactPage(Long customerId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<CustomerContact> selectList = customerContactDao.selectList(
                new QueryWrapper<CustomerContact>().eq("id",customerId));
        PageInfo<CustomerContact> pageInfo = new PageInfo<>(selectList);
        return pageInfo;
    }
}