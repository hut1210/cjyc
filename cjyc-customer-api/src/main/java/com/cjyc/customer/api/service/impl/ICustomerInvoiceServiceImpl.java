package com.cjyc.customer.api.service.impl;

import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.ICustomerInvoiceDao;
import com.cjyc.common.model.dao.IInvoiceApplyDao;
import com.cjyc.common.model.dao.IInvoiceOrderConDao;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.dto.customer.invoice.OrderAmountDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.entity.InvoiceOrderCon;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICustomerInvoiceService;
import com.cjyc.customer.api.service.IInvoiceApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @Description 发票信息业务接口实现类
 * @Author LiuXingXiang
 * @Date 2019/11/2 10:24
 **/
@Service
public class ICustomerInvoiceServiceImpl extends SuperServiceImpl<ICustomerInvoiceDao, CustomerInvoice> implements ICustomerInvoiceService {
    @Autowired
    private IInvoiceApplyDao invoiceApplyDao;
    @Autowired
    private IInvoiceApplyService invoiceApplyService;
    @Autowired
    private IInvoiceOrderConDao invoiceOrderConDao;

    @Override
    @Transactional
    public ResultVo applyInvoice(CustomerInvoiceAddDto dto) throws Exception {
        // 保存开票信息
        CustomerInvoice invoice = new CustomerInvoice();
        BeanUtils.copyProperties(dto,invoice);
        invoice.setType(Integer.valueOf(dto.getType()));
        invoice.setCustomerId(dto.getUserId());
        Long invoiceId = null;
        if (dto.getId() != null) {
            invoiceId = dto.getId();
            boolean update = super.updateById(invoice);
            if (!update) {
                return BaseResultUtil.fail();
            }
        } else {
            String returnId = super.saveAndReturnId(invoice);
            invoiceId = Long.valueOf(returnId);
            if (StringUtils.isEmpty(returnId)) {
                return BaseResultUtil.fail();
            }
        }

        // 保存开票申请信息
        InvoiceApply invoiceApply = getInvoiceApply(dto);
        invoiceApply.setInvoiceId(invoiceId);
        String returnId = invoiceApplyService.addAndReturnId(invoiceApply);
        if (StringUtils.isEmpty(returnId)) {
            throw new Exception("保存开票信息异常");
        }
        // 保存开票申请信息与订单关系信息
        InvoiceOrderCon invoiceOrderCon = new InvoiceOrderCon();
        invoiceOrderCon.setInvoiceApplyId(Long.valueOf(returnId));
        for (OrderAmountDto orderAmountDto : dto.getOrderAmountList()) {
            invoiceOrderCon.setOrderNo(orderAmountDto.getOrderNo());
            int i = invoiceOrderConDao.insert(invoiceOrderCon);
            if (i != 1) {
                throw new Exception("保存开票订单异常");
            }
        }
        return BaseResultUtil.success();
    }

    private InvoiceApply getInvoiceApply(CustomerInvoiceAddDto dto) {
        InvoiceApply invoiceApply = new InvoiceApply();
        BigDecimal amount = new BigDecimal(0);
        for (OrderAmountDto orderAmountDto : dto.getOrderAmountList()) {
            amount = amount.add(orderAmountDto.getAmount());
        }
        invoiceApply.setAmount(amount);
        invoiceApply.setApplyTime(System.currentTimeMillis());
        invoiceApply.setCustomerId(dto.getUserId());
        invoiceApply.setCustomerName(dto.getName());
        invoiceApply.setOperationName(dto.getName());
        invoiceApply.setState(FieldConstant.INVOICE_APPLY_IN);
        return invoiceApply;
    }

}
