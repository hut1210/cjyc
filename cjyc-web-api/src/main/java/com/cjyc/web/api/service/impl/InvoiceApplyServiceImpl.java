package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.ICustomerInvoiceDao;
import com.cjyc.common.model.dao.IInvoiceApplyDao;
import com.cjyc.common.model.dao.IInvoiceOrderConDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.customer.invoice.OrderAmountDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceDetailAndConfirmDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceQueryDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.entity.InvoiceOrderCon;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo;
import com.cjyc.web.api.service.IInvoiceApplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        LambdaQueryWrapper<InvoiceApply> queryWrapper = getInvoiceApplyLambdaQueryWrapper(dto);
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceApply> list = super.list(queryWrapper);
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }

    private LambdaQueryWrapper<InvoiceApply> getInvoiceApplyLambdaQueryWrapper(InvoiceQueryDto dto) {
        LambdaQueryWrapper<InvoiceApply> queryWrapper = new QueryWrapper<InvoiceApply>().lambda()
                .like(!StringUtils.isEmpty(dto.getCustomerName()),InvoiceApply::getCustomerName,dto.getCustomerName())
                .like(!StringUtils.isEmpty(dto.getInvoiceNo()),InvoiceApply::getInvoiceNo,dto.getInvoiceNo())
                .like(!StringUtils.isEmpty(dto.getOperationName()),InvoiceApply::getOperationName,dto.getOperationName())
                .eq(!StringUtils.isEmpty(dto.getState()),InvoiceApply::getState,dto.getState());
        if (!Objects.isNull(dto.getApplyTimeStart())) {
            Long applyTimeStart = LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.convertDateToLDT(dto.getApplyTimeStart()));
            queryWrapper = queryWrapper.ge(InvoiceApply::getApplyTime,applyTimeStart);
        }
        if (!Objects.isNull(dto.getApplyTimeEnd())) {
            Long applyTimeEnd = LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.convertDateToLDT(dto.getApplyTimeEnd()));
            queryWrapper = queryWrapper.le(InvoiceApply::getApplyTime,applyTimeEnd);
        }
        if (!Objects.isNull(dto.getInvoiceTimeStart())) {
            Long invoiceTimeStart = LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.convertDateToLDT(dto.getInvoiceTimeStart()));
            queryWrapper = queryWrapper.ge(InvoiceApply::getInvoiceTime,invoiceTimeStart);
        }
        if (!Objects.isNull(dto.getInvoiceTimeEnd())) {
            Long invoiceTimeEnd = LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.convertDateToLDT(dto.getInvoiceTimeEnd()));
            queryWrapper = queryWrapper.le(InvoiceApply::getInvoiceTime,invoiceTimeEnd);
        }
        return queryWrapper;
    }

    @Override
    public ResultVo getDetail(InvoiceDetailAndConfirmDto dto) {
        InvoiceDetailVo detailVo = new InvoiceDetailVo();
        // 根据客户ID，主键ID查询发票申请信息
        LambdaQueryWrapper<InvoiceApply> queryWrapper = new QueryWrapper<InvoiceApply>().lambda()
                .eq(InvoiceApply::getCustomerId, dto.getUserId()).eq(InvoiceApply::getId, dto.getInvoiceApplyId())
                .select(InvoiceApply::getAmount,InvoiceApply::getInvoiceId);
        InvoiceApply invoice = super.getOne(queryWrapper);
        detailVo.setAmount(Objects.isNull(invoice) ? new BigDecimal(0) : invoice.getAmount());

        // 根据发票信息ID查询客户发票信息
        if (!Objects.isNull(invoice)) {
            CustomerInvoice customerInvoice = customerInvoiceDao.selectById(invoice.getInvoiceId());
            detailVo.setCustomerInvoice(customerInvoice);
        }

        // 根据发票申请ID查询该发票下的订单号
        List<InvoiceOrderCon> invoiceOrderConList = invoiceOrderConDao.selectList(new QueryWrapper<InvoiceOrderCon>().lambda()
                .eq(InvoiceOrderCon::getInvoiceApplyId, dto.getInvoiceApplyId()));

        // 根据订单号查询订单金额
        List<OrderAmountDto> orderAmountList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(invoiceOrderConList)) {
            for (InvoiceOrderCon invoiceOrderCon : invoiceOrderConList) {
                String orderNo = invoiceOrderCon.getOrderNo();
                Order order = orderDao.selectOne(new QueryWrapper<Order>().lambda().eq(Order::getNo, orderNo).select(Order::getTotalFee));
                OrderAmountDto amountDto = new OrderAmountDto();
                amountDto.setOrderNo(orderNo);
                amountDto.setAmount(Objects.isNull(order) ? new BigDecimal(0) : order.getTotalFee());
                orderAmountList.add(amountDto);
            }
        }
        detailVo.setOrderAmountList(orderAmountList);
        return BaseResultUtil.success(detailVo);
    }

    @Override
    public ResultVo confirmInvoice(InvoiceDetailAndConfirmDto dto) {
        // 验证是否已经开过票
        InvoiceApply invoiceApply = super.getById(dto.getInvoiceApplyId());
        if (!Objects.isNull(invoiceApply) && FieldConstant.INVOICE_FINISH == invoiceApply.getState()) {
            return BaseResultUtil.fail("该发票单已开过票,不能重复开票哦");
        }
        LambdaUpdateWrapper<InvoiceApply> updateWrapper = new UpdateWrapper<InvoiceApply>().lambda()
                .set(InvoiceApply::getInvoiceNo, dto.getInvoiceNo())
                .set(InvoiceApply::getState, FieldConstant.INVOICE_FINISH)
                .set(InvoiceApply::getInvoiceTime, System.currentTimeMillis())
                .set(InvoiceApply::getOperationName, dto.getOperationName())
                .eq(InvoiceApply::getId, dto.getInvoiceApplyId())
                .eq(InvoiceApply::getCustomerId, dto.getUserId());
        boolean result = super.update(updateWrapper);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }
}
