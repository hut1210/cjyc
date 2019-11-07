package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.vehicle.DriverVehicleConDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

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
    boolean saveVehicle(VehicleDto dto);

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
    ResultVo verifyVehicle(OperateDto dto);

    /**
     * 修改司机与车辆的绑定关系
     * @param dto
     * @return
     */
    ResultVo modifyVehicle(DriverVehicleConDto dto);

}
