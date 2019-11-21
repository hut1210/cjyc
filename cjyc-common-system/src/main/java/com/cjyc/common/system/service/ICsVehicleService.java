package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.FreeVehicleDto;
import com.cjyc.common.model.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestBody;

public interface ICsVehicleService {

    /**
     * 获取该承运商下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo findFreeVehicle(@RequestBody FreeVehicleDto dto);
}
