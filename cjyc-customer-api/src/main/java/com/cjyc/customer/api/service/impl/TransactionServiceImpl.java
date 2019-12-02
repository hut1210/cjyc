package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ITradeBillDao;
import com.cjyc.common.model.entity.TradeBill;
import com.cjyc.customer.api.service.ITransactionService;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @Author:Hut
 * @Date:2019/11/20 16:40
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class TransactionServiceImpl implements ITransactionService {

    @Resource
    private ITradeBillDao iTradeBillDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTransactions(Object obj, String state) {
        TradeBill tb = objToTransactions(obj, null, state);;
        if(tb != null){
            iTradeBillDao.insert(tb);
        }
    }

    public TradeBill objToTransactions(Object obj,Event event,String status){
        TradeBill  tb= null;
        if(obj instanceof Order){
            tb = orderToTransactions((Order)obj, event,status);
        }/*else if(obj instanceof Recharge){
            tb = rechargeToTransactions((Recharge)obj, event,status);
        }else if(obj instanceof Withdrawal){
            tb = withdrawalToTransactions((Withdrawal)obj, event,status);
        }else if(obj instanceof Transfer){
            tb = transferToTransactions((Transfer)obj, event,status);
        }else if(obj instanceof Charge){
            tb = chargeToTransactions((Charge)obj, event,status);
        }*/else{
            tb = (TradeBill) obj;
        }

        return tb;
    }

    private TradeBill orderToTransactions(Order order, Event event, String state) {
        TradeBill tb = new TradeBill();
        tb.setSubject(order.getSubject());
        tb.setBody(order.getBody());
        tb.setPingPayNo(order.getMerchantOrderNo());
        tb.setAmount(order.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(order.getAmount()));
        tb.setCreateTime(order.getCreated());
        tb.setType(1);
        if(order.getLivemode()){
            tb.setLivemode("live");
        }else{
            tb.setLivemode("test");
        }
        ChargeCollection charges = order.getCharges();
        if(charges != null){
            List<Charge> data = charges.getData();
            Charge charge = data.get(0);
            tb.setChannel(charge.getChannel());
            //TODO
            //添加通道费率@JPG
            //BigDecimal channelFee = dictionaryService.getChannelFee(charge.getChannel(), order.getAmount().toString());
            tb.setChannelFee(new BigDecimal(0));
        }
        String uid = order.getUid();
        if(uid!=null){
            if(uid.startsWith("bond") || uid.startsWith("freight")){
                uid = uid.substring(uid.indexOf("_")+1);
                tb.setPayerId(Long.valueOf(uid));
            }
        }

        tb.setReceiverId(0L);
        if(event != null){
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }
        Map<String, Object> map = order.getMetadata();
        String orderCode = (String)map.get("code");
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setPingPayId(orderCode);

        return tb;
    }

    @Override
    public void cancelOrderRefund(String orderCode) {

    }
}
