package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.FreeVehicleDto;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ICsVehicleService {

    /**
     * 获取该承运商(个人/企业)下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findFreeVehicle(@RequestBody FreeVehicleDto dto);
}
