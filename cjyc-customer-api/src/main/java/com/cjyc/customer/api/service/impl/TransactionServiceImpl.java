package com.cjyc.customer.api.service.impl;

import com.Pingxx.model.MetaDataEntiy;
import com.pingplusplus.model.Order;
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
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.common.system.service.ICsUserService;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.customer.api.service.ITransactionService;
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
    public ResultVo update(Charge charge, Event event, String state) {

        Map<String, Object> metadata = charge.getMetadata();
        log.debug("update metadata = "+metadata.toString()+" taskId = "+metadata.get("taskId"));

        Long taskId = Long.valueOf((String)metadata.get("taskId"));

        List<String> taskCarIdList = (List<String>)metadata.get("taskCarIdList");

        List<String> orderCarNosList = (List<String>)metadata.get("orderCarIds");

        String chargeType = (String)metadata.get("chargeType");
        log.info("update chargeType="+chargeType);

        if(chargeType!=null){
            if(chargeType.equals("5")){
                log.info("后台预付码回调");
            }
            if(chargeType.equals("2")||chargeType.equals("6")){
                log.info("司机出示二维码回调或者后台出库回调");
                Task task = null;
                if(taskId == null){
                    log.error("回调中参数taskId不存在");
                    return BaseResultUtil.fail("缺少参数taskId");
                }else{
                    task = taskDao.selectById(taskId);
                    if (task == null) {
                        log.error(taskId+" 任务不存在");
                        return BaseResultUtil.fail(taskId+"任务不存在");
                    }else{
                        //验证运单
                        Waybill waybill = waybillDao.selectById(task.getWaybillId());
                        if (waybill == null) {
                            log.error("运单不存在");
                            return BaseResultUtil.fail("运单不存在");
                        }
                        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
                            log.error(waybill.getNo()+"运单已完结");
                            return BaseResultUtil.fail(waybill.getNo()+"运单已完结");
                        }
                    }
                }

                if(orderCarNosList==null){
                    log.error("回调中参数orderCarNosList不存在");
                    return BaseResultUtil.fail("缺少参数orderCarNosList");
                }else{
                    //修改流水支付状态
                    TradeBill tradeBill = new TradeBill();
                    tradeBill.setPingPayId(charge.getId());
                    tradeBill.setState(2);
                    tradeBill.setTradeTime(System.currentTimeMillis());
                    tradeBillDao.updateTradeBillByPingPayId(tradeBill);
                    //修改车辆支付状态
                    for(int i=0;i<orderCarNosList.size();i++){
                        tradeBillDao.updateOrderCar(orderCarNosList.get(i),1,System.currentTimeMillis());
                    }

                    for (int i=0;i<taskCarIdList.size();i++){
                        TaskCar taskCar = taskCarDao.selectById(Long.valueOf(taskCarIdList.get(i)));
                        if(taskCar != null){
                            waybillCarDao.updateForReceipt(taskCar.getWaybillCarId());
                        }
                    }
                }
                //验证任务是否完成
                int row = taskCarDao.countUnFinishByTaskId(taskId);
                if (row == 0) {
                    //更新任务状态
                    taskDao.updateStateById(task.getId(), TaskStateEnum.FINISHED.code);
                    //验证运单是否完成
                    int n = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
                    if (n == 0) {
                        //更新运单状态
                        tradeBillDao.updateForReceipt(task.getWaybillId(), System.currentTimeMillis());

                        //TODO
                        //更新订单状态
                        List<com.cjyc.common.model.entity.Order> list = orderDao.findListByCarNos(orderCarNosList);
                        com.cjyc.common.model.entity.Order order = list.get(0);

                        int num = tradeBillDao.countUnFinishByOrderNo(order.getNo());
                        if(num == 0){
                            orderDao.updateForReceipt(order.getId(),System.currentTimeMillis());
                        }
                    }
                }
            }

        }

        return BaseResultUtil.success();
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

            Map<String, Object> metadata = ((Order)obj).getMetadata();
            Object orderCarIds = metadata.get("orderCarIds");
            if(orderCarIds != null){
                String[] ids =((String)orderCarIds).split(",");
                for(int i=0;i<ids.length;i++){
                    TradeBillDetail tradeBillDetail = new TradeBillDetail();
                    tradeBillDetail.setTradeBillId(Long.valueOf(id));
                    tradeBillDetail.setSourceNo(ids[i]==null?null:ids[i]);
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

    private TradeBill chargeToTransactions(Charge charge,Event event,String status) {
        TradeBill tb = new TradeBill();
        tb.setSubject(charge.getSubject());
        tb.setBody(charge.getBody());
        /*Map<String, Object> metadata = charge.getMetadata();
        Object orderNo = metadata.get("orderNo");
        tb.setPingPayNo((String)orderNo);*/
        tb.setAmount(charge.getAmount()==null?BigDecimal.valueOf(0):BigDecimal.valueOf(charge.getAmount()).multiply(new BigDecimal(100)));
        tb.setCreateTime(System.currentTimeMillis());
        tb.setChannel(charge.getChannel());
        tb.setChannelFee(new BigDecimal(0));
        tb.setReceiverId(0L);
        if(event != null){
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
            log.info(chargeType+" 物流费预付 orderNo ="+orderNo);
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
    public void cancelExpireTrade() {
        List<TradeBill> list = tradeBillDao.getAllExpireTradeBill();

        if(list != null && list.size()>0){
            executorService.execute(new Runnable() {
                @Override
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
}
