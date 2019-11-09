package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.web.vehicle.*;
import com.cjyc.common.model.vo.ResultVo;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:22
 *  @Description:运输车辆管理
 */
public interface IVehicleService {

    /**
     * 添加车辆信息
     * @param dto
     * @return
     */
    ResultVo saveVehicle(VehicleDto dto);

    /**
     * 根据条件分页查询车辆信息
     * @param dto
     * @return
     */
    ResultVo findVehicle(SelectVehicleDto dto);

    /**
     * 根据车辆id删除/修改与司机绑定关系
     * @param dto
     * @return
     */
    ResultVo removeVehicle(RemoveVehicleDto dto);

    /**
     * 修改司机与车辆的绑定关系
     * @param dto
     * @return
     */
    ResultVo modifyVehicle(ModifyCarryNumDto dto);

    /**
     * 根据车牌号模糊匹配空闲的社会车辆
     * @param dto
     * @return
     */
    ResultVo findFreeVehicle(FreeVehicleDto dto);

}
