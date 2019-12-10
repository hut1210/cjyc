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

    void updateOrderCar(@Param("orderCarNos") String orderCarNo,@Param("wlPayState") int wlPayState,@Param("wlPayTime") long wlPayTime);

    TradeBill getTradeBillByOrderNo(String orderNo);

    BigDecimal getAmountByOrderNo(String orderNo);

    void updateOrderState(@Param("orderNo")String orderNo, @Param("wlPayState") int wlPayState,@Param("wlPayTime") long wlPayTime);

    List<TradeBill> getAllExpireTradeBill();

    BigDecimal getAmountByOrderCarIds(@Param("orderCarIds")String orderCarIds);

    int updateForPaySuccess(@Param("no") String no, @Param("tradeTime") Long tradeTime);

    TradeBillVo findVoByNo(String no);
}
