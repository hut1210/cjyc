package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.DispatchCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDriverDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.github.pagehelper.PageInfo;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:22
 *  @Description:承运商管理接口
 */
public interface ICarrierService {

    /**
     * 根据手机号判断该承运商是否已添加
     * @param linkmanPhone
     * @return
     */
    ResultVo existCarrier(String linkmanPhone);
    /**
     * 添加承运商
     * @param dto
     * @return
     */
    ResultVo saveCarrier(CarrierDto dto);

    /**
     * 查询是否为该承运商下的司机
     * @param carrierId,linkmanPhone
     * @return
     */
    ResultVo existCarrierDriver(Long carrierId,String linkmanPhone);

    /**
     * 更新承运商
     * @param dto
     * @return
     */
    ResultVo modifyCarrier(CarrierDto dto);

    /**
     * 根据条件查询承运商
     * @param dto
     * @return
     */
    ResultVo findCarrier(SeleCarrierDto dto);

    /**
     * 根据承运商id进行审核通过/拒绝/冻结
     * @param dto
     * @return
     */
    ResultVo verifyCarrier(OperateDto dto);

    /**
     * 根据承运商carrierId查看承运商信息
     * @param carrierId
     * @return
     */
    ResultVo showBaseCarrier(Long carrierId);

    /**
     * 根据承运商id查看车辆信息
     * @param dto
     * @return
     */
    ResultVo findBaseVehicle(SeleVehicleDriverDto dto);

    /**
     * 根据承运商id查看司机信息
     * @param dto
     * @return
     */
    ResultVo findBaseDriver(SeleVehicleDriverDto dto);

    /**
     * 重置承运商超级管理员密码
     * @param id
     * @return
     */
    ResultVo resetPwd(Long id);

    /**
     * 调度承运商信息
     * @param dto
     * @return
     */
    ResultVo dispatchCarrier(DispatchCarrierDto dto);
}
