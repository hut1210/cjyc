package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICustomerInvoiceDao;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICustomerInvoiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @Description 发票信息业务接口实现类
 * @Author LiuXingXiang
 * @Date 2019/11/2 10:24
 **/
@Service
public class ICustomerInvoiceServiceImpl extends ServiceImpl<ICustomerInvoiceDao, CustomerInvoice> implements ICustomerInvoiceService {
    @Override
    public ResultVo add(CustomerInvoiceAddDto dto) {
        CustomerInvoice invoice = new CustomerInvoice();
        BeanUtils.copyProperties(dto,invoice);
        invoice.setType(Integer.valueOf(dto.getType()));
        boolean result = super.save(invoice);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }
}
