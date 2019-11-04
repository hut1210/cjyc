package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 * 任务表(子运单) 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-26
 */
public interface ITaskService extends IService<Task> {

    ResultVo allot(AllotTaskDto paramsDto);

    ResultVo load(LoadTaskDto paramsDto);

    ResultVo unload(UnLoadTaskDto paramsDto);

    ResultVo inStore(InStoreTaskDto paramsDto);

    ResultVo outStore(OutStoreTaskDto paramsDto);

    ResultVo sign(SignTaskDto paramsDto);
}
