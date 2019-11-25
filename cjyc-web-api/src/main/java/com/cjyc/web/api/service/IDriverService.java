package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.BaseCarrierIdDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ExistDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;

import java.util.List;

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
    ResultVo saveOrModifyDriver(DriverDto dto);

    /**
     * 根据条件查询司机信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<DriverVo>> findDriver(SelectDriverDto dto);

    /**
     * 根据司机userId进行审核
     * @param dto
     * @return
     */
    ResultVo verifyDriver(OperateDto dto);

    /**
     * 根据承运商id查看司机信息
     * @param dto
     * @return
     */
    ResultVo showDriver(BaseCarrierIdDto dto);

    /**
     * app校验注册记录
     * @return
     */
    ResultVo<List<ExistDriverVo>> showExistDriver();

    Driver getByUserId(Long userId);

    /**
     * 冻结/解除司机状态
     * @param id
     * @param flag
     * @return
     */
    ResultVo resetState(Long id, Integer flag);

    /**
     * 调度个人司机
     * @param dto
     * @return
     */
    ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(DispatchDriverDto dto);

    Driver getById(Long id, boolean isSearchCache);
}
