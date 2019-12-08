package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.mine.AppCarrierVehicleDto;
import com.cjyc.common.model.dto.driver.mine.CarrierVehicleNoDto;
import com.cjyc.common.model.dto.web.vehicle.FreeVehicleDto;
import com.cjyc.common.model.entity.Vehicle;
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

    /**
     * 验证司机与车辆之间关系
     * @return
     */
    boolean verifyDriverVehicle(Long driverId,Long vehicleId);

    /**
     * 封装运力
     * @param dto
     * @param appDto
     * @param veh
     */
    void saveTransport(CarrierVehicleDto dto, AppCarrierVehicleDto appDto, Vehicle veh);

    /**
     * 更新运力
     * @param dto
     * @param appDto
     * @param veh
     */
    void updateTransport(CarrierVehicleDto dto, AppCarrierVehicleDto appDto, Vehicle veh);
}
