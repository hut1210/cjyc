package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.model.dao.ICustomerContactDao;
import com.cjyc.salesman.api.service.ICustomerContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户常用联系人表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class CustomerContactServiceImpl extends ServiceImpl<ICustomerContactDao, CustomerContact> implements ICustomerContactService {

}
