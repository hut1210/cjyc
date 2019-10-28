package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.FinanceReceipt;
import com.cjyc.common.model.dao.IFinanceReceiptDao;
import com.cjyc.salesman.api.service.IFinanceReceiptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 财务收款单表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class FinanceReceiptServiceImpl extends ServiceImpl<IFinanceReceiptDao, FinanceReceipt> implements IFinanceReceiptService {

}
