package com.cjyc.driver.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.mine.*;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.*;

public interface IMineService extends IService<Driver> {

    /**
     * 根据司机id获取绑定银行卡信息
     * @param dto
     * @return
     */
    ResultVo<BankCardVos> findBinkCard(AppDriverDto dto);

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
    ResultVo frozenDriver(FrozenDriverDto dto);

    /**
     * 新增修改企业的车辆
     * @param dto
     * @return
     */
    ResultVo saveOrModifyVehicle(AppCarrierVehicleDto dto);

    /**
     * 删除个人司机车辆信息
     * @param dto
     * @return
     */
    ResultVo removeVehicle(RemoveVehicleDto dto);

    /**
     * 查看车辆信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<DriverVehicleVo>> findVehicle(BaseDriverDto dto);

    /**
     * 个人司机实名认证或者修改信息
     * @param dto
     * @return
     */
    ResultVo authOrModifyInfo(SocietyDriverDto dto);

    /**
     * 获取司机状态
     * @param dto
     * @return
     */
    ResultVo<AppDriverInfoVo> findNewDriverInfo(AppDriverDto dto);

    /**
     * 查看认证后的个人司机信息
     * @param dto
     * @return
     */
    ResultVo<SocietyDriverVo> showDriverInfo(AppDriverDto dto);

    /**
     * 个人司机添加银行卡
     * @param dto
     * @return
     */
    ResultVo addBankCard(BankCardDto dto);

    /**
     * 删除银行卡
     * @param dto
     * @return
     */
    ResultVo removeBankCard(RemoveBankCardDto dto);
}
