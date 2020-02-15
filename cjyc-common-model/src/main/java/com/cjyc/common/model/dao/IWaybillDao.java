package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.salesman.dispatch.HistoryDispatchRecordDto;
import com.cjyc.common.model.dto.web.mineCarrier.MyWaybillDto;
import com.cjyc.common.model.dto.web.mineCarrier.SettlementDetailQueryDto;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.entity.WaybillCar;
import com.cjyc.common.model.entity.defined.FullWaybill;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskBillVo;
import com.cjyc.common.model.vo.driver.waybill.WaitAllotVo;
import com.cjyc.common.model.vo.salesman.dispatch.HistoryDispatchRecordVo;
import com.cjyc.common.model.vo.web.mineCarrier.SettlementDetailVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyWaybillVo;
import com.cjyc.common.model.vo.web.waybill.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 运单表(业务员调度单) Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IWaybillDao extends BaseMapper<Waybill> {

    Waybill findByNo(String waybillNo);

    //int updateByIdSelectiveForNull(Waybill waybill);
    int updateByIdForNull(Waybill waybill);
    /**
     * 根据运单号批量更新运单状态
     * @param waybillNoList 运单编号列表
     * @param state 状态
     * @return
     */
    int updateStateBatchByNos(@Param("state") int state, @Param("waybillNoList") List<String> waybillNoList);
    int updateStateBatchByIds(@Param("state") int code, @Param("waybillIdList") List<Long> waybillIdList);
    /**
     * 批量根据运单号查询未承接运单
     * @author JPG
     * @since 2019/10/18 17:03
     * @param waybillNoList
     */
    List<Waybill> findListByNos(@Param("waybillNoList") List<String> waybillNoList);

    List<HistoryListWaybillVo> findHistoryList(@Param("paramsDto") HistoryListDto paramsDto);

    List<Waybill> findByOrderCarId(Long orderCarId);

    List<Integer> findTrunkStateListByOrderCarId(Long orderCarId);


    int countWaybill(@Param("orderCarId") Long orderCarId, @Param("waybillType") int waybillType);

    List<TrunkListWaybillVo> findListTrunk(@Param("paramsDto") TrunkListWaybillDto paramsDto);

    List<TrunkListWaybillVo> findLeftListTrunk(TrunkListWaybillDto paramsDto);

    WaybillVo findVoById(Long id);


    List<TrunkMainListWaybillVo> findMainListTrunk(@Param("paramsDto") TrunkMainListWaybillDto paramsDto);

    List<TrunkSubListWaybillVo> findSubListTrunk(@Param("paramsDto")TrunkSubListWaybillDto paramsDto);

    /**
     * 获取承运商下的运单
     * @param dto
     * @return
     */
    List<MyWaybillVo> findByCarrierId(MyWaybillDto dto);
    List<Waybill> findListByIds(@Param("waybillIdList") List<Long> waybillIdList);

    int updateStateById(@Param("state") int state, @Param("waybillId") Long waybillId);

    List<CrWaybillVo> findCrListForMineCarrier(@Param("paramsDto") CrWaybillDto paramsDto);

    ResultVo<List<CrWaybillVo>> findCrListByCarrierId(@Param("paramsDto") CrWaybillDto paramsDto);

    List<WaitAllotVo> findWaitAllotListByCarrierIds(List<Long> carrierIds);

    /**
     * 功能描述: 查询待分配运单列表
     * @author liuxingxiang
     * @date 2019/11/20
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.TaskBillVo>
     */
    List<TaskBillVo> selectWaitHandleTaskPage(BaseDriverDto dto);

    int updateForAllotDriver(Long waybillId);

    int updateForReceipt(@Param("id") Long id, @Param("currentTimeMillis") long currentTimeMillis);

    /**
     * 功能描述: 查询历史调度记录列表分页
     * @author liuxingxiang
     * @date 2019/12/13
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.salesman.dispatch.HistoryDispatchRecordVo>
     */
    List<HistoryDispatchRecordVo> selectHistoryDispatchRecord(HistoryDispatchRecordDto dto);

    List<SettlementDetailVo> getSettlementDetail(SettlementDetailQueryDto settlementDetailQueryDto);

    int updateForLoad(Long id);

    int updateForFinish(Long id);


    int updateNum(@Param("waybillId") Long waybillId);

    int updateFreightFee(Long id);

    List<Waybill> findListByWaybillCarIds(@Param("collection") Collection<Long> waybillCarIds);

    int updateNumAndFreightFee(@Param("waybillId") Long waybillId);
}
