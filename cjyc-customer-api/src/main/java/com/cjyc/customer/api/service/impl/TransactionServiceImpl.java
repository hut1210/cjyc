package com.cjyc.customer.api.service.impl;

import com.Pingxx.model.MetaDataEntiy;
import com.Pingxx.model.PingxxMetaData;
import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dto.web.WayBillCarrierDto;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.log.OrderLogEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.util.MiaoxinSmsUtil;
import com.cjyc.customer.api.service.IOrderService;
import com.pingplusplus.model.*;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.ChargeTypeEnum;
import com.cjyc.common.model.enums.PayStateEnum;
import com.cjyc.common.model.enums.Pingxx.ChannelEnum;
import com.cjyc.common.model.enums.Pingxx.LiveModeEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.BeanMapUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.customer.api.service.ITransactionService;
import com.pingplusplus.model.Order;
import com.pingplusplus.model.Refund;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @Author:Hut
 * @Date:2019/11/20 16:40
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class TransactionServiceImpl implements ITransactionService {

    @Resource
    private ITradeBillDao tradeBillDao;

    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private ExecutorService executorService;

    @Resource
    private ICsSendNoService csSendNoService;

    @Resource
    private ITradeBillDetailDao tradeBillDetailDao;
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsUserService userService;

    @Resource
    private ITaskDao taskDao;

    @Resource
    private IWaybillDao waybillDao;

    @Resource
    private IOrderDao orderDao;

    @Resource
    private ITaskCarDao taskCarDao;

    @Resource
    private IWaybillCarDao waybillCarDao;

    @Resource
    private ICsOrderLogService csOrderLogService;

    @Resource
    private IOrderCarDao orderCarDao;

    @Resource
    private ICsPingPayService csPingPayService;

    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderRefundDao orderRefundDao;
    @Resource
    private ICsPushMsgService csPushMsgService;

    @Resource
    private ICsSmsService csSmsService;

    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsAmqpService csAmqpService;

    @Resource
    private ICarrierDao carrierDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(Object obj) {
        TradeBill bill;
        if (obj instanceof TradeBill) {
            tradeBillDao.insert((TradeBill) obj);
        } else if (obj instanceof Order) {
            //根据ping++订单创建账单
            Order order = (Order) obj;
            MetaDataEntiy metaDataEntiy = BeanMapUtil.mapToBean(order.getMetadata(), new MetaDataEntiy());
            int chargeType = Integer.valueOf(metaDataEntiy.getChargeType());
            bill = orderToTradeBill(order, null, PayStateEnum.UNPAID.code);
            tradeBillDao.insert(bill);
            //预付
            if (chargeType == ChargeTypeEnum.PREPAY.getCode() || chargeType == ChargeTypeEnum.PREPAY_QRCODE.getCode()) {

            }
            //到付
            if (chargeType == ChargeTypeEnum.COLLECT_PAY.getCode() || chargeType == ChargeTypeEnum.COLLECT_QRCODE.getCode()) {
                List<String> orderNos = Arrays.asList(metaDataEntiy.getSourceNos().split(","));
                tradeBillDetailDao.saveBatch(bill.getId(), orderNos);
            }
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo update(Charge charge, Event event, String state) {

        TradeBill tradeBill = new TradeBill();
        tradeBill.setPingPayId(charge.getId());
        tradeBill.setState(2);
        tradeBill.setTradeTime(System.currentTimeMillis());
        tradeBillDao.updateTradeBillByPingPayId(tradeBill);

        Map<String, Object> metadata = charge.getMetadata();

        PingxxMetaData pingxxMetaData = BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
        log.debug("update metadata = " + metadata.toString() + " taskId = " + metadata.get("taskId"));
        String chargeType = pingxxMetaData.getChargeType();

        //chargeType 1、app预付 3司机端出示二维码付款 4业务员端出示二维码 5后台出示二维码预付 6后台出库出示二维码付款
        if (chargeType != null && !chargeType.equals(String.valueOf(ChargeTypeEnum.PREPAY.getCode()))) {

            log.info("update chargeType=" + chargeType);

            if (chargeType != null) {
                if (chargeType.equals(String.valueOf(ChargeTypeEnum.WEB_PREPAY_QRCODE.getCode())) ||
                        chargeType.equals(String.valueOf(ChargeTypeEnum.SALES_PREPAY_QRCODE.getCode()))) {
                    log.info("后台物流费预付回调，charge = {}", charge.toString());

                    log.info("预付码回调" + chargeType);
                    String orderNo = pingxxMetaData.getOrderNo();
                    log.info(chargeType + " 物流费预付 orderNo =" + orderNo);
                    updateForPrePay(pingxxMetaData);
                    //验证订单金额是否一致
                    checkOrderFee(orderNo, charge.getAmount());
                }
                if (chargeType.equals(String.valueOf(ChargeTypeEnum.DRIVER_COLLECT_QRCODE.getCode()))
                        || chargeType.equals(String.valueOf(ChargeTypeEnum.WEB_OUT_STOCK_QRCODE.getCode()))
                        || chargeType.equals(String.valueOf(ChargeTypeEnum.SALESMAN_COLLECT_QRCODE.getCode()))) {
                    Long taskId = Long.valueOf((String) metadata.get("taskId"));

                    List<String> taskCarIdList = pingxxMetaData.getTaskCarIdList();

                    List<String> orderCarNosList = pingxxMetaData.getOrderCarIds();
                    log.info("【二维码支付回调】司机出示二维码回调或者后台出库回调");
                    Task task = null;
                    if (taskId == null) {
                        log.error("回调中参数taskId不存在");
                        return BaseResultUtil.fail("缺少参数taskId");
                    } else {
                        task = taskDao.selectById(taskId);
                        if (task == null) {
                            log.error(taskId + " 任务不存在");
                            return BaseResultUtil.fail(taskId + "任务不存在");
                        } else {
                            //验证运单
                            Waybill waybill = waybillDao.selectById(task.getWaybillId());
                            if (waybill == null) {
                                log.error("运单不存在");
                                return BaseResultUtil.fail("运单不存在");
                            }
                            if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
                                log.error(waybill.getNo() + "运单已完结");
                                return BaseResultUtil.fail(waybill.getNo() + "运单已完结");
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(orderCarNosList)) {
                        log.error("回调中参数orderCarNosList不存在");
                        return BaseResultUtil.fail("缺少参数orderCarNosList");
                    }

                    //修改车辆支付状态
                    UserInfo userInfo = new UserInfo();
                    try {
                        userInfo = userService.getUserInfo(Long.valueOf(pingxxMetaData.getLoginId()), Integer.valueOf(pingxxMetaData.getLoginType()));
                    } catch (Exception e) {
                        log.error("【支付回调-二维码】获取用户信息失败{}", JSON.toJSONString(metadata));
                        log.error(e.getMessage(), e);
                    }
                    csTaskService.updateForTaskCarFinish(taskCarIdList, ChargeTypeEnum.COLLECT_PAY.getCode(), userInfo);

                    //验证任务是否完成
                    int row = taskCarDao.countUnFinishByTaskId(taskId);
                    log.debug("验证任务是否完成 taskId = {},row = {}", taskId, row);
                    if (row == 0) {

                        //更新任务状态
                        taskDao.updateStateById(task.getId(), TaskStateEnum.FINISHED.code);
                        //验证运单是否完成
                        int n = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
                        log.debug("验证运单是否完成 waybillId = {},n = {}", task.getWaybillId(), n);
                        if (n == 0) {
                            log.debug("更新运单状态");
                            //更新运单状态
                            tradeBillDao.updateForReceipt(task.getWaybillId(), System.currentTimeMillis());
                            /*try{
                                csPingPayService.allinpayToCarrier(task.getWaybillId());
                            }catch (Exception e){
                                log.error("通联代付给下游付款异常 "+e.getMessage(),e);
                            }*/

                            //更新订单状态
                            List<com.cjyc.common.model.entity.Order> list = orderDao.findListByCarNos(orderCarNosList);
                            com.cjyc.common.model.entity.Order order = list.get(0);

                            int num = tradeBillDao.countUnFinishByOrderNo(order.getNo());
                            if (num == 0) {
                                orderDao.updateForReceipt(order.getId(), System.currentTimeMillis());
                                tradeBillDao.updateOrderPayState(order.getNo(), System.currentTimeMillis());
                            }
                        }
                    }
                    try {
                        sendMessage(orderCarNosList, userInfo);
                    } catch (Exception e) {
                        log.error("回调短信发送异常" + e.getMessage(), e);
                    }

                }
            }
        }

        return BaseResultUtil.success();
    }

    private void sendMessage(List<String> orderCarNosList, UserInfo userInfo) {
        StringBuilder message = new StringBuilder("【韵车物流】VIN码后六位为");
        List<OrderCar> orderCarList = orderCarDao.findListByNos(orderCarNosList);

        try {
            for (int i = 0; i < orderCarList.size(); i++) {
                OrderCar orderCar = orderCarList.get(i);
                if (orderCar != null) {
                    String vin = orderCar.getVin();
                    if (vin != null) {
                        int length = vin.length();
                        if (length > 6) {
                            if (i == orderCarList.size() - 1) {
                                message.append(vin.substring(length - 6));
                            } else {
                                message.append(vin.substring(length - 6));
                                message.append("、");
                            }
                        } else {
                            if (i == orderCarList.size() - 1) {
                                message.append(vin);
                            } else {
                                message.append(vin);
                                message.append("、");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("拼接Vin码异常" + e.getMessage(), e);
        }

        message.append("的车辆已完成交车收款，收款金额");
        List<com.cjyc.common.model.entity.Order> list = orderDao.findListByCarNos(orderCarNosList);
        com.cjyc.common.model.entity.Order order = list.get(0);

        BigDecimal freightFee = new BigDecimal("0");
        if (order != null && order.getCustomerType() == 3) {//合伙人
            freightFee = tradeBillDao.getAmountByOrderCarNosToPartner(orderCarNosList);
        } else {//非合伙人
            freightFee = tradeBillDao.getAmountByOrderCarNos(orderCarNosList);
        }

        if (freightFee != null) {
            message.append(freightFee.divide(new BigDecimal(100)));
        }
        message.append("元");
        if (userInfo != null && userInfo.getPhone() != null) {
            try {
                MiaoxinSmsUtil.send(userInfo.getPhone(), message.toString());
            } catch (Exception e) {
                log.error("回调短信发送失败" + e.getMessage(), e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransfer(Transfer transfer, Event event, String state) {
        //更新交易流水表状态
        TradeBill tradeBill = new TradeBill();
        tradeBill.setPingPayId(transfer.getId());
        tradeBill.setState(2);
        tradeBill.setTradeTime(System.currentTimeMillis());
        tradeBillDao.updateTradeBillByPingPayId(tradeBill);

        //更新运单支付状态
        Map<String, Object> metadata = transfer.getMetadata();
        PingxxMetaData pingxxMetaData = BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
        String chargeType = pingxxMetaData.getChargeType();
        if (chargeType.equals(String.valueOf(ChargeTypeEnum.UNION_PAY.getCode()))) {

            log.info("回调给承运商付款");
            //给承运商付款
            Long waybillId = Long.valueOf(pingxxMetaData.getWaybillId());

            String no = tradeBillDao.getTradeBillByPingPayId(transfer.getId());
            try {
                tradeBillDao.updateWayBillPayState(waybillId, no, System.currentTimeMillis(), "1");
            } catch (Exception e) {
                log.error("回调给承运商付款更新运单支付状态失败" + e.getMessage(), e);
            }
            //给司机发送短信提醒到账
            try{
                Waybill waybill = waybillDao.findVoById(waybillId);
                if (waybill != null) {
                    StringBuilder message = new StringBuilder("【韵车物流】运单");
                    message.append(waybill.getNo());
                    message.append("已完成，并向您支付本单运费");
                    message.append(MoneyUtil.fenToYuan(waybill.getFreightFee(),MoneyUtil.PATTERN_TWO));
                    message.append("元。请查看绑定的银行账户（具体到账时间以银行信息为准）");
                    log.info("给司机发送短信提醒到账 message={}",message.toString());
                    BaseCarrierVo carrier = carrierDao.showCarrierById(waybill.getCarrierId());
                    if(carrier!=null&&carrier.getLinkmanPhone()!=null){
                        try{
                            MiaoxinSmsUtil.send(carrier.getLinkmanPhone(), message.toString());
                        }catch (Exception e){
                            log.error("给司机发送短信提醒失败" + e.getMessage(), e);
                        }

                    }
                }
            }catch (Exception e){
                log.error("给司机发送短信提醒异常" + e.getMessage(), e);
            }


        }
        if (chargeType.equals(String.valueOf(ChargeTypeEnum.UNION_PAY_PARTNER.getCode()))) {
            String orderId = pingxxMetaData.getOrderId();
            com.cjyc.common.model.entity.Order order = orderDao.selectById(orderId);

            //更新合伙人打款状态
            String orderNo = String.valueOf(pingxxMetaData.getOrderNo());
            tradeBillDao.updateOrderFlag(orderNo, "2", System.currentTimeMillis());

        }

    }

    @Override
    public void transferFailed(Transfer transfer, Event event) {
        //更新交易流水表状态
        TradeBill tradeBill = new TradeBill();
        tradeBill.setPingPayId(transfer.getId());
        tradeBill.setState(-2);
        tradeBill.setTradeTime(System.currentTimeMillis());
        tradeBillDao.updateTradeBillByPingPayId(tradeBill);

        //更新运单支付状态
        Map<String, Object> metadata = transfer.getMetadata();
        PingxxMetaData pingxxMetaData = BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
        String chargeType = pingxxMetaData.getChargeType();
        if (chargeType.equals(String.valueOf(ChargeTypeEnum.UNION_PAY.getCode()))) {

            log.info("回调给承运商付款失败");
            //给承运商付款
            Long waybillId = Long.valueOf(pingxxMetaData.getWaybillId());

            String no = tradeBillDao.getTradeBillByPingPayId(transfer.getId());
            try {
                tradeBillDao.updateWayBillPayState(waybillId, no, System.currentTimeMillis(), "-2");
            } catch (Exception e) {
                log.error("回调给承运商付款更新运单支付状态失败" + e.getMessage(), e);
            }

        }
        if (chargeType.equals(String.valueOf(ChargeTypeEnum.UNION_PAY_PARTNER.getCode()))) {
            log.info("回调给合伙人付款失败");
            String orderId = pingxxMetaData.getOrderId();
            com.cjyc.common.model.entity.Order order = orderDao.selectById(orderId);

            //更新合伙人打款状态
            String orderNo = String.valueOf(pingxxMetaData.getOrderNo());
            tradeBillDao.updateOrderFlag(orderNo, "-2", System.currentTimeMillis());

        }
    }

    @Override
    public void refund(Refund refund, Event event) {
        /*TradeBill tradeBill = new TradeBill();
        tradeBill.setPingPayId(refund.getId());
        tradeBill.setState(10);
        tradeBill.setTradeTime(System.currentTimeMillis());
        tradeBillDao.updateTradeBillByPingPayId(tradeBill);*/
        Map<String, Object> metadata = refund.getMetadata();
        PingxxMetaData pingxxMetaData = BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
        String orderNo = pingxxMetaData.getOrderNo();
        log.info("refund orderNo = {}", orderNo);
        try {
            orderRefundDao.updateRefund(orderNo);
        } catch (Exception e) {
            log.error("回调退款更新异常 orderNo = {}", orderNo);
            log.error(e.getMessage(), e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTransactions(Object obj, String state) {
        TradeBill tb;
        int id = 0;
        if (obj instanceof Order) {
            Order order = (Order) obj;

            tb = orderToTransactions(order, null, "0");
            tradeBillDao.insert(tb);
            Map<String, Object> metadata = ((Order) obj).getMetadata();
            Object orderNo = metadata.get("orderNo");

            TradeBillDetail tradeBillDetail = new TradeBillDetail();
            tradeBillDetail.setTradeBillId(tb.getId());
            tradeBillDetail.setSourceNo(orderNo == null ? null : String.valueOf(orderNo));
            tradeBillDetailDao.insert(tradeBillDetail);
        } else if (obj instanceof Charge) {
            tb = chargeToTransactions((Charge) obj, null, state);
            if (tb != null) {
                id = tradeBillDao.insert(tb);
            }

            Map<String, Object> metadata = ((Order) obj).getMetadata();
            Object orderCarIds = metadata.get("orderCarIds");
            if (orderCarIds != null) {
                String[] ids = ((String) orderCarIds).split(",");
                for (int i = 0; i < ids.length; i++) {
                    TradeBillDetail tradeBillDetail = new TradeBillDetail();
                    tradeBillDetail.setTradeBillId(Long.valueOf(id));
                    tradeBillDetail.setSourceNo(ids[i] == null ? null : ids[i]);
                    tradeBillDetailDao.insert(tradeBillDetail);
                }
            }
        } else {
            tb = (TradeBill) obj;
        }

    }

    private TradeBill orderToTradeBill(Order order, Event event, int state) {
        Map<String, Object> metadata = order.getMetadata();
        MetaDataEntiy mde = BeanMapUtil.mapToBean(metadata, new MetaDataEntiy());
        TradeBill tb = new TradeBill();
        tb.setNo(order.getMerchantOrderNo());
        tb.setPingPayId(order.getId());
        tb.setLivemode(order.getLivemode() ? LiveModeEnum.LIVE.getTag() : LiveModeEnum.TEST.getTag());
        tb.setSubject(order.getSubject());
        tb.setBody(order.getBody());
        tb.setAmount(order.getAmount() == null ? BigDecimal.ZERO : BigDecimal.valueOf(order.getAmount()));
        tb.setPayerId(Long.valueOf(order.getUid()));
        tb.setPayerName(mde.getLoginName());
        tb.setState(state);
        tb.setCreateTime(System.currentTimeMillis());
        tb.setType(Integer.valueOf(mde.getChargeType()));
        //tb.setSourceMainNo()
        //支付渠道
        ChargeCollection charges = order.getCharges();
        if (charges != null) {
            List<Charge> data = charges.getData();
            Charge charge = data.get(0);
            tb.setChannel(charge.getChannel());
            //支付渠道名
            ChannelEnum channelEnum = ChannelEnum.valueOfTag(charge.getChannel());
            if (channelEnum != null) {
                tb.setChannelName(channelEnum.getName());
            }
            //支付渠道费
            //BigDecimal channelFee = dictionaryService.getChannelFee(charge.getChannel(), order.getAmount().toString());
            tb.setChannelFee(new BigDecimal(0));
        }
        tb.setReceiverId(0L);
        if (event != null) {
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }

        return tb;
    }

    private TradeBill orderToTransactions(Order order, Event event, String state) {
        TradeBill tb = new TradeBill();
        tb.setPingPayId(order.getId());
        tb.setSubject(order.getSubject());
        tb.setBody(order.getBody());
        tb.setAmount(order.getAmount() == null ? BigDecimal.valueOf(0) : BigDecimal.valueOf(order.getAmount()));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setType(ChargeTypeEnum.PREPAY.getCode());
        tb.setLivemode(order.getLivemode() ? LiveModeEnum.LIVE.getTag() : LiveModeEnum.TEST.getTag());

        ChargeCollection charges = order.getCharges();
        if (charges != null) {
            List<Charge> data = charges.getData();
            Charge charge = data.get(0);
            tb.setChannel(charge.getChannel());
            //支付渠道名
            ChannelEnum channelEnum = ChannelEnum.valueOfTag(charge.getChannel());
            if (channelEnum != null) {
                tb.setChannelName(channelEnum.getName());
            }
            //TODO
            //添加通道费率@JPG
            //BigDecimal channelFee = dictionaryService.getChannelFee(charge.getChannel(), order.getAmount().toString());
            tb.setChannelFee(new BigDecimal(0));
        }
        String uid = order.getUid();
        tb.setPayerId(Long.valueOf(uid));

        tb.setReceiverId(0L);
        if (event != null) {
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));

        return tb;
    }

    private TradeBill chargeToTransactions(Charge charge, Event event, String status) {
        TradeBill tb = new TradeBill();
        tb.setSubject(charge.getSubject());
        tb.setBody(charge.getBody());
        /*Map<String, Object> metadata = charge.getMetadata();
        Object orderNo = metadata.get("orderNo");
        tb.setPingPayNo((String)orderNo);*/
        tb.setAmount(charge.getAmount() == null ? BigDecimal.valueOf(0) : BigDecimal.valueOf(charge.getAmount()).multiply(new BigDecimal(100)));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setChannel(charge.getChannel());
        tb.setChannelFee(new BigDecimal(0));
        tb.setReceiverId(0L);
        if (event != null) {
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }
        tb.setType(0);
        tb.setState(0);//待支付/已支付/付款失败
        tb.setPingPayId(charge.getId());
        return tb;
    }

    @Override
    public void cancelOrderRefund(String orderCode) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransactions(Order order, Event event, String state) {
        /**
         * 更新f_trade_bill状态，更新车辆付款状态,物流费支付时间,更新订单状态
         */
        TradeBill tradeBill = new TradeBill();
        tradeBill.setPingPayId(order.getId());
        tradeBill.setState(2);
        tradeBill.setTradeTime(System.currentTimeMillis());
        tradeBillDao.updateTradeBillByPingPayId(tradeBill);
        Map<String, Object> metadata = order.getMetadata();
        MetaDataEntiy mde = BeanMapUtil.mapToBean(metadata, new MetaDataEntiy());
        int chargeType = Integer.valueOf(mde.getChargeType());
        if (chargeType == ChargeTypeEnum.COLLECT_PAY.getCode() || chargeType == ChargeTypeEnum.COLLECT_QRCODE.getCode()) {
            //物流费到付
            String no = order.getMerchantOrderNo();
            log.info(chargeType + " 物流费到付 no =" + no);
            //操作人信息
            UserInfo userInfo = userService.getUserInfo(Long.valueOf(mde.getLoginId()), Integer.valueOf(mde.getLoginType()));
            tradeBillDao.updateForPaySuccess(no, order.getTimePaid() * 1000);
            //更新车辆支付状态
            List<String> orderCarNoList = Arrays.asList(mde.getSourceNos().split(","));
            for (int i = 0; i < orderCarNoList.size(); i++) {
                String orderCarNo = orderCarNoList.get(i);
                if (orderCarNo != null) {
                    tradeBillDao.updateOrderCar(orderCarNo, 2, System.currentTimeMillis());
                }
            }
            //处理运单订单任务数据
            csTaskService.updateForCarFinish(Arrays.asList(mde.getSourceNos().split(",")), userInfo);

        } else if (chargeType == ChargeTypeEnum.PREPAY.getCode() || chargeType == ChargeTypeEnum.PREPAY_QRCODE.getCode()) {
            log.info("物流费预付回调，order = {}", order.toString());

            PingxxMetaData pingxxMetaData = BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
            //物流费预付
            String orderNo = pingxxMetaData.getOrderNo();
            log.info(chargeType + " 物流费预付 orderNo =" + orderNo);
            updateForPrePay(pingxxMetaData);

            String lockKey = getRandomNoKey(orderNo);
            redisUtil.delete(lockKey);
            //验证订单金额是否一致
            checkOrderFee(orderNo, order.getAmount());

        }

    }

    private void checkOrderFee(String orderNo, Integer payFee) {
        BigDecimal amount = new BigDecimal(payFee.toString());
        try {
            //校验订单金额是否有改变
            if (orderNo != null) {
                com.cjyc.common.model.entity.Order ord = orderDao.findByNo(orderNo);
                if (ord != null) {
                    BigDecimal totalFee = ord.getTotalFee();

                    if (MoneyUtil.nullToZero(totalFee).compareTo(amount) != 0) {//订单金额不一致，退款
                        log.warn("物流费预付,订单金额不一致orderNo = {},订单金额为{}，付款金额为{} ", orderNo, totalFee, amount);
                        Long cui = ord.getCheckUserId();
                        if (cui != null) {
                            BigDecimal difference = MoneyUtil.nullToZero(totalFee).subtract(amount);
                            Admin admin = csAdminService.getById(cui, true);
                            csSmsService.send(admin.getPhone(), "订单编号{0}订单金额与付款金额不一致,差额为多{1}", orderNo, difference);
                        }

                    }
                } else {
                    log.error("物流费预付回调查询订单不存在");
                }

            } else {
                log.error("物流费预付回调订单编号为空");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateForPrePay(PingxxMetaData pingxxMetaData) {
        //操作人信息
        UserInfo userInfo = userService.getUserInfo(Long.valueOf(pingxxMetaData.getLoginId()), Integer.valueOf(pingxxMetaData.getLoginType()));
        String orderNo = pingxxMetaData.getOrderNo();
        if (orderNo != null) {
            com.cjyc.common.model.entity.Order order = orderDao.findByNo(orderNo);
            tradeBillDao.updateOrderState(orderNo, 2, System.currentTimeMillis());
            List<String> list = tradeBillDao.getOrderCarNoList(orderNo);
            List<OrderCar> orderCarList = orderCarDao.findListByNos(list);
            if (list != null) {
                for (int i = 0; i < orderCarList.size(); i++) {
                    OrderCar orderCar = orderCarList.get(i);
                    if (orderCar != null) {
                        tradeBillDao.updateOrderCar(orderCar.getNo(), 2, System.currentTimeMillis());
                    }
                }
            }

            //添加日志
            csOrderLogService.asyncSave(order, OrderLogEnum.PREPAID,
                    new String[]{OrderLogEnum.PREPAID.getOutterLog(),
                            MessageFormat.format(OrderLogEnum.PREPAID.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                    userInfo);

            csPushMsgService.send(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_PAID_ORDER, order.getNo());
            csAmqpService.sendOrderState(order);
        }
    }

    private String getRandomNoKey(String prefix) {
        return "cjyc:random:no:prepay:" + prefix;
    }

    @Override
    public TradeBill getTradeBillByOrderNo(String orderNo) {
        return tradeBillDao.getTradeBillByOrderNo(orderNo);
    }

    @Override
    public BigDecimal getAmountByOrderNo(String orderNo) {
        return tradeBillDao.getAmountByOrderNo(orderNo);
    }

    @Override
    public void cancelExpireTrade() {
        List<TradeBill> list = tradeBillDao.getAllExpireTradeBill();

        if (list != null && list.size() > 0) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < list.size(); i++) {
                        TradeBill tb = list.get(i);
                        if (tb != null) {
                            TradeBill tradeBill = new TradeBill();
                            tradeBill.setPingPayId(tb.getPingPayId());
                            tradeBill.setState(-1);
                            tradeBill.setTradeTime(tb.getTradeTime());
                            tradeBillDao.updateTradeBillByPingPayId(tradeBill);
                        }

                    }
                }
            });
        }
    }
}
