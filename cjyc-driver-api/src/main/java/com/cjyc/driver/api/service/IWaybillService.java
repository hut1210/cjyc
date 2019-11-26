package com.cjyc.driver.api.service;

import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.driver.waybill.WaitAllotDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.waybill.WaitAllotVo;

public interface IWaybillService {
    ResultVo<PageVo<WaitAllotVo>> getWaitAllotPage(WaitAllotDto dto);

    ResultVo replenishInfo(ReplenishInfoDto reqDto);
}
