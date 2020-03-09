package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.web.api.service.ICustomerService;
import com.cjyc.web.api.service.IFinanceService;
import com.cjyc.web.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private ICsPingPayService csPingPayService;

    @Resource
    private IExternalPaymentDao externalPaymentDao;

    @Resource
    private ICsAdminService csAdminService;

    @Resource
    private IWaybillDao waybillDao;

    @Resource
    private IConfigDao configDao;

    @Resource
    private IPaymentErrorLogDao paymentErrorLogDao;

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
    public List<ExportFinanceVo> exportExcel(FinanceQueryDto financeQueryDto) {
        log.info("financeQueryDto ="+financeQueryDto.toString());
        List<ExportFinanceVo> financeVoList = financeDao.getAllFinanceList(financeQueryDto);
        if(financeVoList==null){
            return financeVoList;
        }
        for(int i=0;i<financeVoList.size();i++){
            ExportFinanceVo financeVo = financeVoList.get(i);

            if(financeVo != null){
                //TODO 实收金额  收入合计

                String orderCarNo = financeVo.getNo();
                BigDecimal pickUpCarFee = financeDao.getFee(orderCarNo,1);
                BigDecimal trunkLineFee = financeDao.getFee(orderCarNo,2);
                BigDecimal carryCarFee = financeDao.getFee(orderCarNo,3);

                financeVo.setPickUpCarFee(pickUpCarFee!=null?pickUpCarFee.divide(new BigDecimal(100)): null);
                financeVo.setTrunkLineFee(trunkLineFee!=null?trunkLineFee.divide(new BigDecimal(100)): null);
                financeVo.setCarryCarFee(carryCarFee!=null?carryCarFee.divide(new BigDecimal(100)): null);

                List<TrunkLineVo> pickUpCarList = financeDao.getTrunkCostList(orderCarNo,1);
                List<TrunkLineVo> trunkLineVoList = financeDao.getTrunkCostList(orderCarNo,2);
                List<TrunkLineVo> carryCarList = financeDao.getTrunkCostList(orderCarNo,3);

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

                financeVo.setFreightReceivable(financeVo.getFreightReceivable()!=null?financeVo.getFreightReceivable().divide(new BigDecimal(100)):financeVo.getFreightReceivable());

                financeVo.setFeeShare(financeVo.getFeeShare()!=null?financeVo.getFeeShare().divide(new BigDecimal(100)):financeVo.getFeeShare());
                financeVo.setAmountReceived(financeVo.getAmountReceived()!=null?financeVo.getAmountReceived().divide(new BigDecimal(100)):financeVo.getAmountReceived());
                financeVo.setTotalCost(totalCost!=null?totalCost.divide(new BigDecimal(100)):null);

                financeVo.setGrossProfit((financeVo.getTotalIncome().subtract(totalCost)).divide(new BigDecimal(100)));
                financeVo.setTotalIncome(financeVo.getTotalIncome()!=null?financeVo.getTotalIncome().divide(new BigDecimal(100)):financeVo.getTotalIncome());
            }
        }

        return financeVoList;
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
        List<PaymentVo> paymentVoList = new ArrayList<>();
        for(int i=0;i<financeVoList.size();i++){
            PaymentVo paymentVo = financeVoList.get(i);
            if(paymentVo!=null&&paymentVo.getType()!=null){
                if(paymentVo.getType()==2){//企业
                    Integer settleType = financeDao.getCustomerContractById(paymentVo.getCustomerContractId());
                    if(settleType!=null&&settleType==0){
                        paymentVo.setPayModeName("时付");
                        paymentVoList.add(paymentVo);
                    }
                }else if(paymentVo.getType()==3){//合伙人
                    Integer settleType = financeDao.getCustomerPartnerById(paymentVo.getCustomerId());
                    if(settleType!=null&&settleType==0){
                        paymentVo.setPayModeName("时付");
                        paymentVoList.add(paymentVo);
                    }
                }
                if(paymentVo.getType()==1){
                    paymentVoList.add(paymentVo);
                }
            }
        }
        PageInfo<PaymentVo> pageInfo = new PageInfo<>(paymentVoList);
        FinanceQueryDto fqd = new FinanceQueryDto();
        List<PaymentVo> pv = financeDao.getPaymentList(fqd);
        List<PaymentVo> paymentVos = new ArrayList<>();
        for(int j=0;j<pv.size();j++){
            PaymentVo paymentVo = pv.get(j);
            if(paymentVo!=null&&paymentVo.getType()!=null){
                if(paymentVo.getType()==2){//企业
                    Integer settleType = financeDao.getCustomerContractById(paymentVo.getCustomerContractId());
                    if(settleType!=null&&settleType==0){
                        paymentVo.setPayModeName("时付");
                        paymentVos.add(paymentVo);
                    }
                }else if(paymentVo.getType()==3){//合伙人
                    Integer settleType = financeDao.getCustomerPartnerById(paymentVo.getCustomerId());
                    if(settleType!=null&&settleType==0){
                        paymentVo.setPayModeName("时付");
                        paymentVos.add(paymentVo);
                    }
                }
                if(paymentVo.getType()==1){
                    paymentVoList.add(paymentVo);
                }
            }
        }
        Map<String, Object> countInfo = new HashMap<>();
        countInfo.put("receiptCount",paymentVos.size());
        return BaseResultUtil.success(pageInfo,countInfo);
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
                settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
            }
            financePayableVo.setRemainDate(financePayableVo.getSettlePeriod()-formatDuring(System.currentTimeMillis()-financePayableVo.getCompleteTime()));
            financePayableVo.setFreightPayable(freightFee.divide(new BigDecimal(100)));
        }
        PageInfo<FinancePayableVo> pageInfo = new PageInfo<>(financeVoList);

        return BaseResultUtil.success(pageInfo,getCountInfo());
    }

    private Map getCountInfo(){
        PayableQueryDto pqd = new PayableQueryDto();
        List<FinancePayableVo> fpv = financeDao.getFinancePayableList(pqd);
        WaitTicketCollectDto wc = new WaitTicketCollectDto();
        List<SettlementVo> sv = financeDao.getCollectTicketList(wc);
        WaitPaymentDto wp = new WaitPaymentDto();
        List<SettlementVo> payablePaymentList = financeDao.getPayablePaymentList(wp);
        PayablePaidQueryDto pp = new PayablePaidQueryDto();
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(pp);
        PayMentQueryDto pq = new PayMentQueryDto();

        List<PaidNewVo> paidList = new ArrayList<>();
        /*Config config = configDao.getByItemKey("external_pay");
        if(config!=null&&config.getState()==1) {//对外支付模式
            log.info("config.getState() "+config.getState().toString());
            paidList =  financeDao.getPaidListNew(pq);
        }else{//自动付款*/
            paidList =  financeDao.getAutoPaidList(pq);
        /*}*/

        Map<String, Object> countInfo = new HashMap<>();
        countInfo.put("payableCount",fpv.size());
        countInfo.put("waitTicketCount",sv.size());
        countInfo.put("waitPaymentCount",payablePaymentList.size());
        countInfo.put("paidCount",payablePaidList.size());
        countInfo.put("timePaidCount",paidList.size());

        return countInfo;
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
        return BaseResultUtil.success(pageInfo,getCountInfo());
    }

    @Override
    public ResultVo getConfirmTicket(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = new ArrayList<>();
        if(list!=null&&list.size()>0){
            settlementVoList = financeDao.getSettlementInfo(list);
        }

        BigDecimal freightFee = new BigDecimal(0);
        for (int j=0;j<settlementVoList.size();j++){
            PayableTaskVo settlementVo = settlementVoList.get(j);
            if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                freightFee = freightFee.add(settlementVo.getFreightFee());
            }
            settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee.divide(new BigDecimal(100)));

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
        return BaseResultUtil.success(pageInfo,getCountInfo());
    }

    @Override
    public ResultVo getWriteOffTicket(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (int j=0;j<settlementVoList.size();j++){
            PayableTaskVo settlementVo = settlementVoList.get(j);
            if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                freightFee = freightFee.add(settlementVo.getFreightFee());
            }
            settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        SettlementVo settlementVo = financeDao.getPayableSettlement(serialNumber);
        if(settlementVo!=null&&settlementVo.getInvoiceNo()!=null){
            payableSettlementVo.setInvoiceNo(settlementVo.getInvoiceNo());
        }
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee.divide(new BigDecimal(100)));

        return BaseResultUtil.success(payableSettlementVo);
    }

    @Override
    public ResultVo writeOffPayable(WriteOffTicketDto writeOffTicketDto) {
        SettlementVo settlementVo = new SettlementVo();
        settlementVo.setState("2");
        settlementVo.setSerialNumber(writeOffTicketDto.getSerialNumber());
        settlementVo.setWriteOffId(writeOffTicketDto.getWriteOffId());
        settlementVo.setWriteOffTime(System.currentTimeMillis());
        settlementVo.setTotalFreightPay(writeOffTicketDto.getTotalFreightPay());
        financeDao.writeOffPayable(settlementVo);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo paid(PayablePaidQueryDto payablePaidQueryDto) {
        PageHelper.startPage(payablePaidQueryDto.getCurrentPage(), payablePaidQueryDto.getPageSize());
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(payablePaidQueryDto);
        PageInfo<PayablePaidVo> pageInfo = new PageInfo<>(payablePaidList);
        return BaseResultUtil.success(pageInfo,getCountInfo());
    }

    @Override
    public ResultVo payableDetail(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (int j=0;j<settlementVoList.size();j++){
            PayableTaskVo settlementVo = settlementVoList.get(j);
            if(settlementVo!=null && settlementVo.getFreightFee()!=null){
                freightFee = freightFee.add(settlementVo.getFreightFee());
            }
            settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        SettlementVo settlementVo = financeDao.getPayableSettlement(serialNumber);
        if(settlementVo!=null&&settlementVo.getInvoiceNo()!=null){
            payableSettlementVo.setInvoiceNo(settlementVo.getInvoiceNo());
        }
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee.divide(new BigDecimal(100)));
        payableSettlementVo.setTotalFreightPaid(settlementVo.getTotalFreightPay());
        payableSettlementVo.setWriteOffTime(settlementVo.getWriteOffTime());

        return BaseResultUtil.success(payableSettlementVo);
    }

    @Override
    public ResultVo<PageVo<PaidNewVo>> getPaidListNew(PayMentQueryDto payMentQueryDto) {
        log.info("payMentQueryDto = "+payMentQueryDto.toString());
        if(payMentQueryDto.getState()!=null){
            if(payMentQueryDto.getState()!=0){
                payMentQueryDto.setState(1);
            }
        }

        List<PaidNewVo> financeVoList = new ArrayList<>();
        /*Config config = configDao.getByItemKey("external_pay");
        if(config!=null&&config.getState()==1) {//对外支付模式
            log.info("config.getState() "+config.getState().toString());
            financeVoList = getExternalPaidList(payMentQueryDto);
        }else{//自动付款*/
            financeVoList = getAutoPaidList(payMentQueryDto);
        /*}*/
        log.info("financeVoList = "+financeVoList.size());
        PageInfo<PaidNewVo> pageInfo = new PageInfo<>(financeVoList);
        return BaseResultUtil.success(pageInfo,getCountInfo());
    }

    private List<PaidNewVo> getAutoPaidList(PayMentQueryDto payMentQueryDto){
        PageHelper.startPage(payMentQueryDto.getCurrentPage(), payMentQueryDto.getPageSize());
        return financeDao.getAutoPaidList(payMentQueryDto);
    }

    private List<PaidNewVo> getExternalPaidList(PayMentQueryDto payMentQueryDto){
        PageHelper.startPage(payMentQueryDto.getCurrentPage(), payMentQueryDto.getPageSize());
        return  financeDao.getPaidListNew(payMentQueryDto);
    }

    @Override
    public ResultVo externalPayment(ExternalPaymentDto externalPaymentDto) {
        log.info("对外支付操作人Id={}",externalPaymentDto.getLoginId());
        List<Long> waybillIds = externalPaymentDto.getWaybillIds();
        StringBuilder result =  new StringBuilder();
        for (int i=0;i<waybillIds.size();i++){
            Long waybillId = waybillIds.get(i);
            Waybill waybill = waybillDao.selectById(waybillId);
            if(waybill!=null){
                if(waybill.getFreightPayState()==1){
                    if(result.length()>0){
                        result.append(",");
                    }
                    result.append(waybill.getNo());
                    result.append("已支付");
                }else{
                    try {
                        ResultVo resultVo = csPingPayService.allinpayToCarrierNew(waybillIds.get(i));
                        log.info("resultVo错误码 ={}",resultVo.getCode());
                        if(resultVo.getCode()==1){
                            if(result.length()>0){
                                result.append(",");
                            }
                            result.append(waybill.getNo());
                            result.append(resultVo.getMsg());
                        }

                        try {
                            log.info("result = {}",result.toString());
                            if(result.length()==0){
                                if(externalPaymentDto!=null && externalPaymentDto.getLoginId()!=null){
                                    Admin admin = csAdminService.getById(externalPaymentDto.getLoginId(), true);

                                    if(admin != null){
                                        ExternalPayment externalPayment = new ExternalPayment();
                                        externalPayment.setWaybillId(waybillId);
                                        externalPayment.setPayTime(System.currentTimeMillis());
                                        externalPayment.setLoginId(externalPaymentDto.getLoginId());
                                        externalPayment.setOperator(admin.getName());
                                        externalPayment.setState(2);
                                        externalPaymentDao.updateByWayBillId(externalPayment);
                                    }

                                }
                            }
                        }catch (Exception e){
                            log.error("运单打款详情更新失败 waybillId = {}",waybillId);
                        }


                    }catch (Exception e){
                        if(result.length()>0){
                            result.append(",");
                        }
                        result.append(waybill.getNo());
                        result.append("打款失败");
                        log.error("运单打款失败 waybillId = {}",waybillId);
                    }
                }

            }else{
                result.append("运单不存在");
            }
        }
        if(result.length()>0){
            return BaseResultUtil.fail(result.toString());
        }
        return BaseResultUtil.success();
    }

    @Override
    public List<PaymentVo> exportPaymentExcel(FinanceQueryDto financeQueryDto) {

        List<PaymentVo> financeVoList = financeDao.getPaymentList(financeQueryDto);
        for(int i=0;i<financeVoList.size();i++){
            PaymentVo paymentVo = financeVoList.get(i);
            paymentVo.setFreightPay(paymentVo.getFreightPay()!=null?paymentVo.getFreightPay().divide(new BigDecimal(100)):null);
            paymentVo.setFreightReceivable(paymentVo.getFreightReceivable()!=null?paymentVo.getFreightReceivable().divide(new BigDecimal(100)):null);
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
        return financeVoList;
    }

    @Override
    public List<FinancePayableVo> exportPayableAll(PayableQueryDto payableQueryDto) {
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
                settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
            }
            financePayableVo.setRemainDate(financePayableVo.getSettlePeriod()-formatDuring(System.currentTimeMillis()-financePayableVo.getCompleteTime()));
            financePayableVo.setFreightPayable(freightFee.divide(new BigDecimal(100)));
        }
        return financeVoList;
    }

    @Override
    public List<SettlementVo> exportPayableCollect(WaitTicketCollectDto waitTicketCollectDto) {
        List<SettlementVo> settlementVoList = financeDao.getCollectTicketList(waitTicketCollectDto);
        return settlementVoList;
    }

    @Override
    public List<SettlementVo> exportPayment(WaitPaymentDto waitPaymentDto) {
        List<SettlementVo> settlementVoList = financeDao.getPayablePaymentList(waitPaymentDto);
        return settlementVoList;
    }

    @Override
    public List<PayablePaidVo> exportPaid(PayablePaidQueryDto payablePaidQueryDto) {
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(payablePaidQueryDto);
        return payablePaidList;
    }

    @Override
    public List<PaidNewVo> exportTimePaid(PayMentQueryDto payMentQueryDto) {

        if(payMentQueryDto.getState()!=null){
            if(payMentQueryDto.getState()!=0){
                payMentQueryDto.setState(1);
            }
        }
        List<PaidNewVo> paidNewVoList = new ArrayList<>();
        /*Config config = configDao.getByItemKey("external_pay");
        if(config!=null&&config.getState()==1) {//对外支付模式
            log.info("config.getState() "+config.getState().toString());
            paidNewVoList =  financeDao.getPaidListNew(payMentQueryDto);
        }else{//自动付款*/
            paidNewVoList = financeDao.getAutoPaidList(payMentQueryDto);;
        /*}*/

        for(int i=0;i<paidNewVoList.size();i++){
            PaidNewVo paidNewVo = paidNewVoList.get(i);
            paidNewVo.setFreightFee(paidNewVo.getFreightFee()!=null?paidNewVo.getFreightFee().divide(new BigDecimal(100)):null);
        }
        return paidNewVoList;
    }

    @Override
    public ResultVo<PageVo<CooperatorPaidVo>> getCooperatorPaidList() {
        return null;
    }
}
