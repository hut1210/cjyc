package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.dto.web.vehicle.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    ResultVo<PageVo<VehicleVo>> findVehicle(SelectVehicleDto dto);

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
     * 车辆管理导出
     * @param request
     * @param response
     */
    void exportVehicleExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 社会车辆导入Excel文件
     * @param file
     * @return
     */
    boolean importVehicleExcel(MultipartFile file, Long loginId);
}
