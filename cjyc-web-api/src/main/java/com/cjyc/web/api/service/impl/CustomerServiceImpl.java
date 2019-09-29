package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
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
            Customer customer = Customer.getInstance();
            customer.setName(customerDto.getName());
            customer.setPhone(customerDto.getPhone());
            customer.setIdCard(customerDto.getIdCard());
            customer.setIdCardFrontImg(customerDto.getIdCardFrontImg());
            customer.setIdCardBackImg(customerDto.getIdCardBackImg());
            iCustomerDao.insert(customer);
        }catch (Exception e){
            log.error("新增用户出现异常",e);
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"新增移动用户处理异常");
        }
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),"新增移动用户成功");
    }
}
