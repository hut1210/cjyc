package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.waybill.HistoryListWaybillDto;
import com.cjyc.common.model.dto.web.waybill.TrunkListWaybillDto;
import com.cjyc.common.model.entity.Waybill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.waybill.HistoryListWaybillVo;
import com.cjyc.common.model.vo.web.waybill.TrunkListWaybillVo;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据运单号批量更新运单状态
     * @param waybillNoList 运单编号列表
     * @param state 状态
     * @return
     */
    int updateStateBatchByNos(@Param("state") int state, @Param("waybillNoList") List<String> waybillNoList);

    /**
     * 批量根据运单号查询未承接运单
     * @author JPG
     * @since 2019/10/18 17:03
     * @param waybillNoList
     */
    List<Waybill> findListByNos(@Param("waybillNoList") List<String> waybillNoList);

    List<HistoryListWaybillVo> findHistoryList(@Param("paramsDto") HistoryListWaybillDto paramsDto);

    List<Waybill> findByOrderCarId(Long orderCarId);

    List<Integer> findTrunkStateListByOrderCarId(Long orderCarId);


    int countWaybill(@Param("orderCarId") Long orderCarId, @Param("waybillType") int waybillType);

    List<TrunkListWaybillVo> findListTrunk(TrunkListWaybillDto paramsDto);
}
