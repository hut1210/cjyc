package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.finance.WaitQueryDto;
import com.cjyc.common.model.entity.InvoiceReceipt;
import com.cjyc.common.model.vo.web.finance.ReceivableVo;
import com.cjyc.common.model.vo.web.finance.WaitForBackVo;
import com.cjyc.common.model.vo.web.finance.WaitInvoiceVo;

import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/25 15:55
 */
public interface IInvoiceReceiptDao extends BaseMapper<InvoiceReceipt> {
    List<WaitInvoiceVo> getWaitInvoiceList(WaitQueryDto waitInvoiceQueryDto);

    List<WaitForBackVo> getWaitForBackList(WaitQueryDto waitInvoiceQueryDto);

    void confirmSettlement(InvoiceReceipt invoiceReceipt);

    void writeOff(InvoiceReceipt invoiceReceipt);

    List<ReceivableVo> getReceivableList(WaitQueryDto waitInvoiceQueryDto);
}
