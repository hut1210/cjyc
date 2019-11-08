package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.waybill.CancelDispatchDto;
import com.cjyc.common.model.dto.web.waybill.LocalDispatchListWaybillDto;
import com.cjyc.common.model.dto.web.waybill.TrunkDispatchListShellWaybillDto;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;

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
    ResultVo localDisoatch(LocalDispatchListWaybillDto paramsDto);

    /**
     * 干线调度
     * @author JPG
     * @since 2019/11/5 17:23
     * @param paramsDto
     */
    ResultVo trunkDispatch(TrunkDispatchListShellWaybillDto paramsDto);

    /**
     * 取消调度
     * @author JPG
     * @since 2019/11/5 17:33
     * @param paramsDto
     */
    ResultVo<ListVo<BaseTipVo>> cancelDispatch(CancelDispatchDto paramsDto);
}
