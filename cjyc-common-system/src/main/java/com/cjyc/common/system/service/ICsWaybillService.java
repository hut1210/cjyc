package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.Map;
import java.util.Set;

/**
 * 运单接口
 * @author JPG
 */
public interface ICsWaybillService {
    /**
     * 同城调度
     * @author JPG
     * @since 2019/11/5 17:07
     * @param paramsDto
     */
    ResultVo saveLocal(SaveLocalDto paramsDto);

    /**
     * 干线调度
     * @author JPG
     * @since 2019/11/5 17:23
     * @param paramsDto
     */
    ResultVo saveTrunk(SaveTrunkWaybillDto paramsDto);

    boolean validateIsArriveEndStore(Long orderEndStoreId, Long waybillCarEndStoreId);

    /**
     * 取消调度
     * @author JPG
     * @since 2019/11/5 17:33
     * @param paramsDto
     */
    ResultVo<ListVo<BaseTipVo>> cancel(CancelWaybillDto paramsDto);

    Order getOrderFromMap(Map<Long, Order> orderMap, Long orderId);

    OrderCar getOrderCarFromMap(Map<Long, OrderCar> orderCarMap, Long orderCarId);

    String computeGuideLine(String startAreaCode, String endAreaCode, String defaultGuideLine, Integer carNum);
    String computeGuideLine(Set<String> startAreaCodeSet, Set<String> endAreaCodeSet, String defaultGuideLine, Integer carNum);

    /**
     * 修改同城运单
     * @author JPG
     * @since 2019/11/9 8:59
     * @param paramsDto
     */
    ResultVo updateLocal(UpdateLocalDto paramsDto);

    /**
     * 修改干线运单
     * @author JPG
     * @since 2019/11/9 8:59
     * @param paramsDto
     */
    ResultVo updateTrunk(UpdateTrunkWaybillDto paramsDto);

    /**
     * 中途强制卸车
     * @author JPG
     * @since 2019/11/13 10:00
     * @param paramsDto
     */
    ResultVo trunkMidwayUnload(TrunkMidwayUnload paramsDto);

    void validateAndFinishWaybill(Long waybillId);

    void cancelWaybill(Waybill waybill);

    WaybillCar getWaybillCarByIdFromMap(Map<Long, WaybillCar> waybillCarMap, Long waybillCarId);

    void cancelWaybillCar(Waybill waybill, WaybillCar waybillCar);
    void cancelWaybillCar(WaybillCar waybillCar);

    WaybillCar getWaybillCarByTaskCarIdFromMap(Map<Long, WaybillCar> waybillCarMap, Long taskCarId);
}
