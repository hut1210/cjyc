package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.common.model.dao.ICustomerContractDao;
import com.cjyc.salesman.api.service.ICustomerContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户（企业）合同表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class CustomerContractServiceImpl extends ServiceImpl<ICustomerContractDao, CustomerContract> implements ICustomerContractService {

}
