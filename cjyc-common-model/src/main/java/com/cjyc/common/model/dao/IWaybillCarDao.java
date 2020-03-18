package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.dto.salesman.mine.SalesAchieveDto;
import com.cjyc.common.model.dto.web.WayBillCarrierDto;
import com.cjyc.common.model.dto.web.mineStore.StorageCarQueryDto;
import com.cjyc.common.model.dto.web.waybill.GetDto;
import com.cjyc.common.model.dto.web.waybill.LocalListWaybillCarDto;
import com.cjyc.common.model.dto.web.waybill.TrunkListWaybillCarDto;
import com.cjyc.common.model.entity.WaybillCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.defined.DispatchNum;
import com.cjyc.common.model.entity.defined.FullWaybillCar;
import com.cjyc.common.model.entity.defined.BillCarNum;
import com.cjyc.common.model.vo.salesman.dispatch.DispatchListVo;
import com.cjyc.common.model.vo.salesman.dispatch.DispatchRecordVo;
import com.cjyc.common.model.vo.web.WayBillCarrierVo;
import com.cjyc.common.model.vo.web.finance.DriverUpstreamPaidInfoVo;
import com.cjyc.common.model.vo.web.mineStore.StorageCarVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarTransportVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.model.vo.web.waybill.LocalListWaybillCarVo;
import com.cjyc.common.model.vo.web.waybill.TrunkCarListWaybillCarVo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    //int updateByIdSelectiveForNull(WaybillCar waybill);
    int updateByIdForNull(WaybillCar waybill);

    List<WayBillCarrierDto> getWayBillCarrier(WayBillCarrierVo wayBillCarrierVo);

    int saveBatch(@Param("list") List<WaybillCar> list);

    List<WaybillCar> findVoByIds(@Param("waybillCarIdList") List<Long> waybillCarIdList);

    List<WaybillCar> findListByWaybillId(Long waybillId);

    int updateForCancelDispatch(@Param("orderCarId") Long orderCarId, @Param("endStoreId") Long endStoreId, @Param("state") int state);

    List<WaybillCar> findNextDispatch(@Param("orderCarId") Long orderCarId, @Param("endStoreId")Long endStoreId);

    WaybillCar findByTaskCarId(Long taskCarId);

    WaybillCar findByStartStoreAndOrderCar(@Param("orderCarId") Long orderCarId,@Param("inStoreId") Long inStoreId);

    int countByStartCityAndOrderCar(@Param("cityCode") String cityCode, @Param("inStoreId") Long inStoreId);

    WaybillCar findUnConnectCar(String cityCode);

    WaybillCar findLastTrunkWaybillCar(@Param("cityCode") String cityCode,@Param("orderCarId") Long orderCarId);

    WaybillCar findLastPrevByArea(@Param("orderCarId") Long orderCarId, @Param("areaList") List<String> areaList);

    WaybillCar findLastNextByArea(@Param("orderCarId") Long orderCarId, @Param("areaList") List<String> areaList);

    int countForValidateRepeatTrunkDisPatch(@Param("areaList") List<String> areaList);

    List<LocalListWaybillCarVo> findListLocal(@Param("paramsDto") LocalListWaybillCarDto paramsDto);

    List<WaybillCarTransportVo> findVoByType(@Param("orderCarId") Long orderCarId, @Param("waybillType") Integer waybillType);

    List<WaybillCarVo> findVoByWaybillId(Long waybillId);

    List<WaybillCarVo> findVoByTaskId(Long taskId);

    List<TrunkCarListWaybillCarVo> findTrunkCarList(@Param("paramsDto") TrunkListWaybillCarDto paramsDto);

    int deleteByWaybillId(Long waybillId);

    FullWaybillCar findLastPrevByBelongStoreId(@Param("orderCarId") Long orderCarId, @Param("storeId") Long storeId);

    FullWaybillCar findLastNextByBelongStoreId(@Param("orderCarId") Long orderCarId, @Param("storeId") Long storeId);

    int countUnAllFinish(Long waybillId);

    int deleteWaybillCarAndTaskCar(@Param("waybillId") Long waybillId, @Param("unDeleteWaybillCarId") Set<Long> unDeleteWaybillCarId);

    int updateForAllotDriver(Long id);

    int updateBatchForUnload(@Param("set")Set<Long> waybillCarIdSet, @Param("state")int state);

    int updateBatchForLoad(@Param("set") Set<Long> waybillCarIdSet, @Param("state") int state, @Param("currentTimeMillis") long currentTimeMillis);
    int updateForLoad(@Param("id")Long id,  @Param("state") int state, @Param("currentTimeMillis") long currentTimeMillis);

    int updateStateBatchByIds(@Param("set") Set<Long> waybillCarIdSet, @Param("state") int state);

    int countUnFinishByWaybillId(Long waybillId);

    int updateStateById(@Param("id") Long id, @Param("state") int state);

    int updateForLoadReplenishInfo(@Param("id") Long id, @Param("loadPhotoImg") String loadPhotoImg);

    List<WaybillCarVo> findVo(GetDto paramsDto);

    List<WaybillCar> findWaitReceiptListByOrderCarId(Long orderCarId);

    int updateForPaySuccess(Long id);

    WaybillCar findWaitReceiptWaybill(Long orderCarId);

    List<DispatchListVo> getDispatchList(Page<DispatchListVo> page,
                                         @Param("param") DispatchListDto dto);

    /**
     * 功能描述: 查询车辆调度记录
     * @author liuxingxiang
     * @date 2019/12/13
     * @param id
     * @return java.util.List<com.cjyc.common.model.vo.salesman.dispatch.DispatchRecordVo>
     */
    List<DispatchRecordVo> selectWaybillRecordList(@Param("id") Long id);

    WaybillCar findLastByOderCarId(Long orderCarId);

    WaybillCar findFirstTrunkWaybillCar(Long orderCarId);

    int countPrevTrunk(@Param("id") Long id);

    List<Long> findCarIdsByWaybillId(Long waybillId);

    List<WaybillCar> findListByIds(@Param("list") List<Long> waitDeleteWaybillCarIds);

    List<WaybillCar> findWaitCancelListByUnCancelIds(@Param("set") Set<Long> unDeleteWaybillCarIds, @Param("waybillId") Long waybillId);

    int cancelAfterWaybillCar(@Param("waybillCarId") Long waybillCarId, @Param("orderCarNo") String orderCarNo);

    int countTrunkWaybillCar(String orderCarNo);

    /**
     * 业务员端已交付台数
     * @param achieveDto
     * @return
     */
    Integer deliveredCarCount(SalesAchieveDto achieveDto);

    /**
     * 获取业务员端提车完成台数
     * @param achieveDto
     * @return
     */
    Integer waybillCarPickCount(SalesAchieveDto achieveDto);

    /**
     * 获取业务员端送车完成台数
     * @param achieveDto
     * @return
     */
    Integer waybillCarBackCount(SalesAchieveDto achieveDto);

    int updateForInStore(Long id);
    int updateForFinish(Long id);

    /**
     * 功能描述: 分页查询在库列表
     * @author liuxingxiang
     * @date 2019/12/25
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.web.mineStore.StorageCarVo>
     */
    List<StorageCarVo> selectStorageCarPage(StorageCarQueryDto dto);

    /**
     * 功能描述: 查询在库车辆数量
     * @author liuxingxiang
     * @date 2019/12/25
     * @param nowStoreId
     * @return int
     */
    int selectStorageCount(@Param("nowStoreId") Long nowStoreId);

    WaybillCar findLastByOderCarIdAndId(@Param("id") Long id, @Param("orderCarId") Long orderCarId);

    List<WaybillCar> findUnAllotListByWaybillId(Long waybillId);

    List<WaybillCar> findAfterWaybillCar(@Param("waybillCarId") Long waybillCarId, @Param("orderCarNo") String orderCarNo);

    WaybillCar findNextWaybillCar(@Param("waybillCarId") Long waybillCarId, @Param("orderCarNo") String orderCarNo);

    int cancelBatch(@Param("collect") Collection<Long> afterWaybillCarIds);

    WaybillCar findFirst(@Param("orderCarId") Long orderCarId);

    void updateForUnloadReplenishInfo(@Param("id") Long id, @Param("unloadPhotoImg") String unloadPhotoImg);

    List<WaybillCar> findListByOrderCarIds(@Param("list") List<Long> orderCarIds);

    WaybillCar findBackWaybill(Long orderCarId);

    int countByStartAddress(@Param("orderCarId") Long orderCarId, @Param("areaCode") String areaCode, @Param("address") String address);

    String findUploadPhoto(Long orderCarId);

    int updateSelfCarryForFinish(Long id);

    WaybillCar selectWaybillCar(DetailQueryDto dto);

    List<WaybillCar>  selectWaybillCarList(Map<String,Object> map);

    DispatchNum countOrderDispatchState(Long orderCarId);

    BillCarNum countUnFinishForState(Long waybillId);

    int updateLoadImgsNull(Long id);

    int countActiveWaybill(@Param("orderCarId") Long orderCarId, @Param("type") int type);

    /**
     * 根据运单号查看上游付款状态列表
     *
     * @param waybillNo
     * @return
     */
    List<DriverUpstreamPaidInfoVo> listDriverUpstreamPaidInfo(String waybillNo);
}
