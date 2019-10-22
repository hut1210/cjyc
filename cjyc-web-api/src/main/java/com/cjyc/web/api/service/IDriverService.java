package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
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
    PageInfo<DriverVo> getDriverByTerm(SelectDriverDto dto);
}
