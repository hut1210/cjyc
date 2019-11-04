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

    ResultVo cancelDispatch(CancelDispatchDto paramsDto);

    ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListWaybillDto reqDto);

    ResultVo<List<CysWaybillVo>> cysList(CysWaybillDto reqDto);

    ResultVo<PageVo<LocalListWaybillCarVo>> Locallist(LocalListWaybillCarDto reqDto);

    ResultVo<PageVo<TrunkListWaybillVo>> trunklist(TrunkListWaybillDto reqDto);

    ResultVo<PageVo<TrunkListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto reqDto);
}
