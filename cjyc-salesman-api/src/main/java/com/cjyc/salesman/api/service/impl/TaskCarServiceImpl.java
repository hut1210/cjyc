package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.TaskCar;
import com.cjyc.common.model.dao.ITaskCarDao;
import com.cjyc.salesman.api.service.ITaskCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务明细表(车辆表) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class TaskCarServiceImpl extends ServiceImpl<ITaskCarDao, TaskCar> implements ITaskCarService {

}
