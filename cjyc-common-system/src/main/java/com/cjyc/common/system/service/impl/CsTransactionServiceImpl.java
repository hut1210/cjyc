package com.cjyc.common.system.service.impl;

import com.Pingxx.model.MetaDataEntiy;
import com.Pingxx.model.Order;
import com.Pingxx.model.PingxxMetaData;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.ITradeBillDao;
import com.cjyc.common.model.dao.ITradeBillDetailDao;
import com.cjyc.common.model.entity.TradeBill;
import com.cjyc.common.model.entity.TradeBillDetail;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.ChargeTypeEnum;
import com.cjyc.common.model.enums.PayStateEnum;
import com.cjyc.common.model.enums.Pingxx.ChannelEnum;
import com.cjyc.common.model.enums.Pingxx.LiveModeEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.BeanMapUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.util.RedisUtils;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.representer.BaseRepresenter;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
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
public class CsTransactionServiceImpl implements ICsTransactionService {

    @Resource
    private ITradeBillDao tradeBillDao;

    @Autowired
    private RedisUtils redisUtil;

    @Resource
    private ICsSendNoService csSendNoService;

    @Resource
    private ITradeBillDetailDao tradeBillDetailDao;
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsUserService userService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ICsPingPayService csPingPayService;

    @Resource
    private IOrderDao orderDao;

    @Override
    public int save(Object obj) {
        TradeBill bill;
        if(obj instanceof TradeBill){
            tradeBillDao.insert((TradeBill) obj);
        }else if(obj instanceof Order){
            //根据ping++订单创建账单
            Order order = (Order) obj;
            MetaDataEntiy metaDataEntiy = BeanMapUtil.mapToBean(order.getMetadata(), new MetaDataEntiy());
            int chargeType = Integer.valueOf(metaDataEntiy.getChargeType());
            bill = orderToTradeBill(order, null, PayStateEnum.UNPAID.code);
            tradeBillDao.insert(bill);
            //预付
            if(chargeType == ChargeTypeEnum.PREPAY.getCode() || chargeType == ChargeTypeEnum.PREPAY_QRCODE.getCode()){

            }
            //到付
            if(chargeType == ChargeTypeEnum.COLLECT_PAY.getCode() || chargeType == ChargeTypeEnum.COLLECT_QRCODE.getCode() ){
                List<String> orderNos = Arrays.asList(metaDataEntiy.getSourceNos().split(","));
                tradeBillDetailDao.saveBatch(bill.getId(), orderNos);
            }
        }
        return 0;
    }

    @Override
    public List<String> getOrderCarNosByTaskId(Long taskId) {
        return tradeBillDao.getOrderCarNosByTaskId(taskId);
    }

    @Override
    public List<String> getOrderCarNosByTaskCarIds(List<Long> taskCarIdList) {
        return tradeBillDao.getOrderCarNosByTaskCarIds(taskCarIdList);
    }

    @Override
    public TradeBill getTradeBillByOrderNoAndType(String orderNo, int type) {
        return tradeBillDao.getTradeBillByOrderNoAndType(orderNo,type);
    }

    @Override
    public void updateWayBillPayStateNoPay(Long waybillId, long time) {
        tradeBillDao.updateWayBillPayStateNoPay(waybillId,time);
    }

