package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.Waybill;
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

    ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListDto reqDto);

    ResultVo<PageVo<CrWaybillVo>> crListForMineCarrier(CrWaybillDto reqDto);

    ResultVo<PageVo<LocalListWaybillCarVo>> locallist(LocalListWaybillCarDto reqDto);

    List<LocalListWaybillCarVo> localAllList(LocalListWaybillCarDto reqDto);

    ResultVo<PageVo<TrunkCarListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto reqDto);

    ResultVo<WaybillVo> get(Long id);

    ResultVo<List<WaybillCarTransportVo>> getCarByType(Long orderCarId, Integer waybillType);

    ResultVo<PageVo<TrunkMainListWaybillVo>> getTrunkMainList(TrunkMainListWaybillDto reqDto);

    List<TrunkMainListWaybillVo> getTrunkMainAllList(TrunkMainListWaybillDto reqDto);

    ResultVo<PageVo<TrunkSubListWaybillVo>> getTrunkSubList(TrunkSubListWaybillDto reqDto);

    List<TrunkSubListWaybillVo> getTrunkSubAllList(TrunkSubListWaybillDto reqDto);

    ResultVo<WaybillVo> get(getWaybillDto reqDto);
}
