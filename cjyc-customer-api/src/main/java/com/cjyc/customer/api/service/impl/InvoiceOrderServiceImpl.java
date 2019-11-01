package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.InvoiceOrder;
import com.cjyc.common.model.dao.IInvoiceOrderDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IInvoiceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
public class InvoiceOrderServiceImpl extends ServiceImpl<IInvoiceOrderDao, InvoiceOrder> implements IInvoiceOrderService {

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IInvoiceOrderDao invoiceOrderDao;

    @Override
    public ResultVo getUnbilledOrder(Long userId) {
        try{

        }catch (Exception e){

        }
        return null;
    }
}
