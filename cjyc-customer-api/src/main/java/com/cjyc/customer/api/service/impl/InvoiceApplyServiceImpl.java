package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.IInvoiceApplyDao;
import com.cjyc.common.model.dao.IInvoiceOrderConDao;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.invoice.OrderAmountDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.entity.InvoiceOrderCon;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceApplyVo;
import com.cjyc.customer.api.service.ICustomerInvoiceService;
import com.cjyc.customer.api.service.IInvoiceApplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-31
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class InvoiceApplyServiceImpl extends SuperServiceImpl<IInvoiceApplyDao, InvoiceApply> implements IInvoiceApplyService {
    @Autowired
    private ICustomerInvoiceService customerInvoiceService;
    @Autowired
    private IInvoiceOrderConDao invoiceOrderConDao;

    @Override
    public ResultVo getInvoiceApplyPage(InvoiceApplyQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceApply> list = super.list(new QueryWrapper<InvoiceApply>().lambda().eq(InvoiceApply::getCustomerId, dto.getLoginId()));
        List<InvoiceApplyVo> returnList = new ArrayList<>(10);
        for (InvoiceApply invoiceApply : list) {
            InvoiceApplyVo vo = new InvoiceApplyVo();
            BeanUtils.copyProperties(invoiceApply,vo);
            returnList.add(vo);
        }

        PageInfo<InvoiceApplyVo> pageInfo = new PageInfo<>(returnList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo applyInvoice(CustomerInvoiceAddDto dto) throws Exception {
        // 开票订单号校验
        for (OrderAmountDto orderAmountDto : dto.getOrderAmountList()) {
            InvoiceOrderCon invoiceOrderCon = invoiceOrderConDao.selectOne(new QueryWrapper<InvoiceOrderCon>().lambda()
                    .eq(InvoiceOrderCon::getOrderNo, orderAmountDto.getOrderNo()));
            if (!Objects.isNull(invoiceOrderCon)) {
                return BaseResultUtil.fail("订单号"+invoiceOrderCon.getOrderNo()+"已开过发票");
            }
        }
        // 保存开票信息
        CustomerInvoice invoice = new CustomerInvoice();
        BeanUtils.copyProperties(dto,invoice);
        invoice.setType(Integer.valueOf(dto.getType()));
        invoice.setCustomerId(dto.getLoginId());
        Long invoiceId = null;
        if (dto.getId() != null) {
            invoiceId = dto.getId();
            boolean update = customerInvoiceService.updateById(invoice);
            if (!update) {
                return BaseResultUtil.fail("开票失败");
            }
        } else {
            String returnId = customerInvoiceService.addAndReturnId(invoice);
            invoiceId = Long.valueOf(returnId);
            if (StringUtils.isEmpty(returnId)) {
                return BaseResultUtil.fail("开票失败");
            }
        }
        // 保存开票申请信息
        InvoiceApply invoiceApply = getInvoiceApply(dto);
        invoiceApply.setInvoiceId(invoiceId);
        String returnId = super.saveAndReturnId(invoiceApply);
        if (StringUtils.isEmpty(returnId)) {
            throw new Exception("保存开票信息异常");
        }
        // 保存开票申请信息与订单关系信息
        InvoiceOrderCon invoiceOrderCon = new InvoiceOrderCon();
        invoiceOrderCon.setInvoiceApplyId(Long.valueOf(returnId));
        for (OrderAmountDto orderAmountDto : dto.getOrderAmountList()) {
            invoiceOrderCon.setOrderNo(orderAmountDto.getOrderNo());
            invoiceOrderConDao.insert(invoiceOrderCon);
        }
        return BaseResultUtil.success();
    }

    private InvoiceApply getInvoiceApply(CustomerInvoiceAddDto dto) {
        InvoiceApply invoiceApply = new InvoiceApply();
        BigDecimal amount = new BigDecimal(0);
        for (OrderAmountDto orderAmountDto : dto.getOrderAmountList()) {
            amount = amount.add(orderAmountDto.getAmount());
        }
        invoiceApply.setAmount(amount.multiply(new BigDecimal(100)));
        invoiceApply.setApplyTime(System.currentTimeMillis());
        invoiceApply.setCustomerId(dto.getLoginId());
        invoiceApply.setCustomerName(dto.getName());
        invoiceApply.setState(FieldConstant.INVOICE_APPLY_IN);
        return invoiceApply;
    }
}
