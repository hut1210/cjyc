package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.waybill.*;
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
    ResultVo saveLocal(SaveLocalDto paramsDto);

    /**
     * 干线调度
     * @author JPG
     * @since 2019/11/5 17:23
     * @param paramsDto
     */
    ResultVo saveTrunk(SaveTrunkWaybillDto paramsDto);

    /**
     * 取消调度
     * @author JPG
     * @since 2019/11/5 17:33
     * @param paramsDto
     */
    ResultVo<ListVo<BaseTipVo>> cancel(CancelWaybillDto paramsDto);

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

}
