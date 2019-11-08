package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.WayBillCarrierDto;
import com.cjyc.common.model.dto.web.waybill.LocalListWaybillCarDto;
import com.cjyc.common.model.dto.web.waybill.TrunkListWaybillCarDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.WayBillCarrierVo;
import com.cjyc.common.model.vo.web.waybill.TrunkDetailWaybillCarVo;
import com.cjyc.common.model.vo.web.waybill.LocalListWaybillCarVo;
import com.cjyc.common.model.vo.web.waybill.TrunkCarListWaybillCarVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    int updateStateById(@Param("state") int state, @Param("id") Long id);

    WaybillCar findUnConnectCar(String cityCode);

    WaybillCar findLastTrunkWaybillCar(@Param("cityCode") String cityCode,@Param("orderCarId") Long orderCarId);

    WaybillCar findLastPrevByArea(@Param("orderCarId") Long orderCarId, @Param("areaList") List<String> areaList);

    WaybillCar findLastNextByCity(@Param("orderCarId") Long orderCarId, @Param("areaList") List<String> areaList);

    int countForValidateRepeatTrunkDisPatch(@Param("areaList") List<String> areaList);

    List<LocalListWaybillCarVo> findListLocal(LocalListWaybillCarDto paramsDto);

    List<TrunkCarListWaybillCarVo> findCarListTrunk(TrunkListWaybillCarDto paramsDto);

    List<TrunkDetailWaybillCarVo> findVoByType(@Param("orderCarId") Long orderCarId, @Param("waybillType") Integer waybillType);

    List<TrunkDetailWaybillCarVo> findForTrunkDetail(Long waybillId);
}
