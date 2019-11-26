package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.CarrierVehicleNoDto;
import com.cjyc.common.model.entity.VehicleRunning;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

public interface ICsVehicleService {

    /**
     * 获取社会空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findPersonFreeVehicle(KeywordDto dto);

    /**
     * 获取承运商下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findCarrierFreeVehicle(FreeDto dto);

    /**
     * 根据承运商id获取该承运商下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findCarrierVehicle(CarrierVehicleNoDto dto);
}
