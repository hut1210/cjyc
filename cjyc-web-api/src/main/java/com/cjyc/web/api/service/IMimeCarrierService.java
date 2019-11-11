package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mimeCarrier.*;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.vo.ResultVo;

public interface IMimeCarrierService extends IService<Carrier> {

    /**
     * 新增承运商下司机
     * @param dto
     * @return
     */
    ResultVo saveDriver(MyDriverDto dto);

    /**
     * 分页查询承运商下司机
     * @param dto
     * @return
     */
    ResultVo findPageDriver(QueryMyDriverDto dto);

    /**
     * 操作承运商下的司机
     * @param dto
     * @return
     */
    ResultVo verifyDriver(OperateDto dto);

    /**
     * 新增承运商下车辆
     * @param dto
     * @return
     */
    ResultVo saveCar(MyCarDto dto);

    /**
     * 分页查询该承运商下的车辆
     * @param dto
     * @return
     */
    ResultVo findPageCar(QueryMyCarDto dto);

    /**
     * 根据承运商id和司机姓名查询该承运商下的空闲司机
     * @param carrierId
     * @param realName
     * @return
     */
    ResultVo findFreeDriver(Long carrierId,String realName);

    /**
     * 修改该承运商下的车辆与司机绑定关系
     * @param dto
     * @return
     */
    ResultVo modifyVehicle(ModifyMyCarDto dto);

    /**
     * 修改承运商下的司机与车辆绑定关系
     * @param dto
     * @return
     */
    ResultVo modifyDriver(ModifyMyDriverDto dto);
}
