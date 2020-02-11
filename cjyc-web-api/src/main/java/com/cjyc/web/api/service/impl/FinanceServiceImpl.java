package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.ICustomerInvoiceDao;
import com.cjyc.common.model.dao.IFinanceDao;
import com.cjyc.common.model.dao.IInvoiceReceiptDao;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.InvoiceReceipt;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.web.api.service.ICustomerService;
import com.cjyc.web.api.service.IFinanceService;
import com.cjyc.web.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/21 15:06
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class FinanceServiceImpl implements IFinanceService {

    @Resource
    private IFinanceDao financeDao;

    @Resource
    private ICustomerInvoiceDao customerInvoiceDao;

    @Resource
    private IInvoiceReceiptDao invoiceReceiptDao;

    @Resource
    private ICsSendNoService csSendNoService;

    @Resource
    private ICustomerService customerService;

    @Resource
    private IOrderService orderService;

    @Override
    public ResultVo<PageVo<FinanceVo>> getFinanceList(FinanceQueryDto financeQueryDto) {
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<FinanceVo> financeVoList = financeDao.getFinanceList(financeQueryDto);
        for(int i=0;i<financeVoList.size();i++){
            FinanceVo financeVo = financeVoList.get(i);

            if(financeVo != null){
                //TODO 实收金额  收入合计

                String orderCarNo = financeVo.getNo();
                BigDecimal pickUpCarFee = financeDao.getFee(orderCarNo,1);
                BigDecimal trunkLineFee = financeDao.getFee(orderCarNo,2);
                BigDecimal carryCarFee = financeDao.getFee(orderCarNo,3);

                financeVo.setPickUpCarFee(pickUpCarFee);
                financeVo.setTrunkLineFee(trunkLineFee);
                financeVo.setCarryCarFee(carryCarFee);

                List<TrunkLineVo> pickUpCarList = financeDao.getTrunkCostList(orderCarNo,1);
                List<TrunkLineVo> trunkLineVoList = financeDao.getTrunkCostList(orderCarNo,2);
                List<TrunkLineVo> carryCarList = financeDao.getTrunkCostList(orderCarNo,3);

                financeVo.setPickUpCarList(pickUpCarList);
                financeVo.setTrunkLineVoList(trunkLineVoList);
                financeVo.setCarryCarList(carryCarList);

                BigDecimal totalCost = new BigDecimal(0);
                //成本合计
                if(pickUpCarList!=null){
                    for(int j=0;j<pickUpCarList.size();j++){
                        if(pickUpCarList.get(j)!=null&&pickUpCarList.get(j).getFreightFee()!=null){
                            totalCost = totalCost.add(pickUpCarList.get(j).getFreightFee());
                        }
                    }
                }

                if(trunkLineVoList!=null){
                    for(int k=0;k<trunkLineVoList.size();k++){
                        if(trunkLineVoList.get(k)!=null&&trunkLineVoList.get(k).getFreightFee()!=null){
                            totalCost = totalCost.add(trunkLineVoList.get(k).getFreightFee());
                        }
                    }
                }

                if(carryCarList!=null){
                    for(int m=0;m<carryCarList.size();m++){
                        if(carryCarList.get(m)!=null&&carryCarList.get(m).getFreightFee()!=null){
                            totalCost = totalCost.add(carryCarList.get(m).getFreightFee());
                        }
                    }
                }


                financeVo.setTotalCost(totalCost);

                financeVo.setGrossProfit(financeVo.getTotalIncome().subtract(totalCost));
            }
        }
        PageInfo<FinanceVo> pageInfo = new PageInfo<>(financeVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        FinanceQueryDto financeQueryDto = getFinanceQueryDto(request);
        List<FinanceVo> financeVoList = getAllFinanceList(financeQueryDto);
    }

    private List<FinanceVo> getAllFinanceList(FinanceQueryDto financeQueryDto) {
        List<FinanceVo> financeVoList = financeDao.getFinanceList(financeQueryDto);
        for(int i=0;i<financeVoList.size();i++){
            FinanceVo financeVo = financeVoList.get(i);
            if(financeVo != null){
                String orderCarNo = financeVo.getNo();
                List<TrunkLineVo> trunkLineVoList = financeDao.getTrunkCostList(orderCarNo,0);
                financeVo.setTrunkLineVoList(trunkLineVoList);
            }
        }

        return financeVoList;
    }

    private FinanceQueryDto getFinanceQueryDto(HttpServletRequest request) {
        FinanceQueryDto financeQueryDto = new FinanceQueryDto();
        String no = request.getParameter("no");
        return financeQueryDto;
    }

    @Override
    public ResultVo<PageVo<FinanceReceiptVo>> getFinanceReceiptList(FinanceQueryDto financeQueryDto) {
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<FinanceReceiptVo> financeVoList = financeDao.getFinanceReceiptList(financeQueryDto);
        PageInfo<FinanceReceiptVo> pageInfo = new PageInfo<>(financeVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void applySettlement(ApplySettlementDto applySettlementDto) {
        int invoiceId = 0;
        String state="0";
        if(applySettlementDto != null && applySettlementDto.getIsInvoice() != null && applySettlementDto.getIsInvoice().equals("1")){
            CustomerInvoice customerInvoice = new CustomerInvoice();
            customerInvoice.setCustomerId(applySettlementDto.getCustomerId());
            customerInvoice.setType(applySettlementDto.getType());
            customerInvoice.setName(applySettlementDto.getCustomerName());
            customerInvoice.setTaxCode(applySettlementDto.getTaxPayerNumber());
            customerInvoice.setInvoiceAddress(applySettlementDto.getAddress());
            customerInvoice.setTel(applySettlementDto.getPhone());
            customerInvoice.setBankName(applySettlementDto.getBankName());
            customerInvoice.setBankAccount(applySettlementDto.getBankAccount());
            customerInvoice.setPickupPerson(applySettlementDto.getAddressee());
            customerInvoice.setPickupPhone(applySettlementDto.getAddresseePhone());
            customerInvoice.setPickupAddress(applySettlementDto.getMailAddress());
            invoiceId = customerInvoiceDao.insert(customerInvoice);
            state="1";
        }

        InvoiceReceipt invoiceReceipt = new InvoiceReceipt();
        invoiceReceipt.setOrderCarNo(applySettlementDto.getNo());
        invoiceReceipt.setCustomerId(applySettlementDto.getCustomerId());
        invoiceReceipt.setFreightFee(applySettlementDto.getFreightFee());
        invoiceReceipt.setAmount(applySettlementDto.getAmount().multiply(new BigDecimal(100)));
        invoiceReceipt.setInvoiceId(Long.valueOf(invoiceId));
        invoiceReceipt.setState(state);
        invoiceReceipt.setInvoiceTime(System.currentTimeMillis());
        invoiceReceipt.setSerialNumber(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));
        invoiceReceipt.setInvoiceMan("");

        invoiceReceiptDao.insert(invoiceReceipt);
    }

    @Override
    public void confirmSettlement(String serialNumber,String invoiceNo) {
        InvoiceReceipt invoiceReceipt = new InvoiceReceipt();
        invoiceReceipt.setSerialNumber(serialNumber);
        invoiceReceipt.setInvoiceNo(invoiceNo);
        invoiceReceipt.setState("2");
        invoiceReceipt.setConfirmMan("");
        invoiceReceipt.setConfirmTime(System.currentTimeMillis());
        invoiceReceiptDao.confirmSettlement(invoiceReceipt);
    }

    @Override
    public ResultVo<PageVo<WaitInvoiceVo>> getWaitInvoiceList(WaitQueryDto waitInvoiceQueryDto) {

        PageHelper.startPage(waitInvoiceQueryDto.getCurrentPage(), waitInvoiceQueryDto.getPageSize());
        List<WaitInvoiceVo> waitInvoiceVoList = invoiceReceiptDao.getWaitInvoiceList(waitInvoiceQueryDto);
        PageInfo<WaitInvoiceVo> pageInfo = new PageInfo<>(waitInvoiceVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void cancelSettlement(String serialNumber) {
        InvoiceReceipt invoiceReceipt = new InvoiceReceipt();
        invoiceReceipt.setSerialNumber(serialNumber);
        invoiceReceipt.setState("1");
        invoiceReceiptDao.confirmSettlement(invoiceReceipt);
    }

    @Override
    public ResultVo<PageVo<WaitForBackVo>> getWaitForBackList(WaitQueryDto waitInvoiceQueryDto) {
        PageHelper.startPage(waitInvoiceQueryDto.getCurrentPage(), waitInvoiceQueryDto.getPageSize());
        List<WaitForBackVo> waitInvoiceVoList = invoiceReceiptDao.getWaitForBackList(waitInvoiceQueryDto);
        PageInfo<WaitForBackVo> pageInfo = new PageInfo<>(waitInvoiceVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public void writeOff(String serialNumber,String invoiceNo) {
        InvoiceReceipt invoiceReceipt = new InvoiceReceipt();
        invoiceReceipt.setSerialNumber(serialNumber);
        invoiceReceipt.setInvoiceNo(invoiceNo);
        invoiceReceipt.setState("3");
        invoiceReceipt.setWriteOffMan("");
        invoiceReceipt.setWriteOffTime(System.currentTimeMillis());
        invoiceReceiptDao.writeOff(invoiceReceipt);
    }

    @Override
    public SettlementDetailVo detail(Long Id) {
        SettlementDetailVo settlementDetailVo = new SettlementDetailVo();
        InvoiceReceipt invoiceReceipt = invoiceReceiptDao.selectDetailById(Id);
        if(invoiceReceipt != null){
            CustomerInvoice customerInvoice = customerInvoiceDao.selectById(invoiceReceipt.getInvoiceId());

            settlementDetailVo.setCustomerInvoice(customerInvoice);
            settlementDetailVo.setAmount(invoiceReceipt.getAmount());
            settlementDetailVo.setFreightFee(invoiceReceipt.getFreightFee());
            settlementDetailVo.setInvoiceNo(invoiceReceipt.getInvoiceNo());
            settlementDetailVo.setWriteOffTime(invoiceReceipt.getWriteOffTime());
        }

        return settlementDetailVo;
    }

    @Override
    public ResultVo<PageVo<ReceivableVo>> getReceivableList(WaitQueryDto waitInvoiceQueryDto) {
        PageHelper.startPage(waitInvoiceQueryDto.getCurrentPage(), waitInvoiceQueryDto.getPageSize());
        List<ReceivableVo> waitInvoiceVoList = invoiceReceiptDao.getReceivableList(waitInvoiceQueryDto);
        PageInfo<ReceivableVo> pageInfo = new PageInfo<>(waitInvoiceVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<PaymentVo>> getPaymentList(FinanceQueryDto financeQueryDto) {
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<PaymentVo> financeVoList = financeDao.getPaymentList(financeQueryDto);
        for(int i=0;i<financeVoList.size();i++){
            PaymentVo paymentVo = financeVoList.get(i);
            if(paymentVo!=null&&paymentVo.getType()!=null){
                if(paymentVo.getType()==2){//企业
                    Integer settleType = financeDao.getCustomerContractById(paymentVo.getCustomerContractId());
                    paymentVo.setPayModeName(settleType!=null&&settleType==0?"时付":"账期");
                }else if(paymentVo.getType()==3){//合伙人
                    Integer settleType = financeDao.getCustomerPartnerById(paymentVo.getCustomerId());
                    paymentVo.setPayModeName(settleType!=null&&settleType==0?"时付":"账期");
                }
            }
        }
        PageInfo<PaymentVo> pageInfo = new PageInfo<>(financeVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<PaidVo>> getPaidList(PayMentQueryDto payMentQueryDto) {
        PageHelper.startPage(payMentQueryDto.getCurrentPage(), payMentQueryDto.getPageSize());
        List<PaidVo> financeVoList = financeDao.getPaidList(payMentQueryDto);
        PageInfo<PaidVo> pageInfo = new PageInfo<>(financeVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<CollectReceiveVo>> getCollectReceiveList(CollectReceiveQueryDto collectReceiveQueryDto) {
        PageHelper.startPage(collectReceiveQueryDto.getCurrentPage(), collectReceiveQueryDto.getPageSize());
        List<CollectReceiveVo> collectReceiveList = financeDao.getCollectReceiveList(collectReceiveQueryDto);
        PageInfo<CollectReceiveVo> pageInfo = new PageInfo<>(collectReceiveList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<CashSettlementDetailVo> settleDetail(String wayBillNo) {
        CashSettlementDetailVo cashSettlementDetailVo = new CashSettlementDetailVo();
        List<CashCarDetailVo> cashCarDetailVos = new ArrayList<>();
        cashSettlementDetailVo.setCashCarDetailVoList(cashCarDetailVos);
        return BaseResultUtil.success(cashSettlementDetailVo);
    }

    @Override
    public ResultVo updateBackState(String wayBillNo) {
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<FinancePayableVo>> getFinancePayableList(PayableQueryDto payableQueryDto) {
        PageHelper.startPage(payableQueryDto.getCurrentPage(), payableQueryDto.getPageSize());
        List<FinancePayableVo> financeVoList = financeDao.getFinancePayableList(payableQueryDto);
        for (int i=0;i<financeVoList.size();i++) {
            FinancePayableVo financePayableVo = financeVoList.get(i);
            BigDecimal freightFee = new BigDecimal(0);
            List<String> list = new ArrayList<>();
            list.add(financePayableVo.getNo());
            List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);
            for (int j=0;j<settlementVoList.size();j++){
                PayableTaskVo settlementVo = settlementVoList.get(j);
                if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                    freightFee = freightFee.add(settlementVo.getFreightFee());
                }
            }
            financePayableVo.setRemainDate(financePayableVo.getSettlePeriod()-formatDuring(System.currentTimeMillis()-financePayableVo.getCompleteTime()));
            financePayableVo.setFreightPayable(freightFee.divide(new BigDecimal(100)));
        }
        PageInfo<FinancePayableVo> pageInfo = new PageInfo<>(financeVoList);
        return BaseResultUtil.success(pageInfo);
    }

    private Long formatDuring(long time){
        long days = time / (1000 * 60 * 60 * 24);
        return days;
    }

    @Override
    public ResultVo getSettlementPayable(List<String> taskNo) {
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(taskNo);
        return BaseResultUtil.success(settlementVoList);
    }

    @Override
    public ResultVo apply(AppSettlementPayableDto appSettlementPayableDto) {
        List<String> list = appSettlementPayableDto.getTaskNo();
        String serialNumber = csSendNoService.getNo(SendNoTypeEnum.PAYMENT);
        SettlementVo settlementVo = new SettlementVo();
        settlementVo.setSerialNumber(serialNumber);
        settlementVo.setApplyTime(System.currentTimeMillis());
        settlementVo.setApplicantId(appSettlementPayableDto.getLoginId());
        settlementVo.setFreightFee(appSettlementPayableDto.getFreightFee());
        settlementVo.setState("0");
        financeDao.apply(settlementVo);

        for (int i=0;i<list.size();i++){
            String taskNo = list.get(i);
            SettlementVo settlement = new SettlementVo();
            settlement.setSerialNumber(serialNumber);
            settlement.setNo(taskNo);
            financeDao.applyDetail(settlement);
        }

        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<SettlementVo>> collect(WaitTicketCollectDto waitTicketCollectDto) {
        PageHelper.startPage(waitTicketCollectDto.getCurrentPage(), waitTicketCollectDto.getPageSize());
        List<SettlementVo> settlementVoList = financeDao.getCollectTicketList(waitTicketCollectDto);
        PageInfo<SettlementVo> pageInfo = new PageInfo<>(settlementVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo getConfirmTicket(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (int j=0;j<settlementVoList.size();j++){
            PayableTaskVo settlementVo = settlementVoList.get(j);
            if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                freightFee.add(settlementVo.getFreightFee());
            }
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee);

        return BaseResultUtil.success(payableSettlementVo);
    }

    @Override
    public ResultVo confirm(ConfirmTicketDto confirmTicketDto) {

        SettlementVo settlementVo = new SettlementVo();
        settlementVo.setState("1");
        settlementVo.setSerialNumber(confirmTicketDto.getSerialNumber());
        settlementVo.setConfirmId(confirmTicketDto.getConfirmId());
        settlementVo.setConfirmTime(System.currentTimeMillis());
        settlementVo.setInvoiceNo(confirmTicketDto.getInvoiceNo());
        financeDao.confirm(settlementVo);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo withdraw(String serialNumber) {
        financeDao.withdraw(serialNumber);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<SettlementVo>> payment(WaitPaymentDto waitPaymentDto) {
        PageHelper.startPage(waitPaymentDto.getCurrentPage(), waitPaymentDto.getPageSize());
        List<SettlementVo> settlementVoList = financeDao.getPayablePaymentList(waitPaymentDto);
        PageInfo<SettlementVo> pageInfo = new PageInfo<>(settlementVoList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo getWriteOffTicket(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (int j=0;j<settlementVoList.size();j++){
            PayableTaskVo settlementVo = settlementVoList.get(j);
            if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                freightFee.add(settlementVo.getFreightFee());
            }
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        SettlementVo settlementVo = financeDao.getPayableSettlement(serialNumber);
        if(settlementVo!=null&&settlementVo.getInvoiceNo()!=null){
            payableSettlementVo.setInvoiceNo(settlementVo.getInvoiceNo());
        }
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee);

        return BaseResultUtil.success(payableSettlementVo);
    }

    @Override
    public ResultVo writeOffPayable(WriteOffTicketDto writeOffTicketDto) {
        SettlementVo settlementVo = new SettlementVo();
        settlementVo.setState("2");
        settlementVo.setSerialNumber(writeOffTicketDto.getSerialNumber());
        settlementVo.setWriteOffId(writeOffTicketDto.getWriteOffId());
        settlementVo.setWriteOffTime(System.currentTimeMillis());
        settlementVo.setTotalFreightPay(writeOffTicketDto.getTotalFreightPay().multiply(new BigDecimal(100)));
        financeDao.writeOffPayable(settlementVo);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo paid(PayablePaidQueryDto payablePaidQueryDto) {
        PageHelper.startPage(payablePaidQueryDto.getCurrentPage(), payablePaidQueryDto.getPageSize());
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(payablePaidQueryDto);
        PageInfo<PayablePaidVo> pageInfo = new PageInfo<>(payablePaidList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo payableDetail(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (int j=0;j<settlementVoList.size();j++){
            PayableTaskVo settlementVo = settlementVoList.get(j);
            if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                freightFee.add(settlementVo.getFreightFee());
            }
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        SettlementVo settlementVo = financeDao.getPayableSettlement(serialNumber);
        if(settlementVo!=null&&settlementVo.getInvoiceNo()!=null){
            payableSettlementVo.setInvoiceNo(settlementVo.getInvoiceNo());
        }
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee);
        payableSettlementVo.setTotalFreightPaid(settlementVo.getTotalFreightPay());
        payableSettlementVo.setWriteOffTime(settlementVo.getWriteOffTime());

        return BaseResultUtil.success(payableSettlementVo);
    }
}
