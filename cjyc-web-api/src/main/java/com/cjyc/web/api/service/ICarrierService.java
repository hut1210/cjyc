package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.carrier.CarrierDto;
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
     * 添加承运商
     * @param dto
     * @return
     */
    boolean saveCarrier(CarrierDto dto);

    /**
     * 更新承运商
     * @param dto
     * @return
     */
    boolean updateCarrier(CarrierDto dto);

    /**
     * 根据条件查询承运商
     * @param dto
     * @return
     */
    PageInfo<CarrierVo> getCarrierByTerm(SeleCarrierDto dto);

    /**
     * 根据承运商id进行审核通过/拒绝/冻结
     * @param id
     * @param state
     * @return
     */
    boolean verifyCarrierById(Long id,Integer state);

    /**
     * 根据承运商id查看承运商信息
     * @param id
     * @return
     */
    BaseCarrierVo getBaseCarrierById(Long id);

    /**
     * 根据承运商id查看车辆信息
     * @param dto
     * @return
     */
    PageInfo<BaseVehicleVo> getBaseVehicleByTerm(SeleVehicleDriverDto dto);

    /**
     * 根据承运商id查看司机信息
     * @param dto
     * @return
     */
    ResultVo getBaseDriverByTerm(SeleVehicleDriverDto dto);

    /**
     * 重置承运商超级管理员密码
     * @param id
     * @return
     */
    ResultVo resetPwd(Long id);
}
