package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.ResultVo;

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
     * @param dto
     * @return
     */
    ResultData<Long> addDriverToPlatform(Driver driver, CarrierDriverDto dto);

    /**
     * 更新承运商下司机信息到物流平台
     * @param dto
     * @return
     */
    ResultData updateDriverToPlatform(CarrierDriverDto dto);

    Driver validate(Long loginId);
}
