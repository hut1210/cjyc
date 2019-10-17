package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;

public interface IDriverService {
    /**
     * 查询司机列表
     * @author JPG
     * @since 2019/10/16 16:15
     */
    ResultVo<PageVo<DriverListVo>> lineWaitDispatchCarCountList(DriverListDto paramsDto);
}
