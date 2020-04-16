package com.cjyc.web.api.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.utils.JsonUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerPayEnum;
import com.cjyc.common.model.enums.driver.DriverSettleTypeEnum;
import com.cjyc.common.model.enums.finance.NeedInvoiceStateEnum;
import com.cjyc.common.model.enums.finance.ReceiveSettlementStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillPayStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ExcelUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.CustomerInvoiceVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.web.api.service.ICustomerService;
import com.cjyc.web.api.service.IFinanceService;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.service.ITradeBillSummaryService;
import com.cjyc.web.api.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.MarshalledObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IOrderCarDao orderCarDao;

    @Resource
    private IWaybillCarDao waybillCarDao;

    @Resource
    private ITradeBillSummaryService tradeBillService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IReceiveSettlementDao receiveSettlementDao;

    @Resource
    private IReceiveSettlementDetailDao receiveSettlementDetailDao;

    @Override
    public ResultVo<PageVo<FinanceVo>> getFinanceList(FinanceQueryDto financeQueryDto) {
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<FinanceVo> financeVoList = financeDao.getFinanceList(financeQueryDto);
        for (FinanceVo financeVo : financeVoList) {
            if (financeVo != null) {
                String orderCarNo = financeVo.getNo();
                //获取车辆成本列表
                List<TrunkLineVo> costList = financeDao.getCostList(orderCarNo);

                //成本为账期的数据处理
                costList.stream().filter(e -> String.valueOf(DriverSettleTypeEnum.ACCOUNT_PERIOD.code).equals(e.getSettleType()))
                        .forEach(e -> {
                            //获取账期运单开票信息
                            AccountPeriodVo accountPeriodVo = financeDao.getAccountPeriodInfo(e.getWayBillNo());
                            if (accountPeriodVo != null) {
                                e.setPayTime(accountPeriodVo.getWriteOffTime());
                                e.setPayState(WaybillPayStateEnum.PAID.name);
                                e.setPaidFreightFee(e.getFreightFee());
                            } else {
                                e.setPayTime(null);
                                e.setPayState("");
                                e.setPaidFreightFee(null);
                            }
                        });
                //筛选出提车运单列表
                List<TrunkLineVo> pickUpCarList = costList.stream().filter(e -> e.getType() == WaybillTypeEnum.PICK.code).collect(Collectors.toList());
                //筛选出干线运单列表
                List<TrunkLineVo> trunkLineVoList = costList.stream().filter(e -> e.getType() == WaybillTypeEnum.TRUNK.code).collect(Collectors.toList());
                //筛选出送车运单列表
                List<TrunkLineVo> carryCarList = costList.stream().filter(e -> e.getType() == WaybillTypeEnum.BACK.code).collect(Collectors.toList());
                //提车成本费用
                financeVo.setPickUpCarFee(pickUpCarList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add));
                //干线成本费用
                financeVo.setTrunkLineFee(trunkLineVoList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add));
                //送车成本费用
                financeVo.setCarryCarFee(carryCarList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add));
                //成本合计
                BigDecimal totalCost = costList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add);
                //提车成本列表
                financeVo.setPickUpCarList(pickUpCarList);
                //干线成本列表
                financeVo.setTrunkLineVoList(trunkLineVoList);
                //送车成本列表
                financeVo.setCarryCarList(carryCarList);
                //成本合计
                financeVo.setTotalCost(totalCost);
                //毛利
                financeVo.setGrossProfit(financeVo.getTotalIncome().subtract(totalCost));
                //客户为账期的数据处理，查询开票金额
                if (financeVo.getPayMode() != null && CustomerPayEnum.PERIOD_PAY.code == financeVo.getPayMode()) {
                    FinanceSettlementDetailVo financeSettlementDetailVo = financeDao.getSettlementDetail(financeVo.getNo());
                    if (financeSettlementDetailVo != null) {
                        BigDecimal difference = MoneyUtil.nullToZero(financeSettlementDetailVo.getFreightFee()).subtract(MoneyUtil.nullToZero(financeSettlementDetailVo.getInvoiceFee()));
                        //差额
                        financeVo.setDifference(new BigDecimal(MoneyUtil.fenToYuan(difference, MoneyUtil.PATTERN_TWO)));
                        //开票金额
                        financeVo.setInvoiceFee(financeSettlementDetailVo.getInvoiceFee());
                        //付款时间
                        financeVo.setReceivedTime(financeSettlementDetailVo.getVerificationTime());
                    }
                }
            }
        }
        PageInfo<FinanceVo> pageInfo = new PageInfo<>(financeVoList);
        Map<String, Object> countInfo = new HashMap<>();
        //收益合计
        countInfo.put("incomeSummary", MoneyUtil.fenToYuan(tradeBillService.incomeSummary(financeQueryDto)));
        //成本合计
        countInfo.put("costSummary", MoneyUtil.fenToYuan(tradeBillService.costSummary(financeQueryDto)));
        //毛利汇总
        countInfo.put("grossProfit", MoneyUtil.fenToYuan(tradeBillService.grossProfit(financeQueryDto)));
        //实收汇总
        countInfo.put("actualReceiptSummary", MoneyUtil.fenToYuan(tradeBillService.financeActualReceiptSummary(financeQueryDto)));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo exportExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        log.info("financeQueryDto =" + financeQueryDto.toString());
        Map map = new HashMap();
        List<ExportFinanceVo> financeVoList = financeDao.getAllFinanceList(financeQueryDto);
        List<ExportFinanceDetailVo> detailVoList = new ArrayList<>();
        if (financeVoList == null) {
            return BaseResultUtil.success("未查询到结果");
        }
        for (ExportFinanceVo financeVo : financeVoList) {

            if (financeVo != null) {
                String orderCarNo = financeVo.getNo();
                //获取车辆成本列表
                List<TrunkLineVo> costList = financeDao.getCostList(orderCarNo);

                //筛选出提车运单列表
                List<TrunkLineVo> pickUpCarList = costList.stream().filter(e -> e.getType() == WaybillTypeEnum.PICK.code).collect(Collectors.toList());
                //筛选出干线运单列表
                List<TrunkLineVo> trunkLineVoList = costList.stream().filter(e -> e.getType() == WaybillTypeEnum.TRUNK.code).collect(Collectors.toList());
                //筛选出送车运单列表
                List<TrunkLineVo> carryCarList = costList.stream().filter(e -> e.getType() == WaybillTypeEnum.BACK.code).collect(Collectors.toList());
                //提车成本费用
                financeVo.setPickUpCarFee(MoneyUtil.fenToYuan(pickUpCarList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add)));
                //干线成本费用
                financeVo.setTrunkLineFee(MoneyUtil.fenToYuan(trunkLineVoList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add)));
                //送车成本费用
                financeVo.setCarryCarFee(MoneyUtil.fenToYuan(carryCarList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add)));
                //成本合计
                BigDecimal totalCost = costList.stream().map(TrunkLineVo::getFreightFee).reduce(BigDecimal.ZERO, BigDecimal::add);
                //毛利计算
                financeVo.setGrossProfit(MoneyUtil.fenToYuan((financeVo.getTotalIncome().subtract(totalCost))));

                financeVo.setTotalCost(MoneyUtil.fenToYuan(totalCost));
                //应收运费
                financeVo.setFreightReceivable(MoneyUtil.fenToYuan(financeVo.getFreightReceivable()));
                //分摊费用
                financeVo.setFeeShare(MoneyUtil.fenToYuan(financeVo.getFeeShare()));
                //实收运费
                financeVo.setAmountReceived(MoneyUtil.fenToYuan(financeVo.getAmountReceived()));
                //收益合计
                financeVo.setTotalIncome(MoneyUtil.fenToYuan(financeVo.getTotalIncome()));
                //实际收益
                financeVo.setActualIncome(MoneyUtil.nullToZero(financeVo.getTotalIncome()));

                //账期数据查询开票金额
                if (financeVo.getPayMode() != null && CustomerPayEnum.PERIOD_PAY.code == financeVo.getPayMode()) {
                    FinanceSettlementDetailVo financeSettlementDetailVo = financeDao.getSettlementDetail(financeVo.getNo());
                    if (financeSettlementDetailVo != null) {
                        BigDecimal difference = MoneyUtil.nullToZero(financeSettlementDetailVo.getFreightFee()).subtract(MoneyUtil.nullToZero(financeSettlementDetailVo.getInvoiceFee()));
                        //差额
                        financeVo.setDifference(new BigDecimal(MoneyUtil.fenToYuan(difference, MoneyUtil.PATTERN_TWO)));
                        //开票金额
                        financeVo.setInvoiceFee(new BigDecimal(MoneyUtil.fenToYuan(financeSettlementDetailVo.getInvoiceFee(), MoneyUtil.PATTERN_TWO)));
                        //付款时间
                        financeVo.setReceivedTime(financeSettlementDetailVo.getVerificationTime());
                    }
                }

                List<ExportFinanceDetailVo> detailList = financeDao.getFinanceDetailList(financeVo.getNo());
                if (!CollectionUtils.isEmpty(detailList)) {
                    detailList.forEach(e -> {
                        if (String.valueOf(DriverSettleTypeEnum.ACCOUNT_PERIOD.code).equals(e.getSettleType())) {
                            //获取账期运单开票信息
                            AccountPeriodVo accountPeriodVo = financeDao.getAccountPeriodInfo(e.getWayBillNo());
                            if (accountPeriodVo != null) {
                                e.setPayTime(accountPeriodVo.getWriteOffTime());
                                e.setPayState(WaybillPayStateEnum.PAID.name);
                                e.setPaidFreightFee(e.getFreightFee());
                            } else {
                                e.setPayTime(null);
                                e.setPayState("");
                                e.setPaidFreightFee(null);
                            }
                            e.setSettleType(DriverSettleTypeEnum.ACCOUNT_PERIOD.name);
                        } else {
                            e.setSettleType(DriverSettleTypeEnum.TIME_PAY.name);
                        }
                    });
                }
                detailVoList.addAll(detailList);
            }
        }
        /**
         * 总流水数据
         */
        ExportParams totalFlow = new ExportParams();
        totalFlow.setSheetName("总流水");
        Map<String, Object> totalFlowMap = new HashMap<>(4);
        totalFlowMap.put("title", totalFlow);
        totalFlowMap.put("entity", ExportFinanceVo.class);
        totalFlowMap.put("data", financeVoList);
        /**
         * 成本明细数据
         */
        ExportParams detail = new ExportParams();
        detail.setSheetName("成本明细");
        Map<String, Object> detailMap = new HashMap<>(4);
        detailMap.put("title", detail);
        detailMap.put("entity", ExportFinanceDetailVo.class);
        detailMap.put("data", detailVoList);

        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(totalFlowMap);
        sheetsList.add(detailMap);
        try {
            ExcelUtil.exportMultipleSheets(sheetsList, "财务总流水.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出财务总流水异常:", e);
            return BaseResultUtil.fail("导出财务总流水异常" + e.getMessage());
        }
    }

    private List<FinanceVo> getAllFinanceList(FinanceQueryDto financeQueryDto) {
        List<FinanceVo> financeVoList = financeDao.getFinanceList(financeQueryDto);
        for (FinanceVo financeVo : financeVoList) {
            if (financeVo != null) {
                String orderCarNo = financeVo.getNo();
                List<TrunkLineVo> trunkLineVoList = financeDao.getTrunkCostList(orderCarNo, 0);
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
        String state = "0";
        if (applySettlementDto != null && applySettlementDto.getIsInvoice() != null && applySettlementDto.getIsInvoice().equals("1")) {
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
            state = "1";
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
    public void confirmSettlement(String serialNumber, String invoiceNo) {
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
    public void writeOff(String serialNumber, String invoiceNo) {
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
        if (invoiceReceipt != null) {
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
        financeQueryDto.setSettleType(0);
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<PaymentVo> financeVoList = financeDao.getPaymentList(financeQueryDto);

        log.info("financeVoList.size ={}", financeVoList.size());
        PageInfo<PaymentVo> pageInfo = new PageInfo<>(financeVoList);
        //应收汇总数据
        BigDecimal receiptSummary = tradeBillService.receiptSummary(financeQueryDto);
        //实收汇总数据
        BigDecimal actualReceiptSummary = tradeBillService.actualReceiptSummary(financeQueryDto);

        //获取所有应收tab页总数
        Map<String, Object> countInfo = statisticCount();

        BigDecimal incomeSummary = tradeBillService.receiptIncomeSummary(financeQueryDto);

        countInfo.put("incomeSummary", incomeSummary.divide(new BigDecimal(100)));

        countInfo.put("receiptSummary", receiptSummary.divide(new BigDecimal(100)));

        countInfo.put("ActualReceiptSummary", actualReceiptSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo<PageVo<AdvancePaymentVo>> getAdvancePayment(FinanceQueryDto financeQueryDto) {
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<AdvancePaymentVo> advancePaymentVoList = financeDao.getAdvancePayment(financeQueryDto);

        PageInfo<AdvancePaymentVo> pageInfo = new PageInfo<>(advancePaymentVoList);

        //获取所有应收tab页总数
        Map<String, Object> countInfo = statisticCount();
        //未完结订单应收汇总
        BigDecimal advancePaymentSummary = tradeBillService.advancePaymentSummary(financeQueryDto);
        //未完结订单实收汇总
        BigDecimal actualAdvancePaymentSummary = tradeBillService.actualAdvancePaymentSummary(financeQueryDto);
        countInfo.put("advancePaymentSummary", new BigDecimal(MoneyUtil.fenToYuan(advancePaymentSummary, MoneyUtil.PATTERN_TWO)));
        countInfo.put("actualAdvancePaymentSummary", new BigDecimal(MoneyUtil.fenToYuan(actualAdvancePaymentSummary, MoneyUtil.PATTERN_TWO)));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo exportAdvancePaymentExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        List<AdvancePaymentVo> advancePaymentVoList = financeDao.getAdvancePayment(financeQueryDto);
        if (CollectionUtils.isEmpty(advancePaymentVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        if (!CollectionUtils.isEmpty(advancePaymentVoList)) {
            advancePaymentVoList.forEach(e -> {
                e.setFreightReceivable(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightReceivable(), MoneyUtil.PATTERN_TWO)));
                e.setFreightPay(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightPay(), MoneyUtil.PATTERN_TWO)));
            });
        }
        try {
            ExcelUtil.exportExcel(advancePaymentVoList, "已付订单未完结", "已付订单未完结", AdvancePaymentVo.class, "已付订单未完结.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出已付订单未完结异常:", e);
            return BaseResultUtil.fail("导出已付订单未完结异常!");
        }
    }

    /**
     * 获取应收所有table总数
     *
     * @return
     */
    private Map getReceivableCountInfo() {

        FinanceQueryDto fqd = new FinanceQueryDto();
        List<PaymentVo> pv = financeDao.getPaymentList(fqd);
        List<AdvancePaymentVo> ap = financeDao.getAdvancePayment(fqd);
        Map<String, Object> countInfo = new HashMap<>();
        countInfo.put("receiptCount", pv.size());
        countInfo.put("advancePaymentCount", ap.size());
        return countInfo;
    }

    @Override
    public ResultVo<PageVo<ReceiveOrderCarDto>> listPaymentDaysInfo(FinanceQueryDto financeQueryDto) {
        // 结算类型：1：账期 0：时付
        financeQueryDto.setSettleType(1);
        // 应收账款总额统计
        BigDecimal receiveSummary = MoneyUtil.nullToZero(financeDao.receiptSummary(financeQueryDto));
        // 应收账款列表分页查询
        PageHelper.startPage(financeQueryDto.getCurrentPage(), financeQueryDto.getPageSize());
        List<ReceiveOrderCarDto> receiveOrderCarDtoList = financeDao.listPaymentDaysInfo(financeQueryDto);
        // 金额分转元 + 剩余账期计算
        if (!CollectionUtils.isEmpty(receiveOrderCarDtoList)) {
            receiveOrderCarDtoList.forEach(e -> {
                e.setFreightReceivable(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightReceivable(), MoneyUtil.PATTERN_TWO)));
                e.setFreightPay(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightPay(), MoneyUtil.PATTERN_TWO)));
                if (e.getSettlePeriod() == null) {
                    e.setRemainderSettlePeriod(0);
                } else {
                    /**
                     * 剩余账期计算:
                     * 1.已过天数 = 当前时间 - 订单完结时间
                     * 2. 剩余账期 = 大客户合同中配置的账期 - 已过天数
                     */
                    e.setRemainderSettlePeriod(e.getSettlePeriod() - formatDuring(System.currentTimeMillis(), e.getOrderDeliveryDate()));
                }
            });
        }
        // 总条数+总金额统计封装
        Map<String, Object> countInfo = statisticCount();
        countInfo.put("receiveSummary", new BigDecimal(MoneyUtil.fenToYuan(receiveSummary, MoneyUtil.PATTERN_TWO)));
        PageInfo<ReceiveOrderCarDto> pageInfo = new PageInfo<>(receiveOrderCarDtoList);
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo exportPaymentDaysInfo(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        // 账期
        financeQueryDto.setSettleType(1);
        // 应收账款列表查询
        List<ReceiveOrderCarDto> receiveOrderCarDtoList = financeDao.listPaymentDaysInfo(financeQueryDto);
        if (CollectionUtils.isEmpty(receiveOrderCarDtoList)) {
            return BaseResultUtil.success("没数据无法导出！");
        }
        receiveOrderCarDtoList.forEach(e -> {
            e.setFreightReceivable(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightReceivable(), MoneyUtil.PATTERN_TWO)));
            e.setFreightPay(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightPay(), MoneyUtil.PATTERN_TWO)));
            if (e.getSettlePeriod() == null) {
                e.setRemainderSettlePeriod(0);
            } else {
                e.setRemainderSettlePeriod(e.getSettlePeriod() - formatDuring(System.currentTimeMillis(), e.getOrderDeliveryDate()));
            }
        });
        try {
            ExcelUtil.exportExcel(receiveOrderCarDtoList, "应收账款", "应收账款（账期）", ReceiveOrderCarDto.class, "应收账款.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出应收账款（账期）出现异常:", e);
            return BaseResultUtil.fail("导出应收账款（账期）出现异常");
        }
    }

    /**
     * 应收账款4个按钮的数字显示
     *
     * @return
     */
    private Map<String, Object> statisticCount() {
        Map<String, Object> countInfo = new HashMap<>();
        // 应收账款列表总条数查询
        List<ReceiveOrderCarDto> receiveFinanceCount = financeDao.listPaymentDaysInfo(new FinanceQueryDto());
        // 等待开票[账期]总条数
        List<ReceiveSettlementDto> needInvoiceCount = receiveSettlementDao.listReceiveSettlement(new ReceiveSettlementNeedInvoiceVo() {{
            setState(ReceiveSettlementStateEnum.APPLY_INVOICE.code);
        }});
        // 等待回款[账期]总条数
        List<ReceiveSettlementDto> needPayedCount = receiveSettlementDao.listReceiveSettlementNeedInvoice(new ReceiveSettlementNeedPayedVo());
        // 已收款[账期]总条数
        List<ReceiveSettlementDto> payedCount = receiveSettlementDao.listReceiveSettlement(new ReceiveSettlementNeedInvoiceVo() {{
            setState(ReceiveSettlementStateEnum.VERIFICATION.code);
        }});
        countInfo.put("receiveFinanceCount", CollectionUtils.isEmpty(receiveFinanceCount) ? 0 : receiveFinanceCount.size());
        countInfo.put("needInvoiceCount", CollectionUtils.isEmpty(needInvoiceCount) ? 0 : needInvoiceCount.size());
        countInfo.put("needPayedCount", CollectionUtils.isEmpty(needPayedCount) ? 0 : needPayedCount.size());
        countInfo.put("payedCount", CollectionUtils.isEmpty(payedCount) ? 0 : payedCount.size());


        List<PaymentVo> pv = financeDao.getPaymentList(new FinanceQueryDto());
        List<AdvancePaymentVo> ap = financeDao.getAdvancePayment(new FinanceQueryDto());
        countInfo.put("receiptCount", CollectionUtils.isEmpty(pv) ? 0 : pv.size());
        countInfo.put("advancePaymentCount", CollectionUtils.isEmpty(ap) ? 0 : ap.size());
        return countInfo;
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
        for (FinancePayableVo financePayableVo : financeVoList) {
            BigDecimal freightFee = new BigDecimal(0);
            List<String> list = new ArrayList<>();
            list.add(financePayableVo.getNo());
            List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);
            for (PayableTaskVo settlementVo : settlementVoList) {
                if (settlementVo != null && settlementVo.getFreightFee() != null) {
                    freightFee = freightFee.add(settlementVo.getFreightFee());
                }
                settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
            }
            financePayableVo.setRemainDate(financePayableVo.getSettlePeriod() - formatDuring(System.currentTimeMillis(), financePayableVo.getCompleteTime()));
            financePayableVo.setFreightPayable(freightFee.divide(new BigDecimal(100)));
        }
        Map countInfo = getCountInfo();
        PageInfo<FinancePayableVo> pageInfo = new PageInfo<>(financeVoList);
        BigDecimal payableAccountSummary = financeDao.payableAccountSummary(payableQueryDto);
        countInfo.put("payableAccountSummary", new BigDecimal(MoneyUtil.fenToYuan(payableAccountSummary, MoneyUtil.PATTERN_TWO)));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    private Map getCountInfo() {
        PayableQueryDto pqd = new PayableQueryDto();
        List<FinancePayableVo> fpv = financeDao.getFinancePayableList(pqd);
        WaitTicketCollectDto wc = new WaitTicketCollectDto();
        List<SettlementVo> sv = financeDao.getCollectTicketList(wc);
        WaitPaymentDto wp = new WaitPaymentDto();
        List<SettlementVo> payablePaymentList = financeDao.getPayablePaymentList(wp);
        PayablePaidQueryDto pp = new PayablePaidQueryDto();
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(pp);
        PayMentQueryDto pq = new PayMentQueryDto();
        List<PaidNewVo> paidList = financeDao.getAutoPaidList(pq);

        CooperatorSearchDto cs = new CooperatorSearchDto();
        List<CooperatorPaidVo> cooperatorPaidVoList = financeDao.getCooperatorPaidList(cs);

        Map<String, Object> countInfo = new HashMap<>();
        countInfo.put("payableCount", fpv.size());
        countInfo.put("waitTicketCount", sv.size());
        countInfo.put("waitPaymentCount", payablePaymentList.size());
        countInfo.put("paidCount", payablePaidList.size());
        countInfo.put("timePaidCount", paidList.size());
        countInfo.put("cooperstorCount", cooperatorPaidVoList.size());

        return countInfo;
    }

    /**
     * 计算两个日期相差几天
     *
     * @param endMilTime
     * @param startMilTime
     * @return
     */
    private int formatDuring(Long endMilTime, Long startMilTime) {
        return DateUtil.dateDiff(endMilTime, startMilTime);
    }

    @Override
    public ResultVo getSettlementPayable(List<String> taskNo) {
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(taskNo);
        return BaseResultUtil.success(settlementVoList);
    }

    @Override
    @Transactional
    public ResultVo apply(AppSettlementPayableDto appSettlementPayableDto) {
        List<String> list = appSettlementPayableDto.getTaskNo();
        String serialNumber = csSendNoService.getNo(SendNoTypeEnum.PAYMENT);
        SettlementVo settlementVo = new SettlementVo();
        settlementVo.setSerialNumber(serialNumber);
        settlementVo.setApplyTime(System.currentTimeMillis());
        settlementVo.setApplicantId(appSettlementPayableDto.getLoginId());
        settlementVo.setFreightFee(MoneyUtil.yuanToFen(appSettlementPayableDto.getFreightFee()));
        settlementVo.setState("0");
        financeDao.apply(settlementVo);

        for (String taskNo : list) {
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
        settlementVoList.forEach(e -> {
            e.setFreightFee(MoneyUtil.fenToYuan(e.getFreightFee()));
        });
        PageInfo<SettlementVo> pageInfo = new PageInfo<>(settlementVoList);
        Map countInfo = getCountInfo();
        BigDecimal waitCollectSummary = financeDao.waitCollectSummary(waitTicketCollectDto);
        countInfo.put("waitCollectSummary", MoneyUtil.fenToYuan(waitCollectSummary));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo getConfirmTicket(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            settlementVoList = financeDao.getSettlementInfo(list);
        }

        BigDecimal freightFee = new BigDecimal(0);
        for (PayableTaskVo settlementVo : settlementVoList) {
            if (settlementVo != null && settlementVo.getFreightFee() != null) {
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
        settlementVoList.forEach(e -> {
            e.setFreightFee(MoneyUtil.fenToYuan(e.getFreightFee()));
        });
        PageInfo<SettlementVo> pageInfo = new PageInfo<>(settlementVoList);
        Map countInfo = getCountInfo();
        BigDecimal paymentSummary = financeDao.paymentSummary(waitPaymentDto);
        countInfo.put("paymentSummary", MoneyUtil.fenToYuan(paymentSummary));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo getWriteOffTicket(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (PayableTaskVo settlementVo : settlementVoList) {
            if (settlementVo != null && settlementVo.getFreightFee() != null) {
                freightFee = freightFee.add(settlementVo.getFreightFee());
            }
            settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
        }

        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        SettlementVo settlementVo = financeDao.getPayableSettlement(serialNumber);
        if (settlementVo != null && settlementVo.getInvoiceNo() != null) {
            payableSettlementVo.setInvoiceNo(settlementVo.getInvoiceNo());
        }
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(freightFee.divide(new BigDecimal(100)));

        return BaseResultUtil.success(payableSettlementVo);
    }

    @Override
    public ResultVo writeOffPayable(WriteOffTicketDto writeOffTicketDto) {
        SettlementVo settlementVoTemp = financeDao.getPayableSettlement(writeOffTicketDto.getSerialNumber());
        if (settlementVoTemp == null) {
            return BaseResultUtil.fail("结算信息不存在！");
        }
        if (MoneyUtil.yuanToFen(writeOffTicketDto.getTotalFreightPay()).compareTo(settlementVoTemp.getFreightFee()) > 0) {
            return BaseResultUtil.fail("实付总费用不能大于应收总运费！");
        }
        SettlementVo settlementVo = new SettlementVo();
        settlementVo.setState("2");
        settlementVo.setSerialNumber(writeOffTicketDto.getSerialNumber());
        settlementVo.setWriteOffId(writeOffTicketDto.getWriteOffId());
        settlementVo.setWriteOffTime(System.currentTimeMillis());
        settlementVo.setTotalFreightPay(MoneyUtil.yuanToFen(writeOffTicketDto.getTotalFreightPay()));
        financeDao.writeOffPayable(settlementVo);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo paid(PayablePaidQueryDto payablePaidQueryDto) {
        PageHelper.startPage(payablePaidQueryDto.getCurrentPage(), payablePaidQueryDto.getPageSize());
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(payablePaidQueryDto);
        payablePaidList.forEach(e -> {
            e.setFreightFee(MoneyUtil.fenToYuan(e.getFreightFee()));
            e.setTotalFreightPay(MoneyUtil.fenToYuan(e.getTotalFreightPay()));
            e.setDifference(MoneyUtil.fenToYuan(e.getDifference()));
        });
        PageInfo<PayablePaidVo> pageInfo = new PageInfo<>(payablePaidList);
        Map countInfo = getCountInfo();
        Map map = financeDao.payablePaidSummary(payablePaidQueryDto);
        BigDecimal payableSummary = (BigDecimal) map.get("freightFee");
        BigDecimal payablePaidSummary = (BigDecimal) map.get("totalFreightPay");
        countInfo.put("payableSummary", MoneyUtil.fenToYuan(payableSummary));
        countInfo.put("payablePaidSummary", MoneyUtil.fenToYuan(payablePaidSummary));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo payableDetail(String serialNumber) {
        List<String> list = financeDao.getTaskNoList(serialNumber);
        List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);

        BigDecimal freightFee = new BigDecimal(0);
        for (PayableTaskVo settlementVo : settlementVoList) {
            if (settlementVo != null && settlementVo.getFreightFee() != null) {
                freightFee = freightFee.add(settlementVo.getFreightFee());
            }
            settlementVo.setFreightFee(MoneyUtil.fenToYuan(settlementVo.getFreightFee()));
        }
        PayableSettlementVo payableSettlementVo = new PayableSettlementVo();
        SettlementVo settlementVo = financeDao.getPayableSettlement(serialNumber);
        if (settlementVo != null && settlementVo.getInvoiceNo() != null) {
            payableSettlementVo.setInvoiceNo(settlementVo.getInvoiceNo());
        }
        payableSettlementVo.setPayableTaskVo(settlementVoList);
        payableSettlementVo.setTotalFreightFee(MoneyUtil.fenToYuan(freightFee));
        payableSettlementVo.setTotalFreightPaid(MoneyUtil.fenToYuan(settlementVo.getTotalFreightPay()));
        payableSettlementVo.setWriteOffTime(settlementVo.getWriteOffTime());
        return BaseResultUtil.success(payableSettlementVo);
    }

    @Override
    public ResultVo<PageVo<PaidNewVo>> getPaidListNew(PayMentQueryDto payMentQueryDto) {
        log.info("payMentQueryDto = " + payMentQueryDto.toString());
        List<PaidNewVo> financeVoList = getAutoPaidList(payMentQueryDto);

        for (PaidNewVo paidNewVo : financeVoList) {
            if (paidNewVo.getState().equals("支付失败")) {
                //查询承运商付款失败原因
                PaymentErrorLog paymentErrorLog = paymentErrorLogDao.selectOne(new QueryWrapper<PaymentErrorLog>()
                        .lambda().eq(PaymentErrorLog::getWaybillNo, paidNewVo.getWaybillNo())
                );
                //判断是否有失败原因记录
                if (ObjectUtils.isEmpty(paymentErrorLog)) {
                    paidNewVo.setFailReason("请联系管理员");
                } else {
                    paidNewVo.setFailReason(paymentErrorLog.getRemark());
                }
            }
        }
        log.info("financeVoList = " + financeVoList.size());
        PageInfo<PaidNewVo> pageInfo = new PageInfo<>(financeVoList);

        Map countInfo = getCountInfo();

        //承运商应付款汇总
        BigDecimal carrierSummary = tradeBillService.payToCarrierSummary(payMentQueryDto);
        countInfo.put("carrierSummary", carrierSummary.divide(new BigDecimal(100)));
        //承运商实付款汇总
        BigDecimal paidCarrierSummary = tradeBillService.paidToCarrierSummary(payMentQueryDto);
        countInfo.put("paidCarrierSummary", paidCarrierSummary.divide(new BigDecimal(100)));

        return BaseResultUtil.success(pageInfo, countInfo);
    }

    private List<PaidNewVo> getAutoPaidList(PayMentQueryDto payMentQueryDto) {
        PageHelper.startPage(payMentQueryDto.getCurrentPage(), payMentQueryDto.getPageSize());
        return financeDao.getAutoPaidList(payMentQueryDto);
    }

    private List<PaidNewVo> getExternalPaidList(PayMentQueryDto payMentQueryDto) {
        PageHelper.startPage(payMentQueryDto.getCurrentPage(), payMentQueryDto.getPageSize());
        return financeDao.getPaidListNew(payMentQueryDto);
    }

    @Override
    public ResultVo externalPayment(ExternalPaymentDto externalPaymentDto) {
        log.info("对外支付操作人Id={}", externalPaymentDto.getLoginId());
        List<Long> waybillIds = externalPaymentDto.getWaybillIds();
        StringBuilder result = new StringBuilder();
        for (Long waybillId : waybillIds) {
            Waybill waybill = waybillDao.selectById(waybillId);
            if (waybill != null) {
                if (waybill.getFreightPayState() == 1) {
                    if (result.length() > 0) {
                        result.append(",");
                    }
                    result.append(waybill.getNo());
                    result.append("已支付");
                } else {
                    try {
                        ResultVo resultVo = csPingPayService.allinpayToCarrierNew(waybillId);
                        log.info("resultVo错误码 ={}", resultVo.getCode());
                        if (resultVo.getCode() == 1) {
                            if (result.length() > 0) {
                                result.append(",");
                            }
                            result.append(waybill.getNo());
                            result.append(resultVo.getMsg());
                        }

                        try {
                            log.info("result = {}", result.toString());
                            if (result.length() == 0) {
                                if (externalPaymentDto != null && externalPaymentDto.getLoginId() != null) {
                                    Admin admin = csAdminService.getById(externalPaymentDto.getLoginId(), true);

                                    if (admin != null) {
                                        ExternalPayment ep = externalPaymentDao.getByWayBillId(waybillId);

                                        if (ep == null) {
                                            ExternalPayment externalPayment = new ExternalPayment();
                                            externalPayment.setWaybillId(waybillId);
                                            externalPayment.setPayTime(System.currentTimeMillis());
                                            externalPayment.setLoginId(externalPaymentDto.getLoginId());
                                            externalPayment.setOperator(admin.getName());
                                            externalPayment.setState(2);
                                            externalPaymentDao.insert(externalPayment);
                                        } else {
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
                            }
                        } catch (Exception e) {
                            log.error("运单打款详情更新失败 waybillId = {}", waybillId);
                        }


                    } catch (Exception e) {
                        if (result.length() > 0) {
                            result.append(",");
                        }
                        result.append(waybill.getNo());
                        result.append("打款失败");
                        log.error("运单打款失败 waybillId = {}", waybillId);
                    }
                }

            } else {
                result.append("运单不存在");
            }
        }
        if (result.length() > 0) {
            return BaseResultUtil.fail(result.toString());
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<Object> exportPaymentExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        List<PaymentVo> financeVoList = financeDao.getPaymentList(financeQueryDto);
        if (CollectionUtils.isEmpty(financeVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        for (PaymentVo paymentVo : financeVoList) {
            paymentVo.setFreightPay(paymentVo.getFreightPay() != null ? paymentVo.getFreightPay().divide(new BigDecimal(100)) : null);
            paymentVo.setFreightReceivable(paymentVo.getFreightReceivable() != null ? paymentVo.getFreightReceivable().divide(new BigDecimal(100)) : null);
            paymentVo.setTotalIncome(new BigDecimal(MoneyUtil.fenToYuan(paymentVo.getTotalIncome(), MoneyUtil.PATTERN_TWO)));
        }
        try {
            ExcelUtil.exportExcel(financeVoList, "应收账款", "已收款（时付）", PaymentVo.class, "应收账款.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出应收账款异常:", e);
            return BaseResultUtil.fail("导出应收账款异常!");
        }
    }

    @Override
    public ResultVo exportPayableAll(HttpServletResponse response, PayableQueryDto payableQueryDto) {
        List<FinancePayableVo> financeVoList = financeDao.getFinancePayableList(payableQueryDto);
        if (CollectionUtils.isEmpty(financeVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        for (FinancePayableVo financePayableVo : financeVoList) {
            BigDecimal freightFee = new BigDecimal(0);
            List<String> list = new ArrayList<>();
            list.add(financePayableVo.getNo());
            List<PayableTaskVo> settlementVoList = financeDao.getSettlementInfo(list);
            for (PayableTaskVo settlementVo : settlementVoList) {
                if (settlementVo != null && settlementVo.getFreightFee() != null) {
                    freightFee = freightFee.add(settlementVo.getFreightFee());
                }
                settlementVo.setFreightFee(settlementVo.getFreightFee().divide(new BigDecimal(100)));
            }
            financePayableVo.setRemainDate(financePayableVo.getSettlePeriod() - formatDuring(System.currentTimeMillis(), financePayableVo.getCompleteTime()));
            financePayableVo.setFreightPayable(freightFee.divide(new BigDecimal(100)));
        }
        try {
            ExcelUtil.exportExcel(financeVoList, "应付账款", "应付账款", FinancePayableVo.class, "应付账款.xls", response);
            return null;
        } catch (Exception e) {
            log.error("导出应付账款异常:", e);
            return BaseResultUtil.fail("导出应付账款异常" + e.getMessage());
        }
    }

    @Override
    public ResultVo exportPayableCollect(HttpServletResponse response, WaitTicketCollectDto waitTicketCollectDto) {
        List<SettlementVo> settlementVoList = financeDao.getCollectTicketList(waitTicketCollectDto);
        if (CollectionUtils.isEmpty(settlementVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        settlementVoList.forEach(e -> {
            e.setFreightFee(MoneyUtil.fenToYuan(e.getFreightFee()));
        });
        try {
            ExcelUtil.exportExcel(settlementVoList, "等待收票", "等待收票", SettlementVo.class, "等待收票.xls", response);
            return null;
        } catch (Exception e) {
            log.error("导出等待收票异常:", e);
            return BaseResultUtil.fail("导出等待收票异常");
        }
    }

    @Override
    public ResultVo exportPayment(HttpServletResponse response, WaitPaymentDto waitPaymentDto) {
        List<SettlementVo> settlementVoList = financeDao.getPayablePaymentList(waitPaymentDto);
        if (CollectionUtils.isEmpty(settlementVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        settlementVoList.forEach(e -> {
            e.setFreightFee(MoneyUtil.fenToYuan(e.getFreightFee()));
        });
        try {
            com.cjyc.web.api.util.ExcelUtil.exportExcel(settlementVoList, "等待付款", "等待付款", SettlementVo.class, "等待付款.xls", response);
            return null;
        } catch (Exception e) {
            log.error("导出等待付款异常:", e);
            return BaseResultUtil.fail("导出等待付款异常");
        }
    }

    @Override
    public ResultVo exportPaid(HttpServletResponse response, PayablePaidQueryDto payablePaidQueryDto) {
        List<PayablePaidVo> payablePaidList = financeDao.getPayablePaidList(payablePaidQueryDto);
        if (CollectionUtils.isEmpty(payablePaidList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        payablePaidList.forEach(e -> {
            e.setFreightFee(MoneyUtil.fenToYuan(e.getFreightFee()));
            e.setTotalFreightPay((MoneyUtil.fenToYuan(e.getTotalFreightPay())));
            e.setDifference(MoneyUtil.fenToYuan(e.getDifference()));

        });
        try {
            ExcelUtil.exportExcel(payablePaidList, "应付账款-已付款（账期）", "已付款（账期）", PayablePaidVo.class, "应付账款-已付款（账期）.xls", response);
            return null;
        } catch (Exception e) {
            log.error("导出应付账款-已付款（账期）异常:", e);
            return BaseResultUtil.fail("导出应付账款-已付款（账期）异常");
        }
    }

    @Override
    public ResultVo exportTimePaid(HttpServletResponse response, PayMentQueryDto payMentQueryDto) {
        List<PaidNewVo> paidNewVoList = financeDao.getAutoPaidList(payMentQueryDto);
        if (CollectionUtils.isEmpty(paidNewVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        for (PaidNewVo paidNewVo : paidNewVoList) {
            paidNewVo.setFreightFee(paidNewVo.getFreightFee() != null ? paidNewVo.getFreightFee().divide(new BigDecimal(100)) : null);
            paidNewVo.setFreightFeePayable(new BigDecimal(MoneyUtil.fenToYuan(paidNewVo.getFreightFeePayable(), MoneyUtil.PATTERN_TWO)));
            if (paidNewVo.getState().equals("支付失败")) {
                //查询承运商付款失败原因
                PaymentErrorLog paymentErrorLog = paymentErrorLogDao.selectOne(new QueryWrapper<PaymentErrorLog>()
                        .lambda().eq(PaymentErrorLog::getWaybillNo, paidNewVo.getWaybillNo())
                );
                //判断是否有失败原因记录
                if (ObjectUtils.isEmpty(paymentErrorLog)) {
                    paidNewVo.setFailReason("请联系管理员");
                } else {
                    paidNewVo.setFailReason(paymentErrorLog.getRemark());
                }
            }
        }
        try {
            com.cjyc.web.api.util.ExcelUtil.exportExcel(paidNewVoList, "应付账款-已付款（时付）", "已付款（时付）", PaidNewVo.class, "应付账款-已付款（时付）.xls", response);
            return null;
        } catch (Exception e) {
            log.error("导出应付账款-已付款（时付）异常:", e);
            return BaseResultUtil.fail("导出应付账款-已付款（时付）异常");
        }
    }

    @Override
    public ResultVo<PageVo<CooperatorPaidVo>> getCooperatorPaidList(CooperatorSearchDto cooperatorSearchDto) {
        PageHelper.startPage(cooperatorSearchDto.getCurrentPage(), cooperatorSearchDto.getPageSize());
        List<CooperatorPaidVo> cooperatorPaidVoList = financeDao.getCooperatorPaidList(cooperatorSearchDto);
        for (CooperatorPaidVo cooperatorPaidVo : cooperatorPaidVoList) {
            BigDecimal wlFee = getCooperatorFee(cooperatorPaidVo.getOrderId());
            //总费用
            cooperatorPaidVo.setWlFee(wlFee);
            BigDecimal serviceFee = MoneyUtil.nullToZero(cooperatorPaidVo.getTotalFee()).subtract(MoneyUtil.nullToZero(wlFee));
            //合伙人服务费
            cooperatorPaidVo.setServiceFee(MoneyUtil.nullToZero(serviceFee));
            if (cooperatorPaidVo.getState().equals("支付失败")) {
                //查询合伙人付款失败原因
                PaymentErrorLog paymentErrorLog = paymentErrorLogDao.selectOne(new QueryWrapper<PaymentErrorLog>()
                        .lambda().eq(PaymentErrorLog::getOrderNo, cooperatorPaidVo.getOrderNo())
                );
                //判断是否有失败原因记录
                if (ObjectUtils.isEmpty(paymentErrorLog)) {
                    cooperatorPaidVo.setDescription("请联系管理员");
                } else {
                    cooperatorPaidVo.setDescription(paymentErrorLog.getRemark());
                }
            }
            cooperatorPaidVo.setWlFee(MoneyUtil.nullToZero(cooperatorPaidVo.getWlFee()).divide(new BigDecimal(100)));
            cooperatorPaidVo.setServiceFee(MoneyUtil.nullToZero(cooperatorPaidVo.getServiceFee()).divide(new BigDecimal(100)));
            cooperatorPaidVo.setTotalFee(MoneyUtil.nullToZero(cooperatorPaidVo.getTotalFee()).divide(new BigDecimal(100)));

        }
        PageInfo<CooperatorPaidVo> pageInfo = new PageInfo<>(cooperatorPaidVoList);

        Map countInfo = getCountInfo();

        //合伙人应付汇总
        BigDecimal payCooperatorSummary = tradeBillService.payToCooperatorSummary(cooperatorSearchDto);
        countInfo.put("payCooperatorSummary", payCooperatorSummary.divide(new BigDecimal(100)));
        //合伙人实付汇总
        BigDecimal paidCooperatorSummary = tradeBillService.paidToCooperatorSummary(cooperatorSearchDto);
        countInfo.put("paidToCooperatorSummary", paidCooperatorSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo payToCooperator(CooperatorPaymentDto cooperatorPaymentDto) {

        //记录支付操作者
        StringBuilder result = new StringBuilder();
        try {
            if (cooperatorPaymentDto == null) {
                return BaseResultUtil.fail("参数为空");
            }
            if (cooperatorPaymentDto.getOrderId() == null) {
                return BaseResultUtil.fail("订单参数为空");
            }
            log.info("手动支付合伙人费用 cooperatorPaymentDto = {}", cooperatorPaymentDto.toString());
            ResultVo resultVo = csPingPayService.allinpayToCooperatorNew(cooperatorPaymentDto.getOrderId());
            log.info("resultVo错误码 ={}", resultVo.getCode());
            if (resultVo.getCode() == 1) {
                if (result.length() > 0) {
                    result.append(",");
                }
                result.append(resultVo.getMsg());
            }
            try {
                log.info("result = {}", result.toString());
                if (result.length() == 0) {
                    if (cooperatorPaymentDto != null && cooperatorPaymentDto.getLoginId() != null) {
                        Admin admin = csAdminService.getById(cooperatorPaymentDto.getLoginId(), true);

                        if (admin != null) {
                            ExternalPayment externalPayment = new ExternalPayment();
                            externalPayment.setPayTime(System.currentTimeMillis());
                            externalPayment.setLoginId(cooperatorPaymentDto.getLoginId());
                            externalPayment.setOperator(admin.getName());
                            externalPayment.setOrderId(cooperatorPaymentDto.getOrderId());
                            externalPayment.setState(2);
                            externalPaymentDao.insert(externalPayment);
                        }

                    }
                }
            } catch (Exception e) {
                log.error("合伙人打款详情新增失败 orderId = {}", cooperatorPaymentDto.getOrderId());
            }
        } catch (Exception e) {
            result.append("打款失败");
            log.error(e.getMessage(), e);
        }
        if (result.length() > 0) {
            return BaseResultUtil.fail(result.toString());
        }
        return BaseResultUtil.success("合伙人费用支付成功");
    }

    @Override
    public ResultVo exportCooperator(HttpServletResponse response, CooperatorSearchDto cooperatorSearchDto) {
        List<CooperatorPaidVo> cooperatorPaidVoList = financeDao.getCooperatorPaidList(cooperatorSearchDto);
        if (CollectionUtils.isEmpty(cooperatorPaidVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        for (CooperatorPaidVo cooperatorPaidVo : cooperatorPaidVoList) {
            BigDecimal wlFee = getCooperatorFee(cooperatorPaidVo.getOrderId());
            //总费用
            cooperatorPaidVo.setWlFee(wlFee);
            BigDecimal serviceFee = MoneyUtil.nullToZero(cooperatorPaidVo.getTotalFee()).subtract(MoneyUtil.nullToZero(wlFee));
            //合伙人服务费
            cooperatorPaidVo.setServiceFee(MoneyUtil.nullToZero(serviceFee));
            cooperatorPaidVo.setWlFee(MoneyUtil.nullToZero(cooperatorPaidVo.getWlFee()).divide(new BigDecimal(100)));
            cooperatorPaidVo.setServiceFee(MoneyUtil.nullToZero(cooperatorPaidVo.getServiceFee()).divide(new BigDecimal(100)));
            cooperatorPaidVo.setTotalFee(MoneyUtil.nullToZero(cooperatorPaidVo.getTotalFee()).divide(new BigDecimal(100)));
            if (cooperatorPaidVo.getState().equals("支付失败")) {
                //查询合伙人付款失败原因
                PaymentErrorLog paymentErrorLog = paymentErrorLogDao.selectOne(new QueryWrapper<PaymentErrorLog>()
                        .lambda().eq(PaymentErrorLog::getOrderNo, cooperatorPaidVo.getOrderNo())
                );
                //判断是否有失败原因记录
                if (ObjectUtils.isEmpty(paymentErrorLog)) {
                    cooperatorPaidVo.setDescription("请联系管理员");
                } else {
                    cooperatorPaidVo.setDescription(paymentErrorLog.getRemark());
                }
            }
            //公户
            if (cooperatorPaidVo != null && cooperatorPaidVo.getCardType().equals("公户")) {
                cooperatorPaidVo.setBankName("");
                cooperatorPaidVo.setIDCard("");
                cooperatorPaidVo.setCardName("");
                cooperatorPaidVo.setCardNo("");
            } else if (cooperatorPaidVo != null && cooperatorPaidVo.getCardType().equals("私户")) {
                cooperatorPaidVo.setPubCardNo("");
                cooperatorPaidVo.setBankNameDetail("");
                cooperatorPaidVo.setReceiveCardName("");
                cooperatorPaidVo.setProvinceName("");
                cooperatorPaidVo.setAreaName("");
            }
        }

        try {
            ExcelUtil.exportExcel(cooperatorPaidVoList, "合伙人付款（时付）", "付款（时付）", CooperatorPaidVo.class, "合伙人付款（时付）.xls", response);
            return null;
        } catch (Exception e) {
            log.error("导出合伙人付款异常:", e);
            return BaseResultUtil.fail("导出合伙人付款异常");
        }
    }

    @Override
    public ResultVo<List<ExportFinanceDetailVo>> getFinanceDetailList(String no) {
        List<ExportFinanceDetailVo> detailList = financeDao.getFinanceDetailList(no);
        return BaseResultUtil.success(detailList);
    }

    /**
     * 获取合伙人服务费
     *
     * @param orderId
     * @return
     */
    private BigDecimal getCooperatorFee(Long orderId) {
        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(orderId);
        BigDecimal wlFee = new BigDecimal(0);
        for (OrderCar orderCar : orderCarList) {
            if (orderCar != null) {
                if (orderCar.getPickFee() != null) {
                    wlFee = wlFee.add(MoneyUtil.nullToZero(orderCar.getPickFee()));
                }
                if (orderCar.getTrunkFee() != null) {
                    wlFee = wlFee.add(MoneyUtil.nullToZero(orderCar.getTrunkFee()));
                }
                if (orderCar.getBackFee() != null) {
                    wlFee = wlFee.add(MoneyUtil.nullToZero(orderCar.getBackFee()));
                }
                if (orderCar.getAddInsuranceFee() != null) {
                    wlFee = wlFee.add(MoneyUtil.nullToZero(orderCar.getAddInsuranceFee()));
                }
            }
        }

        return MoneyUtil.nullToZero(wlFee);
    }

    @Override
    public ResultVo<PageVo<DriverUpstreamPaidInfoVo>> listDriverUpstreamPaidInfo(String waybillNo) {
        if (StringUtils.isEmpty(waybillNo)) {
            BaseResultUtil.fail("运单单号不能为空！");
        }
        // 根据运单号查看上游付款状态列表
        List<DriverUpstreamPaidInfoVo> listInfo = waybillCarDao.listDriverUpstreamPaidInfo(waybillNo);
        // 订单金额分转元
        listInfo.forEach(e -> {
            e.setTotalFee(new BigDecimal(MoneyUtil.fenToYuan(e.getTotalFee(), MoneyUtil.PATTERN_TWO)));
        });
        // 列表分页
        PageInfo<DriverUpstreamPaidInfoVo> pageInfo = new PageInfo<>(listInfo);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo applyReceiveSettlement(ApplyReceiveSettlementVo applyReceiveSettlementVo) {
        // 应收账款结算申请
        return new ReceiveSettlementOperation(applyReceiveSettlementVo, null, null, null)
                .applyReceiveSettlement();
    }

    @Override
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedInvoice(ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        // 结算状态：1-已申请发票
        receiveSettlementNeedInvoiceVo.setState(ReceiveSettlementStateEnum.APPLY_INVOICE.code);
        // 计算金额汇总
        Map<String, BigDecimal> summaryMap = receiveSettlementDao.summaryReceiveSettlementAmt(receiveSettlementNeedInvoiceVo);
        // 应收账款结算-待开票列表查询, 此时查询结算状态为已经申请的结算信息列表
        PageHelper.startPage(receiveSettlementNeedInvoiceVo.getCurrentPage(), receiveSettlementNeedInvoiceVo.getPageSize());
        List<ReceiveSettlementDto> listInfo = receiveSettlementDao.listReceiveSettlement(receiveSettlementNeedInvoiceVo);
        // 列表金额字段分转元
        fenToYuan(listInfo);
        // 列表分页
        PageInfo<ReceiveSettlementDto> pageInfo = new PageInfo<>(listInfo);
        // 总条数+总金额统计封装
        Map<String, Object> countInfo = statisticCount();
        if (summaryMap != null) {
            countInfo.put("receiveSummary", new BigDecimal(MoneyUtil.fenToYuan(summaryMap.get("receiveSummary"), MoneyUtil.PATTERN_TWO)));
            countInfo.put("actualReceiveSummary", new BigDecimal(MoneyUtil.fenToYuan(summaryMap.get("actualReceiveSummary"), MoneyUtil.PATTERN_TWO)));
        } else {
            countInfo.put("receiveSummary", BigDecimal.ZERO);
            countInfo.put("actualReceiveSummary", BigDecimal.ZERO);
        }
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo exportReceiveSettlementNeedInvoice(HttpServletResponse response, ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        receiveSettlementNeedInvoiceVo.setState(ReceiveSettlementStateEnum.APPLY_INVOICE.code);
        List<ReceiveSettlementDto> listInfo = receiveSettlementDao.listReceiveSettlement(receiveSettlementNeedInvoiceVo);
        if (CollectionUtils.isEmpty(listInfo)) {
            return BaseResultUtil.success("没数据无法导出！");
        }
        // 金额分转元
        fenToYuan(listInfo);
        try {
            List<ReceiveSettlementNeedInvoiceExcelItemDto> newListInfo = new ArrayList<>();
            listInfo.forEach(e -> {
                ReceiveSettlementNeedInvoiceExcelItemDto item = new ReceiveSettlementNeedInvoiceExcelItemDto();
                BeanUtils.copyProperties(e, item);
                newListInfo.add(item);
            });
            ExcelUtil.exportExcel(newListInfo, "应收账款-待开票", "应收账款-待开票（账期）", ReceiveSettlementNeedInvoiceExcelItemDto.class, "应收账款-待开票.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出应收账款-待开票（账期）出现异常：", e);
            return BaseResultUtil.fail("导出应收账款-待开票（账期）出现异常");
        }
    }

    @Override
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedPayed(ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo) {
        // 计算金额汇总
        Map<String, BigDecimal> summaryMap = receiveSettlementDao.summaryReceiveSettlementNeedPayedAmt(receiveSettlementNeedPayedVo);
        PageHelper.startPage(receiveSettlementNeedPayedVo.getCurrentPage(), receiveSettlementNeedPayedVo.getPageSize());
        // 应收账款结算-待回款列表查询, 此时查询结算状态为【已确认和不需要开票】的结算信息列表
        List<ReceiveSettlementDto> listInfo = receiveSettlementDao.listReceiveSettlementNeedInvoice(receiveSettlementNeedPayedVo);
        // 金额分转元
        fenToYuan(listInfo);
        PageInfo<ReceiveSettlementDto> pageInfo = new PageInfo<>(listInfo);
        // 总条数+总金额统计封装
        Map<String, Object> countInfo = statisticCount();
        if (summaryMap != null) {
            countInfo.put("receiveSummary", new BigDecimal(MoneyUtil.fenToYuan(summaryMap.get("receiveSummary"), MoneyUtil.PATTERN_TWO)));
            countInfo.put("actualReceiveSummary", new BigDecimal(MoneyUtil.fenToYuan(summaryMap.get("actualReceiveSummary"), MoneyUtil.PATTERN_TWO)));
        } else {
            countInfo.put("receiveSummary", BigDecimal.ZERO);
            countInfo.put("actualReceiveSummary", BigDecimal.ZERO);
        }
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    @Override
    public ResultVo exportReceiveSettlementNeedPayed(HttpServletResponse response, ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo) {
        List<ReceiveSettlementDto> listInfo = receiveSettlementDao.listReceiveSettlementNeedInvoice(receiveSettlementNeedPayedVo);
        if (CollectionUtils.isEmpty(listInfo)) {
            return BaseResultUtil.success("没数据无法导出！");
        }
        // 金额分转元
        fenToYuan(listInfo);
        try {
            List<ReceiveSettlementExcelItemDto> newListInfo = new ArrayList<>();
            listInfo.forEach(e -> {
                ReceiveSettlementExcelItemDto item = new ReceiveSettlementExcelItemDto();
                BeanUtils.copyProperties(e, item);
                newListInfo.add(item);
            });
            ExcelUtil.exportExcel(newListInfo, "应收账款-待回款", "应收账款-待回款（账期）", ReceiveSettlementExcelItemDto.class, "应收账款-待回款.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出应收账款-待回款（账期）出现异常：", e);
            return BaseResultUtil.fail("导出应收账款-待回款（账期）出现异常");
        }
    }

    @Override
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementPayed(ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        // 结算状态：3-已核销
        receiveSettlementNeedInvoiceVo.setState(ReceiveSettlementStateEnum.VERIFICATION.code);
        // 计算金额汇总
        Map<String, BigDecimal> summaryMap = receiveSettlementDao.summaryReceiveSettlementAmt(receiveSettlementNeedInvoiceVo);
        PageHelper.startPage(receiveSettlementNeedInvoiceVo.getCurrentPage(), receiveSettlementNeedInvoiceVo.getPageSize());
        // 应收账款结算-已收款列表查询, 此时查询结算状态为已收款的结算信息列表
        List<ReceiveSettlementDto> listInfo = receiveSettlementDao.listReceiveSettlement(receiveSettlementNeedInvoiceVo);
        // 金额分转元
        fenToYuan(listInfo);
        // 列表分页
        PageInfo<ReceiveSettlementDto> pageInfo = new PageInfo<>(listInfo);
        // 总条数+总金额统计封装
        Map<String, Object> countInfo = statisticCount();
        if (summaryMap != null) {
            countInfo.put("receiveSummary", new BigDecimal(MoneyUtil.fenToYuan(summaryMap.get("receiveSummary"), MoneyUtil.PATTERN_TWO)));
            countInfo.put("actualReceiveSummary", new BigDecimal(MoneyUtil.fenToYuan(summaryMap.get("actualReceiveSummary"), MoneyUtil.PATTERN_TWO)));
        } else {
            countInfo.put("receiveSummary", BigDecimal.ZERO);
            countInfo.put("actualReceiveSummary", BigDecimal.ZERO);
        }
        return BaseResultUtil.success(pageInfo, countInfo);
    }

    /**
     * 金额分转元
     *
     * @param listInfo
     */
    private void fenToYuan(List<ReceiveSettlementDto> listInfo) {
        if (!CollectionUtils.isEmpty(listInfo)) {
            listInfo.forEach(e -> {
                e.setTotalReceivableFee(new BigDecimal(MoneyUtil.fenToYuan(e.getTotalReceivableFee(), MoneyUtil.PATTERN_TWO)));
                e.setTotalInvoiceFee(new BigDecimal(MoneyUtil.fenToYuan(e.getTotalInvoiceFee(), MoneyUtil.PATTERN_TWO)));
                e.setDifferenceFee(e.getTotalReceivableFee().subtract(e.getTotalInvoiceFee()));
            });
        }
    }

    @Override
    public ResultVo exportReceiveSettlementPayed(HttpServletResponse response, ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        receiveSettlementNeedInvoiceVo.setState(ReceiveSettlementStateEnum.VERIFICATION.code);
        List<ReceiveSettlementDto> listInfo = receiveSettlementDao.listReceiveSettlement(receiveSettlementNeedInvoiceVo);
        if (CollectionUtils.isEmpty(listInfo)) {
            return BaseResultUtil.success("没数据无法导出！");
        }
        // 金额分转元
        fenToYuan(listInfo);
        try {
            List<ReceiveSettlementExcelItemDto> newListInfo = new ArrayList<>();
            listInfo.forEach(e -> {
                ReceiveSettlementExcelItemDto item = new ReceiveSettlementExcelItemDto();
                BeanUtils.copyProperties(e, item);
                newListInfo.add(item);
            });
            ExcelUtil.exportExcel(newListInfo, "应收账款-已收款", "应收账款-已收款（账期）", ReceiveSettlementExcelItemDto.class, "应收账款-已收款.xls", response);
            return null;
        } catch (IOException e) {
            log.error("导出应收账款-已收款（账期）出现异常：", e);
            return BaseResultUtil.fail("导出应收账款-已收款（账期）出现异常");
        }
    }

    @Override
    public ResultVo cancelReceiveSettlement(CancelInvoiceVo cancelInvoiceVo) {
        return new ReceiveSettlementOperation(null, cancelInvoiceVo, null, null)
                .cancelReceiveSettlement();
    }

    @Override
    public ResultVo confirmInvoice(ConfirmInvoiceVo confirmInvoiceVo) {
        return new ReceiveSettlementOperation(null, null, confirmInvoiceVo, null)
                .confirmInvoice();
    }

    @Override
    public ResultVo verificationReceiveSettlement(VerificationReceiveSettlementVo verificationReceiveSettlementVo) {
        return new ReceiveSettlementOperation(null, null, null, verificationReceiveSettlementVo)
                .verificationReceiveSettlement();
    }

    @Override
    public ResultVo<ReceiveSettlementInvoiceDetailDto> listReceiveSettlementDetail(String serialNumber) {
        if (StringUtils.isEmpty(serialNumber)) {
            return BaseResultUtil.fail("结算流水号不能为空！");
        }
        // 查询结算信息
        ReceiveSettlement receiveSettlement = receiveSettlementDao.selectOne(new QueryWrapper<ReceiveSettlement>()
                .lambda()
                .eq(ReceiveSettlement::getSerialNumber, serialNumber)
        );
        // 查询结算明细
        List<ReceiveSettlementDetail> listInfo = receiveSettlementDetailDao.selectList(new QueryWrapper<ReceiveSettlementDetail>()
                .lambda()
                .eq(ReceiveSettlementDetail::getSerialNumber, serialNumber)
        );
        // 查询发票信息
        CustomerInvoice customerInvoice = null;
        if (receiveSettlement.getInvoiceId() != null) {
            customerInvoice = customerInvoiceDao.selectOne(new QueryWrapper<CustomerInvoice>()
                    .lambda()
                    .eq(CustomerInvoice::getId, receiveSettlement.getInvoiceId()));
        } else {
            customerInvoice = new CustomerInvoice();
        }
        // 金额分转元
        moneyFenToYuan(receiveSettlement, listInfo);
        // 组装返回结果
        CustomerInvoice finalCustomerInvoice = customerInvoice;
        ReceiveSettlementInvoiceDetailDto result = new ReceiveSettlementInvoiceDetailDto() {{
            setReceiveSettlementInvoiceDetailList(listInfo);
            setInvoice(finalCustomerInvoice);
            setReceiveSettlement(receiveSettlement);
        }};
        return BaseResultUtil.success(result);
    }

    /**
     * 金额分转元
     *
     * @param receiveSettlement
     * @param listInfo
     */
    private void moneyFenToYuan(ReceiveSettlement receiveSettlement, List<ReceiveSettlementDetail> listInfo) {
        if (!CollectionUtils.isEmpty(listInfo)) {
            listInfo.forEach(e -> {
                e.setFreightReceivable(new BigDecimal(MoneyUtil.fenToYuan(e.getFreightReceivable(), MoneyUtil.PATTERN_TWO)));
                e.setInvoiceFee(new BigDecimal(MoneyUtil.fenToYuan(e.getInvoiceFee(), MoneyUtil.PATTERN_TWO)));
            });
        }
        if (receiveSettlement != null) {
            receiveSettlement.setTotalReceivableFee(new BigDecimal(MoneyUtil.fenToYuan(receiveSettlement.getTotalReceivableFee(), MoneyUtil.PATTERN_TWO)));
            receiveSettlement.setTotalInvoiceFee(new BigDecimal(MoneyUtil.fenToYuan(receiveSettlement.getTotalInvoiceFee(), MoneyUtil.PATTERN_TWO)));
        }
    }

    /**
     * <p>上游账期功能实现封装类，这里封装了增删改接口，查询接口没什么逻辑就访问外围了</p>
     * <ol>
     *     <li>应收账款结算申请</li>
     *     <li>待开票列表-确认开票</li>
     *     <li>待开票列表-撤回</li>
     *     <li>待回款列表查询-核销</li>
     * </ol>
     */
    private class ReceiveSettlementOperation {
        // 接口请求参数
        final ApplyReceiveSettlementVo applyReceiveSettlementVo;
        final CancelInvoiceVo cancelInvoiceVo;
        final ConfirmInvoiceVo confirmInvoiceVo;
        final VerificationReceiveSettlementVo verificationReceiveSettlementVo;

        private ReceiveSettlementOperation(ApplyReceiveSettlementVo applyReceiveSettlementVo, CancelInvoiceVo cancelInvoiceVo, ConfirmInvoiceVo confirmInvoiceVo, VerificationReceiveSettlementVo verificationReceiveSettlementVo) {
            this.applyReceiveSettlementVo = applyReceiveSettlementVo;
            this.cancelInvoiceVo = cancelInvoiceVo;
            this.confirmInvoiceVo = confirmInvoiceVo;
            this.verificationReceiveSettlementVo = verificationReceiveSettlementVo;
        }

        /**
         * 应收账款结算申请逻辑处理
         *
         * @return
         */
        private ResultVo applyReceiveSettlement() {
            // 参数校验
            ResultVo result = validateApplyReceiveSettlementParams(applyReceiveSettlementVo);
            if (result.getCode() != ResultEnum.SUCCESS.getCode()) {
                return result;
            }
            return transactionTemplate.execute(status -> {
                try {
                    log.info("应收账款结算申请开始！");
                    // 新增发票
                    int needVoice = applyReceiveSettlementVo.getNeedVoice();
                    CustomerInvoice customerInvoice = new CustomerInvoice();
                    // 默认不需要开发票
                    Integer state = ReceiveSettlementStateEnum.UNNEEDED_INVOICE.code;
                    // 需要开发票
                    if (needVoice == NeedInvoiceStateEnum.NEEDED_INVOICE.code) {
                        CustomerInvoiceVo customerInvoiceVo = applyReceiveSettlementVo.getCustomerInvoiceVo();
                        BeanUtils.copyProperties(customerInvoiceVo, customerInvoice);
                        customerInvoiceDao.insert(customerInvoice);
                        state = ReceiveSettlementStateEnum.APPLY_INVOICE.code;
                    }
                    // 新增应收账款
                    String serialNumber = csSendNoService.getNo(SendNoTypeEnum.RECEIPT);
                    String applicantName = applyReceiveSettlementVo.getLoginName();
                    if (StringUtils.isEmpty(applicantName)) {
                        Admin admin = csAdminService.validate(applyReceiveSettlementVo.getLoginId());
                        applicantName = admin.getName();
                    }

                    Integer finalState = state;
                    String finalApplicantName = applicantName;
                    receiveSettlementDao.insert(new ReceiveSettlement() {
                        {
                            // 结算流水号
                            setSerialNumber(serialNumber);
                            // 应收账款
                            setTotalReceivableFee(MoneyUtil.yuanToFen(applyReceiveSettlementVo.getTotalReceivableFee()));
                            // 开票金额
                            setTotalInvoiceFee(MoneyUtil.yuanToFen(applyReceiveSettlementVo.getTotalInvoiceFee()));
                            // 发票id
                            setInvoiceId(customerInvoice.getId());
                            // 结算申请状态
                            setState(finalState);
                            // 申请人id
                            setApplicantId(applyReceiveSettlementVo.getLoginId());
                            // 申请人
                            setApplicantName(finalApplicantName);
                            // 申请时间
                            setApplyTime(System.currentTimeMillis());
                        }
                    });
                    // 新增账款明细
                    if (!CollectionUtils.isEmpty(applyReceiveSettlementVo.getReceiveSettlementDetailList())) {
                        applyReceiveSettlementVo.getReceiveSettlementDetailList().forEach(e -> {
                            ReceiveSettlementDetail receiveSettlementDetail = new ReceiveSettlementDetail();
                            BeanUtils.copyProperties(e, receiveSettlementDetail);
                            receiveSettlementDetail.setFreightReceivable(MoneyUtil.yuanToFen(receiveSettlementDetail.getFreightReceivable()));
                            receiveSettlementDetail.setInvoiceFee(MoneyUtil.yuanToFen(receiveSettlementDetail.getInvoiceFee()));
                            // 结算流水号
                            receiveSettlementDetail.setSerialNumber(serialNumber);
                            receiveSettlementDetailDao.insert(receiveSettlementDetail);
                        });
                    }
                    log.info("应收账款结算申请结束！");
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("应收账款结算申请发生异常！", e);
                    return BaseResultUtil.fail("应收账款结算申请失败！");
                }
                return BaseResultUtil.success("应收账款结算申请成功！");
            });
        }

        /**
         * 应收账款-待开票-撤回
         *
         * @return
         */
        public ResultVo cancelReceiveSettlement() {
            if (StringUtils.isEmpty(cancelInvoiceVo.getSerialNumber())) {
                return BaseResultUtil.fail("结算流水号不能为空！");
            }
            if (StringUtils.isEmpty(cancelInvoiceVo.getLoginId())) {
                return BaseResultUtil.fail("撤回业务员id不能为空！");
            }
            return transactionTemplate.execute(status -> {
                try {
                    // 查询原结算明细
                    List<ReceiveSettlementDetail> listInfo = receiveSettlementDetailDao.selectList(
                            new QueryWrapper<ReceiveSettlementDetail>()
                                    .lambda()
                                    .eq(ReceiveSettlementDetail::getSerialNumber, cancelInvoiceVo.getSerialNumber())
                    );
                    // 删除结算信息
                    receiveSettlementDao.delete(new QueryWrapper<ReceiveSettlement>()
                            .lambda()
                            .eq(ReceiveSettlement::getSerialNumber, cancelInvoiceVo.getSerialNumber())
                    );

                    // 删除结算明细信息
                    receiveSettlementDetailDao.delete(new QueryWrapper<ReceiveSettlementDetail>()
                            .lambda()
                            .eq(ReceiveSettlementDetail::getSerialNumber, cancelInvoiceVo.getSerialNumber())
                    );
                    String cancelName = cancelInvoiceVo.getLoginName();
                    if (StringUtils.isEmpty(cancelInvoiceVo.getLoginName())) {
                        Admin admin = csAdminService.validate(cancelInvoiceVo.getLoginId());
                        cancelName = admin.getName();
                    }
                    log.info("业务员：{}撤销结算流水号为：{}的结算申请数据：{}", cancelName, cancelInvoiceVo.getSerialNumber(), CollectionUtils.isEmpty(listInfo) ? "" : JsonUtil.toJson(listInfo));
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("应收账款-待开票-撤回发生异常！", e);
                    return BaseResultUtil.fail("应收账款-待开票-撤回失败！");
                }
                return BaseResultUtil.success("应收账款-待开票-撤回成功！");
            });
        }

        /**
         * 应收账款-待开票-确认开票
         *
         * @return
         */
        public ResultVo confirmInvoice() {
            if (StringUtils.isEmpty(confirmInvoiceVo.getSerialNumber())) {
                return BaseResultUtil.fail("结算流水号不能为空！");
            }
            if (confirmInvoiceVo.getLoginId() == null) {
                return BaseResultUtil.fail("确认开票确认人id不能为空！");
            }
            String confirmName = confirmInvoiceVo.getLoginName();
            if (StringUtils.isEmpty(confirmInvoiceVo.getLoginName())) {
                Admin admin = csAdminService.validate(confirmInvoiceVo.getLoginId());
                confirmName = admin.getName();
            }
            log.info("业务员：{}确认开票结算流水号为：{}", confirmName, confirmInvoiceVo.getSerialNumber());
            String finalConfirmName = confirmName;
            int updated = receiveSettlementDao.update(new ReceiveSettlement() {{
                setInvoiceNo(confirmInvoiceVo.getInvoiceNo());
                setConfirmId(confirmInvoiceVo.getLoginId());
                setConfirmName(finalConfirmName);
                setConfirmTime(System.currentTimeMillis());
                setState(ReceiveSettlementStateEnum.CONFIRM_INVOICE.code);
            }}, new QueryWrapper<ReceiveSettlement>()
                    .lambda()
                    .eq(ReceiveSettlement::getSerialNumber, confirmInvoiceVo.getSerialNumber()));
            if (updated < 1) {
                return BaseResultUtil.fail("应收账款-待开票-确认开票失败！");
            }
            return BaseResultUtil.success("应收账款-待开票-确认开票成功！");
        }

        /**
         * 应收账款-待回款-核销
         *
         * @return
         */
        public ResultVo verificationReceiveSettlement() {
            if (StringUtils.isEmpty(verificationReceiveSettlementVo.getSerialNumber())) {
                return BaseResultUtil.fail("结算流水号不能为空！");
            }
            if (verificationReceiveSettlementVo.getLoginId() == null) {
                return BaseResultUtil.fail("核销人id不能为空！");
            }
            String verificationName = verificationReceiveSettlementVo.getLoginName();
            if (StringUtils.isEmpty(verificationReceiveSettlementVo.getLoginName())) {
                Admin admin = csAdminService.validate(verificationReceiveSettlementVo.getLoginId());
                verificationName = admin.getName();
            }
            log.info("业务员：{}核销结算流水号为：{}", verificationName, verificationReceiveSettlementVo.getSerialNumber());
            String finalVerificationName = verificationName;
            int updated = receiveSettlementDao.update(new ReceiveSettlement() {{
                setVerificationId(verificationReceiveSettlementVo.getLoginId());
                setVerificationName(finalVerificationName);
                setState(ReceiveSettlementStateEnum.VERIFICATION.code);
                setVerificationTime(System.currentTimeMillis());
            }}, new QueryWrapper<ReceiveSettlement>()
                    .lambda()
                    .eq(ReceiveSettlement::getSerialNumber, verificationReceiveSettlementVo.getSerialNumber()));
            if (updated < 1) {
                return BaseResultUtil.fail("应收账款-待回款-核销失败！");
            }
            return BaseResultUtil.success("应收账款-待回款-核销成功！");
        }

        /**
         * 应收账款申请参数校验：
         * 1.应收运费,开票金额校验
         * 2.订单车辆ID集合校验
         * 3.发票参数校验
         *
         * @param applyReceiveSettlementVo
         * @return
         */
        private ResultVo validateApplyReceiveSettlementParams(ApplyReceiveSettlementVo applyReceiveSettlementVo) {
            BigDecimal totalReceivableFee = applyReceiveSettlementVo.getTotalReceivableFee();
            BigDecimal totalInvoiceFee = applyReceiveSettlementVo.getTotalInvoiceFee();
            if (applyReceiveSettlementVo.getLoginId() == null) {
                return BaseResultUtil.fail("申请人id不能为空！");
            }
            if (totalReceivableFee == null || totalReceivableFee.compareTo(BigDecimal.ZERO) < 0) {
                return BaseResultUtil.fail("应收运费不能为空或负值");
            }
            if (totalInvoiceFee == null || totalInvoiceFee.compareTo(BigDecimal.ZERO) < 0) {
                return BaseResultUtil.fail("开票金额不能为空或负值");
            }
            if (CollectionUtils.isEmpty(applyReceiveSettlementVo.getReceiveSettlementDetailList())) {
                return BaseResultUtil.fail("结算单明细集合不能为空");
            } else {
                for (ReceiveSettlementDetailVo e : applyReceiveSettlementVo.getReceiveSettlementDetailList()) {
                    if (e.getOrderCarId() == null) {
                        return BaseResultUtil.fail("结算单明细-订单车辆id不能为空");
                    }
                    if (StringUtils.isEmpty(e.getCustomerName())) {
                        return BaseResultUtil.fail("结算单明细-客户名称不能为空！");
                    }
                    if (StringUtils.isEmpty(e.getNo())) {
                        return BaseResultUtil.fail("结算单明细-车辆编码不能为空！");
                    }
                    if (StringUtils.isEmpty(e.getVin())) {
                        return BaseResultUtil.fail("结算单明细-vin码不能为空！");
                    }
                    if (StringUtils.isEmpty(e.getBrand())) {
                        return BaseResultUtil.fail("结算单明细-品牌不能为空！");
                    }
                    if (StringUtils.isEmpty(e.getModel())) {
                        return BaseResultUtil.fail("结算单明细-车系不能为空！");
                    }
                    if (StringUtils.isEmpty(e.getStartAddress())) {
                        return BaseResultUtil.fail("结算单明细-始发地不能为空！");
                    }
                    if (StringUtils.isEmpty(e.getEndAddress())) {
                        return BaseResultUtil.fail("结算单明细-目的地不能为空！");
                    }
                    if (e.getDeliveryDate() == null) {
                        return BaseResultUtil.fail("结算单明细-交付日期不能为空！");
                    }
                    if (e.getFreightReceivable() == null || e.getFreightReceivable().compareTo(BigDecimal.ZERO) < 0) {
                        return BaseResultUtil.fail("结算单明细-应收运费不能为空或负值");
                    }
                    if (e.getInvoiceFee() == null || e.getInvoiceFee().compareTo(BigDecimal.ZERO) < 0) {
                        return BaseResultUtil.fail("结算单明细-开票金额不能为空或负值");
                    }
                }
            }
            /**
             * 不需要开票下面的开票就不校验
             */
            if (NeedInvoiceStateEnum.UNNEEDED_INVOICE.code == applyReceiveSettlementVo.getNeedVoice()) {
                return BaseResultUtil.success();
            }
            CustomerInvoiceVo customerInvoiceVo = applyReceiveSettlementVo.getCustomerInvoiceVo();
            if (customerInvoiceVo != null && customerInvoiceVo.getType() != null) {
                switch (customerInvoiceVo.getType()) {
                    case 1:
                        if (StringUtils.isEmpty(customerInvoiceVo.getName())) {
                            return BaseResultUtil.fail("新增个人普票-客户名称不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupPerson())) {
                            return BaseResultUtil.fail("新增个人普票-收件人不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupPhone())) {
                            return BaseResultUtil.fail("新增个人普票-收件人电话不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupAddress())) {
                            return BaseResultUtil.fail("新增个人普票-邮寄地址不能为空");
                        }
                        break;
                    case 2:
                        if (StringUtils.isEmpty(customerInvoiceVo.getName())) {
                            return BaseResultUtil.fail("新增公司普票-客户名称不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getTaxCode())) {
                            return BaseResultUtil.fail("新增公司普票-纳税人识别号不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupPerson())) {
                            return BaseResultUtil.fail("新增公司普票-收件人不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupPhone())) {
                            return BaseResultUtil.fail("新增公司普票-收件人电话不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupAddress())) {
                            return BaseResultUtil.fail("新增公司普票-邮寄地址不能为空");
                        }
                        break;
                    case 3:
                        if (StringUtils.isEmpty(customerInvoiceVo.getName())) {
                            return BaseResultUtil.fail("新增公司专票-客户名称不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getTaxCode())) {
                            return BaseResultUtil.fail("新增公司专票-纳税人识别号不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getInvoiceAddress())) {
                            return BaseResultUtil.fail("新增公司专票-地址不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getTel())) {
                            return BaseResultUtil.fail("新增公司专票-电话不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getBankName())) {
                            return BaseResultUtil.fail("新增公司专票-开户行不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getBankAccount())) {
                            return BaseResultUtil.fail("新增公司专票-银行账号不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupPerson())) {
                            return BaseResultUtil.fail("新增公司专票-收件人不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupPhone())) {
                            return BaseResultUtil.fail("新增公司专票-收件人电话不能为空");
                        }
                        if (StringUtils.isEmpty(customerInvoiceVo.getPickupAddress())) {
                            return BaseResultUtil.fail("新增公司专票-邮寄地址不能为空");
                        }
                        break;
                    default:
                        //不做处理
                }
            }
            return BaseResultUtil.success();
        }
    }
}
