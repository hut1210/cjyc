package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserResp;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.DispatchCarrierDto;
import com.cjyc.common.model.dto.web.carrier.TrailCarrierDto;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.DispatchCarrierVo;
import com.cjyc.common.model.vo.web.carrier.TrailCarrierVo;

import java.util.List;

public interface ICsCarrierService {

    /**
     * 将承运商相关信息保存到物流平台
     * @param carrier 承运商信息
     * @return
     */
    ResultData<AddDeptAndUserResp> saveCarrierToPlatform(Carrier carrier);

    List<Carrier> getBelongListByDriver(Long driverId);

    List<Long> getBelongIdsListByDriver(Long driverId);
    /**
     * 更新承运商信息
     * @param carrier
     * @param dto
     * @return
     */
    ResultData<Long> updateCarrierToPlatform(Carrier carrier, CarrierDto dto);

    /**
     * 调度中心中提车干线调度中代驾和拖车列表
     * @param dto
     * @return
     */
    ResultVo<PageVo<TrailCarrierVo>> trailDriver(TrailCarrierDto dto);

    /**
     * 调度承运商信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<DispatchCarrierVo>> dispatchCarrier(DispatchCarrierDto dto);



    /*********************************韵车集成改版 st*****************************/

    /**
     * 调度中心中提车干线调度中代驾和拖车列表_改版
     * @param dto
     * @return
     */
    ResultVo<PageVo<TrailCarrierVo>> trailDriverNew(TrailCarrierDto dto);
}
