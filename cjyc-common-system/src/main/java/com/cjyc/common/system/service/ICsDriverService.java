package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.driver.mine.CarrierDriverNameDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

public interface ICsDriverService {

    Driver getById(Long userId, boolean isSearchCache);

    Driver getByUserId(Long userId);

    /**
     * 将司机信息保存到物流平台
     * @param driver
     * @return
     */
    ResultData<Long> saveDriverToPlatform(Driver driver);

    /**
     * 将司机信息更新到平台用户
     * @param driver
     * @return
     */
    ResultData updateUserToPlatform(Driver driver);

    /**
     * 添加和修改承运商下司机信息
     * @param dto
     * @return
     */
    ResultVo saveOrModifyDriver(CarrierDriverDto dto);

    /**
     * 绑定关系
     * @param dto
     */
    void bindDriverVeh(CarrierVehicleDto dto);

    /**
     * 保存承运商下司机
     * @param driver
     * @param carrierId
     * @return
     */
    ResultData<Long> addDriverToPlatform(Driver driver, Long carrierId);

    /**
     * 更新承运商下司机信息到物流平台
     * @param dto
     * @return
     */
    ResultData updateDriverToPlatform(com.cjyc.common.model.dto.CarrierDriverDto dto);

    Driver validate(Long loginId);

    /**
     * 获取该承运商下空闲司机
     * @param dto
     * @return
     */
    ResultVo<List<FreeDriverVo>> findCarrierFreeDriver(FreeDto dto);

    /**
     * 根据承运商id获取该承运商下的空闲司机
     * @param dto
     * @return
     */
    ResultVo<List<FreeDriverVo>> findCarrierDriver(CarrierDriverNameDto dto);
}
