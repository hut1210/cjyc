package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.BusinessCityCode;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.github.pagehelper.PageInfo;

public interface IDriverService {
    /**
     * 查询司机列表
     * @author JPG
     * @since 2019/10/16 16:15
     */
    ResultVo<PageVo<DriverListVo>> lineWaitDispatchCarCountList(DriverListDto paramsDto);

    /**
     * 保存散户司机
     * @param dto
     * @return
     */
    boolean saveDriver(DriverDto dto);

    /**
     * 根据条件查询司机信息
     * @param dto
     * @return
     */
    ResultVo findDriver(SelectDriverDto dto);

    /**
     * 根据司机userId进行审核
     * @param dto
     * @return
     */
    boolean verifyDriver(OperateDto dto);

    /**
     * 根据司机id/userId查看司机信息
     * @param id
     * @param userId
     * @return
     */
    ResultVo showDriver(Long id, Long userId);

    /**
     * 根据司机id更新司机信息
     * @param dto
     * @return
     */
    boolean modifyDriver(DriverDto dto);

    Driver getByUserId(Long userId);

    /**
     * 冻结/解除司机状态
     * @param id
     * @param flag
     * @return
     */
    ResultVo resetState(Long id, Integer flag);

}
