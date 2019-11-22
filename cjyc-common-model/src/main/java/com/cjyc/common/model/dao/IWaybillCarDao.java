package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.WayBillCarrierDto;
import com.cjyc.common.model.dto.web.waybill.LocalListWaybillCarDto;
import com.cjyc.common.model.dto.web.waybill.TrunkListWaybillCarDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.defined.FullWaybillCar;
import com.cjyc.common.model.vo.web.WayBillCarrierVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.model.vo.web.waybill.LocalListWaybillCarVo;
import com.cjyc.common.model.vo.web.waybill.TrunkCarListWaybillCarVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 运单明细表(车辆表) Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IWaybillCarDao extends BaseMapper<WaybillCar> {

    List<WayBillCarrierDto> getWayBillCarrier(WayBillCarrierVo wayBillCarrierVo);

    int saveBatch(@Param("list") List<WaybillCar> list);

    List<WaybillCar> findVoByIds(@Param("waybillCarIdList") List<Long> waybillCarIdList);

    List<WaybillCar> findListByWaybillId(Long waybillId);

    int updateForCancelDispatch(@Param("orderCarId") Long orderCarId, @Param("endStoreId") Long endStoreId, @Param("state") int state);

    List<WaybillCar> findNextDispatch(@Param("orderCarId") Long orderCarId, @Param("endStoreId")Long endStoreId);

    WaybillCar findByTaskCarId(Long taskCarId);

    WaybillCar findByStartStoreAndOrderCar(@Param("orderCarId") Long orderCarId,@Param("inStoreId") Long inStoreId);

    int countByStartCityAndOrderCar(@Param("cityCode") String cityCode, @Param("inStoreId") Long inStoreId);

    int updateStateForLoad(@Param("state") int state, @Param("set") Set<Long> waybillCarIdSet);

    WaybillCar findUnConnectCar(String cityCode);

    WaybillCar findLastTrunkWaybillCar(@Param("cityCode") String cityCode,@Param("orderCarId") Long orderCarId);

    WaybillCar findLastPrevByArea(@Param("orderCarId") Long orderCarId, @Param("areaList") List<String> areaList);

    WaybillCar findLastNextByArea(@Param("orderCarId") Long orderCarId, @Param("areaList") List<String> areaList);

    int countForValidateRepeatTrunkDisPatch(@Param("areaList") List<String> areaList);

    List<LocalListWaybillCarVo> findListLocal(@Param("paramsDto") LocalListWaybillCarDto paramsDto);

    List<WaybillCarVo> findVoByType(@Param("orderCarId") Long orderCarId, @Param("waybillType") Integer waybillType);

    List<WaybillCarVo> findVoByWaybillId(Long waybillId);

    List<WaybillCarVo> findVoByTaskId(Long taskId);

    List<TrunkCarListWaybillCarVo> findTrunkCarList(@Param("paramsDto") TrunkListWaybillCarDto paramsDto);

    int deleteByWaybillId(Long waybillId);

    FullWaybillCar findLastPrevByBelongStoreId(@Param("orderCarId") Long orderCarId, @Param("storeId") Long storeId);

    FullWaybillCar findLastNextByBelongStoreId(@Param("orderCarId") Long orderCarId, @Param("storeId") Long storeId);

    int countUnAllFinish(Long waybillId);

    int deleteWaybillCarAndTaskCar(@Param("waybillId") Long waybillId, @Param("unDeleteWaybillCarId") Set<Long> unDeleteWaybillCarId);

    int updateForAllotDriver(Long id);

    int updateInfoForUnload(List<WaybillCar> updateWaybillCarList);
}
