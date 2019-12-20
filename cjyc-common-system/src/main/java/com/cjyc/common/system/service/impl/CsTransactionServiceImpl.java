package com.cjyc.common.system.service.impl;

import com.Pingxx.model.MetaDataEntiy;
import com.Pingxx.model.Order;
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
import com.cjyc.common.model.util.BeanMapUtil;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.common.system.service.ICsTransactionService;
import com.cjyc.common.system.service.ICsUserService;
import com.cjyc.common.system.util.RedisUtils;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Transactional(rollbackFor = Exception.class)
    public void saveTransactions(Object obj, String state) {
        TradeBill tb;
        int id = 0;
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
                id = tradeBillDao.insert(tb);
            }

            Map<String, Object> metadata = ((Charge)obj).getMetadata();
            List<String> orderCarIds = (List<String>) metadata.get("orderCarIds");
            if(orderCarIds != null){
                for(int i=0;i<orderCarIds.size();i++){
                    TradeBillDetail tradeBillDetail = new TradeBillDetail();
                    tradeBillDetail.setTradeBillId(Long.valueOf(id));
                    tradeBillDetail.setSourceNo(orderCarIds.get(i)==null?null:orderCarIds.get(i));
                    tradeBillDetailDao.insert(tradeBillDetail);
                }
            }
        }else{
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
        tb.setType(0);
        tb.setPingPayId(charge.getId());
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));
        return tb;
    }

    @Override
    public void cancelOrderRefund(String orderCode) {

    }

    @Override
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
