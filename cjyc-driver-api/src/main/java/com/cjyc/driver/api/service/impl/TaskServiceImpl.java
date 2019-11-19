package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.FinishTaskVo;
import com.cjyc.driver.api.service.ITaskService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务表(子运单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-19
 */
@Service
public class TaskServiceImpl extends ServiceImpl<ITaskDao, Task> implements ITaskService {

    @Override
    public ResultVo<PageInfo<FinishTaskVo>> getPage(BaseDriverDto dto) {

        
        return null;
    }
}
