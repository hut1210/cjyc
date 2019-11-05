package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICustomerInvoiceDao;
import com.cjyc.common.model.dao.IInvoiceApplyDao;
import com.cjyc.common.model.dao.IInvoiceOrderConDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.customer.invoice.OrderAmountDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceQueryDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.entity.InvoiceOrderCon;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo;
import com.cjyc.web.api.service.IInvoiceApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 发票申请信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-02
 */
@Service
public class InvoiceApplyServiceImpl extends ServiceImpl<IInvoiceApplyDao, InvoiceApply> implements IInvoiceApplyService {
    @Autowired
    private ICustomerInvoiceDao customerInvoiceDao;
    @Autowired
    private IInvoiceOrderConDao invoiceOrderConDao;
    @Autowired
    private IOrderDao orderDao;

    @Override
    public ResultVo getInvoiceApplyPage(InvoiceQueryDto dto) {

        return null;
    }

    @Override
    public ResultVo getDetail(Long userId, Long invoiceApplyId) {
        InvoiceDetailVo detailVo = new InvoiceDetailVo();
        // 根据客户ID，主键ID查询发票申请信息
        LambdaQueryWrapper<InvoiceApply> queryWrapper = new QueryWrapper<InvoiceApply>().lambda()
                .eq(InvoiceApply::getCustomerId, userId).eq(InvoiceApply::getId, invoiceApplyId)
                .select(InvoiceApply::getAmount,InvoiceApply::getInvoiceId);
        InvoiceApply invoice = super.getOne(queryWrapper);
        detailVo.setAmount(Objects.isNull(invoice) ? new BigDecimal(0) : invoice.getAmount());

        // 根据发票信息ID查询客户发票信息
        CustomerInvoice customerInvoice = customerInvoiceDao.selectById(invoice.getInvoiceId());
        detailVo.setCustomerInvoice(customerInvoice);

        // 根据发票申请ID查询该发票下的订单号
        List<InvoiceOrderCon> invoiceOrderConList = invoiceOrderConDao.selectList(new QueryWrapper<InvoiceOrderCon>().lambda()
                .eq(InvoiceOrderCon::getInvoiceApplyId, invoiceApplyId));

        // 根据订单号查询订单金额
        List<OrderAmountDto> orderAmountList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(invoiceOrderConList)) {
            for (InvoiceOrderCon invoiceOrderCon : invoiceOrderConList) {
                String orderNo = invoiceOrderCon.getOrderNo();
                Order order = orderDao.selectOne(new QueryWrapper<Order>().lambda().eq(Order::getNo, orderNo).select(Order::getTotalFee));
                OrderAmountDto dto = new OrderAmountDto();
                dto.setOrderNo(orderNo);
                dto.setAmount(Objects.isNull(order) ? new BigDecimal(0) : order.getTotalFee());
                orderAmountList.add(dto);
            }
        }
        detailVo.setOrderAmountList(orderAmountList);
        return BaseResultUtil.success(detailVo);
    }
}