    @Override
    public BigDecimal getAmountByOrderCarNosToPartner(List<String> orderCarNosList) {
        return tradeBillDao.getAmountByOrderCarNosToPartner(orderCarNosList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCooperatorTransactions(Transfer transfer, String state) {
        TradeBill tb = transferToTransactions(transfer,null,state);
        if(tb != null){
            tradeBillDao.insert(tb);
        }

        Map<String, Object> metadata = transfer.getMetadata();
        String orderNo = (String) metadata.get("orderNo");
        if(orderNo != null){
            TradeBillDetail tradeBillDetail = new TradeBillDetail();
            tradeBillDetail.setTradeBillId(Long.valueOf(tb.getId()));
            tradeBillDetail.setSourceNo(orderNo==null?null:orderNo);
            tradeBillDetailDao.insert(tradeBillDetail);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWebPrePayTransactions(Charge charge, String state) {
        TradeBill tb = chargeToTransactions(charge, null,state);
        if(tb != null){
            tradeBillDao.insert(tb);
        }

        Map<String, Object> metadata = charge.getMetadata();
        String orderNo = (String) metadata.get("orderNo");
        if(orderNo != null){
            TradeBillDetail tradeBillDetail = new TradeBillDetail();
            tradeBillDetail.setTradeBillId(Long.valueOf(tb.getId()));
            tradeBillDetail.setSourceNo(orderNo==null?null:orderNo);
            tradeBillDetailDao.insert(tradeBillDetail);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSalesPrePayTransactions(Charge charge, String state) {
        TradeBill tb = chargeToTransactions(charge, null,state);
        if(tb != null){
            tradeBillDao.insert(tb);
        }

        Map<String, Object> metadata = charge.getMetadata();
        String orderNo = (String) metadata.get("orderNo");
        if(orderNo != null){
            TradeBillDetail tradeBillDetail = new TradeBillDetail();
            tradeBillDetail.setTradeBillId(Long.valueOf(tb.getId()));
            tradeBillDetail.setSourceNo(orderNo==null?null:orderNo);
            tradeBillDetailDao.insert(tradeBillDetail);
        }
    }

    @Override
    public void updateOrderFlag(String orderNo, String state, long payTime) {
        tradeBillDao.updateOrderFlag(orderNo,state,payTime);
    }

    @Override
    public BigDecimal getWlFeeCount(Long carrierId) {
        BigDecimal fee = tradeBillDao.getWlFeeCount(carrierId);
        if(fee==null){
            return new BigDecimal("0");
        }
        return fee;
    }

    @Override
    public BigDecimal getReceiveFeeCount(Long carrierId) {
        BigDecimal fee = tradeBillDao.getReceiveFeeCount(carrierId);
        if(fee==null){
            return new BigDecimal("0");
        }
        return fee;
    }

    /**
     * 给合伙人付款
     */
    @Override
    public void payToCooperator() {
        List<Long> orderIds = tradeBillDao.getNopayOrder();
        if(orderIds!=null){
            log.info("给合伙人付款 orderIds "+orderIds.toString());
        }

        for(int i=0;i<orderIds.size();i++){
            final Long orderId = orderIds.get(i);
            /*executorService.execute(new Runnable() {
                @Override
                public void run() {*/
                    try {
                        csPingPayService.allinpayToCooperator(orderId);
                    } catch (Exception e) {
                        log.error("定时任务付合伙人服务费失败 orderId= {}",orderId);
                        log.error(e.getMessage(),e);
                    }
             /*   }
            });*/
        }
    }

    /**
     * 因未收到回调，未改状态
     */
    @Override
    public void getPayingOrder() {
        //订单为支付中
        List<Long> orderIds = tradeBillDao.getPayingOrder();
        if(orderIds!=null){
            log.info("订单为支付中 orderIds "+orderIds.toString());
        }

        for(int i=0;i<orderIds.size();i++){
            final Long orderId = orderIds.get(i);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //30分钟未支付的账单
                        com.cjyc.common.model.entity.Order order = orderDao.selectById(orderId);
                        if(order!=null){
                            List<TradeBill> tradeBills = tradeBillDao.getTradeBillList(order.getNo(),ChargeTypeEnum.UNION_PAY_PARTNER.getCode());
                            if(tradeBills!=null){
                                if(tradeBills.size()==1){
                                    TradeBill tradeBill = tradeBills.get(0);
                                    if(tradeBill!=null&&tradeBill.getState()==0){
                                        if(tradeBill.getTradeTime()!=null){
                                            Long time = System.currentTimeMillis()-tradeBill.getTradeTime();
                                            if(time>1800000){
                                                updateFailOrder(order.getNo());
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    } catch (Exception e) {
                        log.error("定时更新未支付的账单失败 orderId= {}",orderId);
                        log.error(e.getMessage(),e);
                    }
                }
            });
        }
    }

    /**
     * 更新订单状态为0，账单状态为支付失败
     * @param orderNo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateFailOrder(String orderNo) {

        tradeBillDao.updateOrderFlag(orderNo,"0",0);

        List<TradeBill> tradeBills = tradeBillDao.getTradeBillList(orderNo,ChargeTypeEnum.UNION_PAY_PARTNER.getCode());
        if(tradeBills!=null){
            if(tradeBills.size()==1){
                TradeBill tradeBill = tradeBills.get(0);
                if(tradeBill!=null&& tradeBill.getState()==1){
                    TradeBill tb = new TradeBill();
                    tb.setPingPayId(tradeBill.getPingPayId());
                    tb.setState(-2);
                    tb.setTradeTime(System.currentTimeMillis());
                    tradeBillDao.updateTradeBillByPingPayId(tb);
                }
            }

        }

        return BaseResultUtil.success();
    }

    @Override
    public List<TradeBill> getTradeBillList(String orderNo, int type) {
        return tradeBillDao.getTradeBillList(orderNo,type);
    }

    @Override
    public BigDecimal getCooperatorServiceFeeCount(Long customId) {
        BigDecimal fee = tradeBillDao.getCooperatorServiceFeeCount(customId);
        if(fee==null){
            return new BigDecimal("0");
        }
        return fee;
    }

    @Override
    public BigDecimal getCooperatorServiceFeeCarCount(Long customId) {
        BigDecimal fee = tradeBillDao.getCooperatorServiceFeeCarCount(customId);
        if(fee==null){
            return new BigDecimal("0");
        }
        return fee;
    }

    @Override
    public BigDecimal getCooperatorServiceReceiveFeeCount(Long customId) {
        BigDecimal fee = tradeBillDao.getCooperatorServiceReceiveFeeCount(customId);
        if(fee==null){
            return new BigDecimal("0");
        }
        return fee;
    }

    @Override
    public BigDecimal getCooperatorServiceReceiveCarFeeCount(Long customId) {
        BigDecimal fee = tradeBillDao.getCooperatorServiceReceiveCarFeeCount(customId);
        if(fee==null){
            return new BigDecimal("0");
        }
        return fee;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTransactions(Object obj, String state) {
        TradeBill tb;
        if(obj instanceof Order){
            Order order = (Order) obj;

            tb = orderToTransactions(order, null, "0");
            tradeBillDao.insert(tb);
            Map<String, Object> metadata = ((Order)obj).getMetadata();
            Object orderNo = metadata.get("orderNo");

            TradeBillDetail tradeBillDetail = new TradeBillDetail();
            tradeBillDetail.setTradeBillId(tb.getId());
            tradeBillDetail.setSourceNo(orderNo==null?null:String.valueOf(orderNo));
            tradeBillDetailDao.insert(tradeBillDetail);
        }else if(obj instanceof Charge){
            tb = chargeToTransactions((Charge)obj, null,state);
            if(tb != null){
                tradeBillDao.insert(tb);
            }

            Map<String, Object> metadata = ((Charge)obj).getMetadata();
            List<String> orderCarIds = (List<String>) metadata.get("orderCarIds");
            if(orderCarIds != null){
                for(int i=0;i<orderCarIds.size();i++){
                    TradeBillDetail tradeBillDetail = new TradeBillDetail();
                    tradeBillDetail.setTradeBillId(Long.valueOf(tb.getId()));
                    tradeBillDetail.setSourceNo(orderCarIds.get(i)==null?null:orderCarIds.get(i));
                    tradeBillDetailDao.insert(tradeBillDetail);
                }
            }
        }else if(obj instanceof Transfer){
            tb = transferToTransactions((Transfer)obj,null,state);
            if(tb != null){
                tradeBillDao.insert(tb);
            }

            Map<String, Object> metadata = ((Transfer)obj).getMetadata();
            String waybillId = (String) metadata.get("waybillId");
            if(waybillId != null){
                TradeBillDetail tradeBillDetail = new TradeBillDetail();
                tradeBillDetail.setTradeBillId(Long.valueOf(tb.getId()));
                tradeBillDetail.setSourceNo(waybillId==null?null:waybillId);
                tradeBillDetailDao.insert(tradeBillDetail);
            }
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
        if(charges != null){
            List<Charge> data = charges.getData();
            Charge charge = data.get(0);
            tb.setChannel(charge.getChannel());
            //支付渠道名
            ChannelEnum channelEnum = ChannelEnum.valueOfTag(charge.getChannel());
            if(channelEnum != null){
                tb.setChannelName(channelEnum.getName());
            }
            //支付渠道费
            //BigDecimal channelFee = dictionaryService.getChannelFee(charge.getChannel(), order.getAmount().toString());
            tb.setChannelFee(new BigDecimal(0));
        }
        tb.setReceiverId(0L);
        if(event != null){
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
        tb.setAmount(order.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(order.getAmount()));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setType(1);
        tb.setLivemode(order.getLivemode() ? LiveModeEnum.LIVE.getTag() : LiveModeEnum.TEST.getTag());
        ChargeCollection charges = order.getCharges();
        if(charges != null){
            List<Charge> data = charges.getData();
            Charge charge = data.get(0);
            tb.setChannel(charge.getChannel());
            //支付渠道名
            ChannelEnum channelEnum = ChannelEnum.valueOfTag(charge.getChannel());
            if(channelEnum != null){
                tb.setChannelName(channelEnum.getName());
            }
            //TODO
            //添加通道费率@JPG
            //BigDecimal channelFee = dictionaryService.getChannelFee(charge.getChannel(), order.getAmount().toString());
            tb.setChannelFee(new BigDecimal(0));
        }
        Map<String, Object> metadata = order.getMetadata();
        if(metadata!=null){
            PingxxMetaData pingxxMetaData =  BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
            String chargeType = pingxxMetaData.getChargeType();
            log.info("chargeType ="+chargeType);

            tb.setType((Integer.valueOf(chargeType)));
        }
        String uid = order.getUid();
        tb.setPayerId(Long.valueOf(uid));

        tb.setReceiverId(0L);
        if(event != null){
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));

        return tb;
    }

    private TradeBill chargeToTransactions(Charge charge,Event event,String state) {
        log.info(charge.toString());
        TradeBill tb = new TradeBill();
        tb.setSubject(charge.getSubject());
        tb.setBody(charge.getBody());
        tb.setAmount(charge.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(charge.getAmount()));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setChannel(charge.getChannel());
        //支付渠道名
        ChannelEnum channelEnum = ChannelEnum.valueOfTag(charge.getChannel());
        if(channelEnum != null){
            tb.setChannelName(channelEnum.getName());
        }
        tb.setChannelFee(new BigDecimal(0));
        tb.setReceiverId(0L);
        tb.setLivemode(charge.getLivemode() ? LiveModeEnum.LIVE.getTag() : LiveModeEnum.TEST.getTag());
        if(event != null){
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }
        Map<String, Object> metadata = charge.getMetadata();
        if(metadata!=null){
            PingxxMetaData pingxxMetaData =  BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
            String chargeType = pingxxMetaData.getChargeType();
            log.info("chargeType ="+chargeType);

            tb.setType((Integer.valueOf(chargeType)));
        }
        tb.setPingPayId(charge.getId());
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));
        return tb;
    }

    private TradeBill transferToTransactions(Transfer transfer,Event event,String state) {
        log.info("transferToTransactions transfer = "+transfer.toString());
        TradeBill tb = new TradeBill();
        Map<String, Object> metadata = transfer.getMetadata();

        tb.setAmount(transfer.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(transfer.getAmount()));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setChannel(transfer.getChannel());
        //支付渠道名
        ChannelEnum channelEnum = ChannelEnum.valueOfTag(transfer.getChannel());
        if(channelEnum != null){
            tb.setChannelName(channelEnum.getName());
        }
        tb.setChannelFee(new BigDecimal(0));
        tb.setLivemode(transfer.getLivemode() ? LiveModeEnum.LIVE.getTag() : LiveModeEnum.TEST.getTag());
        if(event != null){
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }

        if(metadata!=null){
            PingxxMetaData pingxxMetaData =  BeanMapUtil.mapToBean(metadata, new PingxxMetaData());
            String chargeType = pingxxMetaData.getChargeType();

            if(transfer.getType()!=null&&"b2c".equals(transfer.getType())){
                if(chargeType.equals(String.valueOf(ChargeTypeEnum.UNION_PAY.getCode()))){
                    tb.setSubject("通联代付承运商运费");
                    tb.setBody("打款运费到承运商银行卡");
                }else{
                    tb.setSubject("通联代付合伙人费用");
                    tb.setBody("打款运费到合伙人银行卡");
                }
            }
            tb.setReceiverId(pingxxMetaData.getLoginId()!=null?Long.valueOf(pingxxMetaData.getLoginId()):0L);
            tb.setType((Integer.valueOf(chargeType)));
        }
        tb.setPingPayId(transfer.getId());
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));
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
        if(chargeType == ChargeTypeEnum.COLLECT_PAY.getCode() || chargeType == ChargeTypeEnum.COLLECT_QRCODE.getCode()){
            //物流费到付
            String no = order.getMerchantOrderNo();
            //操作人信息
            UserInfo userInfo = userService.getUserInfo(Long.valueOf(mde.getLoginId()), Integer.valueOf(mde.getLoginType()));
            tradeBillDao.updateForPaySuccess(no, order.getTimePaid() * 1000);
            //处理运单订单任务数据
            csTaskService.updateForCarFinish(Arrays.asList(mde.getSourceNos().split(",")), userInfo);
        }else if(chargeType == ChargeTypeEnum.PREPAY.getCode() || chargeType == ChargeTypeEnum.PREPAY_QRCODE.getCode()){
            //物流费预付
            String orderNo = (String) metadata.get("orderNo");

            if(orderNo!=null){
                tradeBillDao.updateOrderState(orderNo,2,System.currentTimeMillis());
                List<String> list = tradeBillDao.getOrderCarNoList(orderNo);
                if(list != null){
                    for(int i=0;i<list.size();i++){
                        String orderCarNo = list.get(i);
                        if(orderCarNo != null){
                            tradeBillDao.updateOrderCar(orderCarNo,1,System.currentTimeMillis());
                        }

                    }
                }
            }
            String lockKey =getRandomNoKey(orderNo);
            redisUtil.delete(lockKey);
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
    public BigDecimal getAmountByOrderCarNos(List<String> orderCarNos) {

        return tradeBillDao.getAmountByOrderCarNos(orderCarNos);
    }


    /**
     *扫码付款逻辑操作
     */
    public void update(){

    }


}
