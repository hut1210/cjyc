package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICustomerContractDao;
import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.web.api.service.ICustomerContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerContractServiceImpl extends ServiceImpl<ICustomerContractDao, CustomerContract> implements ICustomerContractService {

    @Override
    public boolean saveBatch(List<CustomerContract> list) {
        return super.saveBatch(list);
    }
}