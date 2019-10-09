package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.dao.ICustomerInvoiceDao;
import com.cjyc.salesman.api.service.ICustomerInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发票信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class CustomerInvoiceServiceImpl extends ServiceImpl<ICustomerInvoiceDao, CustomerInvoice> implements ICustomerInvoiceService {

}
