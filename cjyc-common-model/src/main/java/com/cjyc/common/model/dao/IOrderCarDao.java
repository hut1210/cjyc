package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.web.order.ChangePriceOrderCarDto;
import com.cjyc.common.model.dto.web.order.LineWaitDispatchCountListOrderCarDto;
import com.cjyc.common.model.dto.web.order.ListOrderCarDto;
import com.cjyc.common.model.dto.web.order.WaitDispatchListOrderCarDto;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.OrderCarCenterVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.order.ListOrderCarVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.model.vo.web.order.TransportInfoOrderCarVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 订单明细（车辆表） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IOrderCarDao extends BaseMapper<OrderCar> {

    /**
     * 按地级城市统计待调度车辆数量
     * @author JPG
     * @since 2019/10/15 14:12
     * @param
     */
    List<Map<String, Object>> countListWaitDispatchCar();

    /**
     * 统计全部待调度车辆数量
     * @author JPG
     * @since 2019/10/15 14:54
     * @param
     */
    Map<String, Object> countTotalWaitDispatchCar();

    /**
     * 查询待调度车辆列表
     * @author JPG
     * @since 2019/10/16 8:29
     * @param
     * @param paramsDto
     * @param bizScope
     */
    List<OrderCarWaitDispatchVo> findWaitDispatchCarList(@Param("paramsDto") WaitDispatchListOrderCarDto paramsDto, @Param("bizScope") List<Long> bizScope);

    /**
     * 按线路统计待调度车辆（统计列表）
     * @author JPG
     * @since 2019/10/16 10:26
     * @param paramsDto 参数条件
     */
    List<Map<String, Object>> findlineWaitDispatchCarCountList(@Param("paramsDto") LineWaitDispatchCountListOrderCarDto paramsDto);

   /**
    * 更新状态
    * @author JPG
    * @since 2019/10/18 15:22
    * @param state
    * @param orderCarId
    */
    int updateStateById(@Param("state") int state, @Param("orderCarId") Long orderCarId);

    int updatePickStateById(@Param("state") int state, Long orderCarId);
    void updateBackStateById(@Param("state") int state, Long orderCarId);
    /**
     * 批量更新状态
     * @author JPG
     * @since 2019/10/18 15:22
     * @param state
     * @param list
     */
    int updateStateBatchByIds(@Param("state") int state, List<Long> list);
    void updateTrunkStateBatchByIds(@Param("state") int state, List<Long> list);

    int saveBatch(@Param("orderCarlist") List<OrderCar> orderCarlist);

    List<OrderCar> findByOrderId(Long orderId);

    int deleteBatchByOrderId(@Param("orderId") Long orderId);

    List<ChangePriceOrderCarDto> findChangePriceDtoByOrderId(Long orderId);

    List<ListOrderCarVo> findListSelective(@Param("paramsDto") ListOrderCarDto paramsDto);

    List<OrderCar> findListByWaybillId(Long waybillId);

    BigDecimal getWLTotalFee(Long orderId);

    Map<String, Object> countTotalWaitDispatchCarByStartCity(@Param("paramsDto") LineWaitDispatchCountListOrderCarDto paramsDto);

    /**
     * 根据条件查询车辆信息
     * @param orderNo
     * @param model
     * @return
     */
    List<OrderCarCenterVo> selectByCondition(@Param("orderNo") String orderNo, @Param("model") String model);

    OrderCarVo findExtraById(Long orderCarId);

    int updateStateForLoad(@Param("orderCarState") int orderCarState, @Param("orderCarIdSet") Set<Long> orderCarIdSet);

    int updateTrunkStateById(@Param("state") int state, @Param("id") Long id);


    List<OrderCarVo> findVoListByIds(@Param("orderCarIdList") List<Long> orderCarIdList);

    /**
     * 查询未开发票订单列表
     * @param loginId
     * @return
     */
    List<InvoiceOrderVo> selectUnInvoiceOrderList(Long loginId);

    /**
     * 查询发票申请信息订单明细
     * @param dto
     * @return
     */
    List<InvoiceOrderVo> selectInvoiceOrderList(InvoiceApplyQueryDto dto);

    /**
     * 查询车辆运输信息
     * @author JPG
     * @since 2019/11/6 18:40
     * @param orderId
     */
    List<TransportInfoOrderCarVo> findTransportStateByOrderId(Long orderId);

    int countUnFinishByOrderId(Long id);

    int updateForReceiptBatch(@Param("collection") Collection<Long> orderCarIdSet);
    int updateForReceipt(Long orderCarId);

    List<OrderCarWaitDispatchVo> findWaitDispatchCarList(@Param("paramsDto") WaitDispatchListOrderCarDto paramsDto);

    List<OrderCar> findListByIds(@Param("list") List<Long> orderCarIdList);

    List<OrderCar> findListByNos(@Param("list") List<String> orderCarNos);
}
