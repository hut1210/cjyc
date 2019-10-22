package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.waybill.CancelDispatchDto;
import com.cjyc.common.model.dto.web.waybill.WaybillPickDispatchListDto;
import com.cjyc.common.model.dto.web.waybill.WaybillTrunkDispatchListListDto;
import com.cjyc.common.model.entity.Waybill;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;

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
    ResultVo pickAndBackDispatch(WaybillPickDispatchListDto paramsDto);

    /**
     * 干线调度
     * @author JPG
     * @since 2019/10/17 9:16
     * @param paramsDto
     */
    ResultVo trunkDispatch(WaybillTrunkDispatchListListDto paramsDto);

    ResultVo cancelDispatch(CancelDispatchDto paramsDto);
}
