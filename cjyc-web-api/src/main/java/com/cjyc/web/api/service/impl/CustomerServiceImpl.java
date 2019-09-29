package com.cjyc.web.api.service.impl;

import com.cjyc.common.base.ResultEnum;
import com.cjyc.common.base.ResultVo;
import com.cjyc.common.model.entity.auto.Customer;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.web.api.dto.CustomerDto;
import com.cjyc.web.api.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:20
 *  @Description: 移动用户
 */ 
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CustomerServiceImpl implements ICustomerService{

    @Autowired
    private ICustomerDao iCustomerDao;

    @Override
    public ResultVo saveCustomer(CustomerDto customerDto) {
        try{
            Customer customer = new Customer();
            customer.setName(customerDto.getName());
            customer.setPhone(customerDto.getPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            iCustomerDao.insert(customer);
        }catch (Exception e){
            log.error("新增用户出现异常",e);
            return ResultVo.response(ResultEnum.FAIL.getCode(),"新增移动用户出现异常");
        }
        return ResultVo.response(ResultEnum.SUCCESS.getCode(),"新增移动用户成功");
    }
}
