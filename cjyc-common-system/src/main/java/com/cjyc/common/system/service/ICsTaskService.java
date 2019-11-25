package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.dto.web.task.LoadTaskDto;
import com.cjyc.common.model.dto.web.task.UnLoadTaskDto;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsTaskService {
    String getTaskNo(String waybillNo);

    ResultVo allot(AllotTaskDto paramsDto);

    ResultVo load(LoadTaskDto paramsDto);

    ResultVo unload(UnLoadTaskDto paramsDto);
}
