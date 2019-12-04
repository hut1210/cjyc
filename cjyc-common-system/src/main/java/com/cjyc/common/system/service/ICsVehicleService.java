package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.CarrierVehicleNoDto;
import com.cjyc.common.model.dto.web.vehicle.FreeVehicleDto;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.CarrierVehicleVo;
import com.cjyc.common.model.vo.driver.mine.SocietyVehicleVo;

import java.util.List;

public interface ICsVehicleService {

    /**
     * 获取社会空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findPersonFreeVehicle(KeywordDto dto);

    /**
     * 获取司机端社会空闲车辆
     * @param dto
     * @return
     */
    ResultVo<SocietyVehicleVo> findSocietyFreeVehicle(CarrierVehicleNoDto dto);

    /**
     * 获取承运商下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findCarrierFreeVehicle(FreeDto dto);

    /**
     * 根据承运商获取该承运商下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo<CarrierVehicleVo> findCompanyFreeVehicle(CarrierVehicleNoDto dto);

    /**
     * 根据承运商id获取该承运商下的空闲车辆
     * @param dto
     * @return
     */
    ResultVo<List<FreeVehicleVo>> findCarrierVehicleById(FreeVehicleDto dto);
}
