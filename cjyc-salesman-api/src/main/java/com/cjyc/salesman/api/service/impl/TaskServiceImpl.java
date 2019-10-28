package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.salesman.api.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务表(子运单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class TaskServiceImpl extends ServiceImpl<ITaskDao, Task> implements ITaskService {

}
