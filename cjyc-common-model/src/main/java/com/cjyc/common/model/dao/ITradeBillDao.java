package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.TradeBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.customer.bill.TradeBillVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * ping++交易流水表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ITradeBillDao extends BaseMapper<TradeBill> {

    void updateTradeBillByPingPayId(TradeBill tradeBill);

    List<String> getOrderCarNoList(String orderNo);

    void updateOrderCar(@Param("orderCarNo") String orderCarNo,@Param("wlPayState") int wlPayState,@Param("wlPayTime") long wlPayTime);

    TradeBill getTradeBillByOrderNo(String orderNo);

    BigDecimal getAmountByOrderNo(String orderNo);

    void updateOrderState(@Param("orderNo")String orderNo, @Param("wlPayState") int wlPayState,@Param("wlPayTime") long wlPayTime);

    List<TradeBill> getAllExpireTradeBill();

    int updateForPaySuccess(@Param("no") String no, @Param("tradeTime") Long tradeTime);

    TradeBillVo findVoByNo(String no);

    List<String> getOrderCarNosByTaskId(Long taskId);

    BigDecimal getAmountByOrderCarNos(@Param("orderCarNos")List<String> orderCarNos);

    List<String> getOrderCarNosByTaskCarIds(@Param("taskCarIdList")List<Long> taskCarIdList);

    void updateForReceipt(@Param("waybillId")Long waybillId, @Param("currentTimeMillis")long currentTimeMillis);

    int countUnFinishByOrderNo(String no);

    TradeBill getTradeBillByOrderNoAndType(@Param("orderNo")String orderNo,@Param("type") int type);

    void updateWayBillPayState(@Param("waybillId") Long waybillId, @Param("no") String no,@Param("time") long time);

    String getTradeBillByPingPayId(String pingPayId);

    void updateWayBillPayStateNoPay(@Param("waybillId")Long waybillId,@Param("time") long time);

    BigDecimal getAmountByOrderCarNosToPartner(@Param("orderCarNos")List<String> orderCarNos);

    void updateOrderPayState(@Param("orderNo")String no,@Param("wlPayTime") long currentTimeMillis);
}
