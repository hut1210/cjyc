package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.*;

import java.util.List;

/**
 * <p>
 * 运单表(业务员调度单) 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
public interface IWaybillService extends IService<Waybill> {

    /**
     * 提送车调度
     * @author JPG
     * @since 2019/10/17 9:16
     * @param paramsDto
     */
    ResultVo localDispatch(LocalDispatchListWaybillDto paramsDto);

    /**
     * 干线调度
     * @author JPG
     * @since 2019/10/17 9:16
     * @param paramsDto
     */
    ResultVo trunkDispatch(TrunkDispatchListShellWaybillDto paramsDto);

    ResultVo<ListVo<BaseTipVo>> cancelDispatch(CancelDispatchDto paramsDto);

    ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListWaybillDto reqDto);

    ResultVo<List<CysWaybillVo>> cysList(CysWaybillDto reqDto);

    ResultVo<PageVo<LocalListWaybillCarVo>> locallist(LocalListWaybillCarDto reqDto);

    ResultVo<PageVo<TrunkListWaybillVo>> trunklist(TrunkListWaybillDto reqDto);

    ResultVo<PageVo<TrunkCarListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto reqDto);

    ResultVo<WaybillVo> getForTrunkDetail(Long id);

    ResultVo<List<TrunkDetailWaybillCarVo>> getCarByType(Long orderCarId, Integer waybillType);

    ResultVo<PageVo<TrunkMainListWaybillVo>> getTrunkMainList(TrunkMainListWaybillDto reqDto);

    ResultVo<PageVo<TrunkSubListWaybillVo>> getTrunkSubList(TrunkSubListWaybillDto reqDto);
}
