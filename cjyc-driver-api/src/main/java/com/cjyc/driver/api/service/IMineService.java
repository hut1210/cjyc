package com.cjyc.driver.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.driver.mine.DeleteVehicleDto;
import com.cjyc.common.model.dto.driver.mine.PersonDriverDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.BaseDto;
import com.cjyc.common.model.dto.driver.mine.FrozenDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BinkCardVo;
import com.cjyc.common.model.vo.driver.mine.DriverInfoVo;

import java.util.List;

public interface IMineService extends IService<Driver> {

    /**
     * 根据司机id获取绑定银行卡信息
     * @param dto
     * @return
     */
    ResultVo<List<BinkCardVo>> findBinkCard(BaseDto dto);

    /**
     * 查询司机管理信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<DriverInfoVo>> findDriver(BaseDriverDto dto);

    /**
     * 司机管理中冻结(删除)司机
     * @param dto
     * @return
     */
    ResultVo frozenDriver(FrozenDto dto);

    /**
     * 个人司机新增修改车辆信息
     * @param dto
     * @return
     */
    ResultVo addOrModifyVehicle(PersonDriverDto dto);

    /**
     * 删除个人司机车辆信息
     * @param dto
     * @return
     */
    ResultVo deleteVehicle(DeleteVehicleDto dto);

    /**
     * 查看车辆信息
     * @param dto
     * @return
     */
    ResultVo findVehicle(BaseDriverDto dto);


}
