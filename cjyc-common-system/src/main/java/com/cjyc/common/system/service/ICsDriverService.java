package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.driver.mine.CarrierDriverNameDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.entity.CarrierDriverCon;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.CarrierDriverVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;

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
    ResultVo<CarrierDriverVo> findCompanyFreeDriver(CarrierDriverNameDto dto);

    /**
     * 获取用户角色id
     * @param roleResps
     * @param cdc
     * @return
     */
    ResultVo<Long> findRoleId(List<SelectRoleResp> roleResps, CarrierDriverCon cdc);

    /**
     * 调度个人司机
     * @param dto
     * @return
     */
    ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(DispatchDriverDto dto);


    /************************************韵车集成改版 st***********************************/

    /**
     * 调度社会司机
     * @param dto
     * @return
     */
    ResultVo<PageVo<DispatchDriverVo>> dispatchDriverNew(DispatchDriverDto dto);

    /**
     * 根据承运商id获取该承运商下的空闲司机
     * @param dto
     * @return
     */
    ResultVo<CarrierDriverVo> findCompanyFreeDriverNew(CarrierDriverNameDto dto);

    /**
     * 添加和修改承运商下司机信息
     * @param dto
     * @return
     */
    ResultVo saveOrModifyDriverAppNew(CarrierDriverDto dto);
}
