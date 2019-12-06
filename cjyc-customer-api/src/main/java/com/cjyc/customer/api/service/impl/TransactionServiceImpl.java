package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ITradeBillDao;
import com.cjyc.common.model.dao.ITradeBillDetailDao;
import com.cjyc.common.model.entity.TradeBill;
import com.cjyc.common.model.entity.TradeBillDetail;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.customer.api.service.ITransactionService;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTransactions(Object obj, String state) {
        TradeBill tb = objToTransactions(obj, null, state);
        int id = 0;
        if(tb != null){
            id = tradeBillDao.insert(tb);
        }
        Map<String, Object> metadata = ((Order)obj).getMetadata();
        Object orderNo = metadata.get("orderNo");

        TradeBillDetail tradeBillDetail = new TradeBillDetail();
        tradeBillDetail.setTradeBillId(Long.valueOf(id));
        tradeBillDetail.setSourceNo(orderNo==null?null:String.valueOf(orderNo));
        tradeBillDetailDao.insert(tradeBillDetail);
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
        }*/else if(obj instanceof Charge){
            tb = chargeToTransactions((Charge)obj, event,status);
        }else{
            tb = (TradeBill) obj;
        }

        return tb;
    }

    private TradeBill orderToTransactions(Order order, Event event, String state) {
        TradeBill tb = new TradeBill();
        tb.setPingPayId(order.getId());
        tb.setSubject(order.getSubject());
        tb.setBody(order.getBody());
        tb.setPingPayNo(order.getMerchantOrderNo());
        tb.setAmount(order.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(order.getAmount()));
        tb.setCreateTime(System.currentTimeMillis());
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
        /*Map<String, Object> map = order.getMetadata();
        String orderNo = (String)map.get("orderNo");*/
        tb.setState(Integer.valueOf(state));//待支付/已支付/付款失败
        tb.setNo(csSendNoService.getNo(SendNoTypeEnum.RECEIPT));

        return tb;
    }

    private TradeBill chargeToTransactions(Charge order,Event event,String status) {
        TradeBill tb = new TradeBill();
        tb.setSubject(order.getSubject());
        tb.setBody(order.getBody());
        Map<String, Object> metadata = order.getMetadata();
        Object orderNo = metadata.get("orderNo");
        tb.setPingPayNo((String)orderNo);
        tb.setAmount(order.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(order.getAmount()).multiply(new BigDecimal(100)));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setChannel(order.getChannel());
        tb.setChannelFee(new BigDecimal(0));
        tb.setReceiverId(0L);
        if(event != null){
            tb.setEventId(event.getId());
            tb.setEventType(event.getType());
        }
        tb.setType(0);
        tb.setState(0);//待支付/已支付/付款失败
        tb.setPingPayId(order.getId());
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

        if(list != null && list.size()>0){
            executorService.execute(new Runnable() {
                public void run() {
                    for(int i=0;i<list.size();i++) {
                        TradeBill tb = list.get(i);
                        if(tb != null){
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

    /**
     *扫码付款逻辑操作
     */
    public void update(){

    }


}
