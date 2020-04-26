package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.dto.salesman.dispatch.WaitCountDto;
import com.cjyc.common.model.dto.salesman.dispatch.WaitCountLineDto;
import com.cjyc.common.model.dto.salesman.mine.OrderCarDto;
import com.cjyc.common.model.dto.salesman.mine.SalesAchieveDto;
import com.cjyc.common.model.dto.salesman.mine.StockCarDto;
import com.cjyc.common.model.dto.web.BaseWebDto;
import com.cjyc.common.model.dto.web.dispatch.LineWaitCountDto;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.OrderCarCenterVo;
import com.cjyc.common.model.vo.salesman.dispatch.CityCarCountVo;
import com.cjyc.common.model.vo.salesman.dispatch.StartAndEndCityCountVo;
import com.cjyc.common.model.vo.salesman.dispatch.WaitDispatchCarListVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarDetailVo;
import com.cjyc.common.model.vo.salesman.mine.StockCarVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.dispatch.WaitCountLineVo;
import com.cjyc.common.model.vo.web.dispatch.WaitCountVo;
import com.cjyc.common.model.vo.web.order.ListOrderCarVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.model.vo.web.order.TransportInfoOrderCarVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
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
     *
     * @param
     * @author JPG
     * @since 2019/10/15 14:12
     */
    @Deprecated
    List<Map<String, Object>> countListWaitDispatchCar();

    /**
     * 统计全部待调度车辆数量
     *
     * @param
     * @author JPG
     * @since 2019/10/15 14:54
     */
    Map<String, Object> countTotalWaitDispatchCar();

    /**
     * 查询待调度车辆列表
     *
     * @param
     * @param paramsDto
     * @param bizScope
     * @author JPG
     * @since 2019/10/16 8:29
     */
    @Deprecated
    List<OrderCarWaitDispatchVo> findWaitDispatchCarList(@Param("paramsDto") WaitDispatchListOrderCarDto paramsDto, @Param("bizScope") List<Long> bizScope);

    /**
     * 按线路统计待调度车辆（统计列表）
     *
     * @param paramsDto 参数条件
     * @author JPG
     * @since 2019/10/16 10:26
     */
    List<Map<String, Object>> findLineWaitDispatchCarCountList(@Param("paramsDto") LineWaitDispatchCountDto paramsDto);

    /**
     * 更新状态
     *
     * @param state
     * @param orderCarId
     * @author JPG
     * @since 2019/10/18 15:22
     */
    int updateStateById(@Param("state") int state, @Param("orderCarId") Long orderCarId);

    int updatePickStateById(@Param("state") int state, Long orderCarId);

    void updateBackStateById(@Param("state") int state, Long orderCarId);

    /**
     * 批量更新状态
     *
     * @param state
     * @param list
     * @author JPG
     * @since 2019/10/18 15:22
     */
    int updateStateBatchByIds(@Param("state") int state, List<Long> list);

    void updateTrunkStateBatchByIds(@Param("state") int state, List<Long> list);

    int saveBatch(@Param("orderCarlist") List<OrderCar> orderCarlist);

    List<OrderCar> findListByOrderId(Long orderId);

    int deleteBatchByOrderId(@Param("orderId") Long orderId);

    List<ChangePriceOrderCarDto> findChangePriceDtoByOrderId(Long orderId);

    List<ListOrderCarVo> findListSelective(@Param("paramsDto") ListOrderCarDto paramsDto);

    List<OrderCar> findListByWaybillId(Long waybillId);

    BigDecimal getWLTotalFee(Long orderId);

    @Deprecated
    Map<String, Object> countTotalWaitDispatchCarByStartCity(@Param("paramsDto") LineWaitDispatchCountDto paramsDto);

    /**
     * 根据条件查询车辆信息
     *
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
     *
     * @param loginId
     * @return
     */
    List<InvoiceOrderVo> selectUnInvoiceOrderList(Long loginId);

    /**
     * 查询发票申请信息订单明细
     *
     * @param dto
     * @return
     */
    List<InvoiceOrderVo> selectInvoiceOrderList(InvoiceApplyQueryDto dto);

    /**
     * 查询车辆运输信息
     *
     * @param orderId
     * @author JPG
     * @since 2019/11/6 18:40
     */
    List<TransportInfoOrderCarVo> findTransportStateByOrderId(Long orderId);

    int countUnFinishByOrderId(Long id);

    int updateForReceiptBatch(@Param("collection") Collection<Long> orderCarIdSet);

    int updateForPaySuccess(@Param("orderCarId") Long orderCarId, @Param("areaCode") String areaCode);

    int updateForPrePaySuccess(Long orderCarId);

    List<WaitDispatchCarListVo> findWaitDispatchCarListForApp(@Param("param") DispatchListDto paramsDto);

    List<OrderCarWaitDispatchVo> findWaitDispatchTrunkCarList(@Param("paramsDto") WaitDispatchTrunkDto paramsDto);

    List<OrderCar> findListByIds(@Param("list") Collection<Long> orderCarIdList);

    List<OrderCar> findListByNos(@Param("list") Collection<String> orderCarNos);

    /**
     * 业务员端我的库存车辆
     *
     * @param dto
     * @return
     */
    List<StockCarVo> findStockCar(StockCarDto dto);

    /**
     * 根据车辆id获取车辆信息
     *
     * @param dto
     * @return
     */
    StockCarDetailVo findOrderCar(OrderCarDto dto);

    /**
     * 功能描述: 查询出发地相同的车辆数量
     *
     * @param storeIds
     * @return java.util.List<com.cjyc.common.model.vo.salesman.dispatch.CityCarCountVo>
     * @author liuxingxiang
     * @date 2019/12/11
     */
    List<CityCarCountVo> selectStartCityCarCount(@Param("storeIds") Set<Long> storeIds);

    /**
     * 功能描述: 查询出发地与目的地相同的车辆数量
     *
     * @param map
     * @return java.util.List<com.cjyc.common.model.vo.salesman.dispatch.StartAndEndCityCountVo>
     * @author liuxingxiang
     * @date 2019/12/11
     */
    List<StartAndEndCityCountVo> selectStartAndEndCityCarCount(Map<String,Object> map);

    OrderCarVo findVoById(Long orderCarId);

    int updateForDispatchTrunk(@Param("id") Long id, @Param("pickState") Integer pickState, @Param("trunkState") Integer trunkState);



    /**
     * 业务员端我下的订单车辆台数
     * @param achieveDto
     * @return
     */
    Integer orderCarCount(SalesAchieveDto achieveDto);

    /**
     * 业务员端我已确认车辆台数
     * @param achieveDto
     * @return
     */
    Integer orderCarCheckCount(SalesAchieveDto achieveDto);

    List<Map<String, Object>> countTrunkListWaitDispatchCar(@Param("paramsDto") BaseWebDto paramsDto);

    List<Map<String, Object>> findLineWaitDispatchTrunkCarCountList(@Param("paramsDto") LineWaitDispatchCountDto paramsDto);

    int updateLocation(@Param("orderCarId") Long orderCarId, @Param("nowStoreId") Long nowStoreId, @Param("nowAreaCode") String nowAreaCode);

    Map<String, Object> countTotalTrunkWaitDispatchCarByStartCity(@Param("paramsDto") LineWaitDispatchCountDto paramsDto);

    @Deprecated
    Map<String, Object> countTotalTrunkWaitDispatchCar(@Param("paramsDto") BaseWebDto paramsDto);

    int updateForFinish(@Param("orderCarId") Long orderCarId, @Param("areaCode")String areaCode);

    List<Map<String, Object>> countListWaitDispatchCarV2(@Param("paramsDto") BaseWebDto paramsDto);

    @Deprecated
    Map<String, Object> countTotalWaitDispatchCarV2(@Param("paramsDto")BaseWebDto paramsDto);

    List<WaitCountVo> findWaitDispatchCarCountListForApp(@Param("param") BaseSalesDto param);

    Map<String, Object> countTotalWaitDispatchCarCountListForApp(@Param("param")BaseSalesDto param);

    @Deprecated
    List<WaitCountLineVo> findWaitDispatchCarCountLineListForApp(@Param("param") WaitCountDto param);

    Map<String, Object> countTotalWaitDispatchCarCountLineListForApp(@Param("param")WaitCountLineDto param);

    List<Map<String, Object>> findLineWaitDispatchCarCountListV2(@Param("paramsDto")LineWaitCountDto paramsDto);

    Map<String, Object> countTotalWaitDispatchCarByStartCityV2(@Param("paramsDto")LineWaitCountDto paramsDto);

    List<WaybillCarVo> findPickCarEndpoint(@Param("list") List<Long> orderCarIdList, @Param("sort") Integer sort);

    List<WaybillCarVo> findBackCarEndpoint(@Param("list")List<Long> orderCarIdList, @Param("sort") Integer sort);

    List<WaybillCarVo> findTrunkCarEndpoint(@Param("list")List<Long> orderCarIdList, @Param("sort") Integer sort);

    List<String> findTrunkNodes(String orderCarId);

    String findOutterState(String orderCarNo);

    List<OrderCarWaitDispatchVo> findWaitDispatchCarListWeb(WaitDispatchListOrderCarDto paramsDto);

    List<Map<String, Object>> countListWaitDispatchCarWeb(BaseWebDto paramsDto);

    List<Map<String, Object>> findLineWaitDispatchCarCountListWeb(LineWaitCountDto paramsDto);

    List<WaitCountLineVo> findWaitDispatchCarCountLineListForAppV2(@Param("param") WaitCountDto paramsDto);

    int countUnPayByOrderId(Long orderId);

    int updateLocationForCancel(Long orderId);

    int countForChechRepeatPlateNo(@Param("orderNo") String orderNo, @Param("orderCarId") Long orderCarId, @Param("plateNo") String plateNo);

    int countForChechRepeatVin(@Param("orderNo") String orderNo, @Param("orderCarId") Long orderCarId, @Param("vin") String vin);

    int updateReleaseFlagByOrderNos(@Param("collection") Collection<String> orderNoSet, @Param("releaseFlag") Integer releaseFlag);
    int updateReleaseFlagByNos(@Param("collection") Collection<String> noSet, @Param("releaseFlag") Integer releaseFlag);

    List<OrderCar> findListbyOrderNos(@Param("collection") Collection<String> orderNoSet);
}
