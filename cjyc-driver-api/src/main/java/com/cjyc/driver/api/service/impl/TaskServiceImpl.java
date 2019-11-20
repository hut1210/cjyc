package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IWaybillDao;
import com.cjyc.common.model.dto.driver.BaseConditionDto;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.DetailQueryDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.driver.task.WaybillTaskVo;
import com.cjyc.driver.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private IWaybillDao waybillDao;
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IDriverDao driverDao;
    @Autowired
    private ICarrierDriverConDao carrierDriverConDao;

    @Override
    public ResultVo<PageInfo<WaybillTaskVo>> getWaitHandleTaskPage(BaseDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = waybillDao.selectWaitHandleTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageInfo<WaybillTaskVo>> getNoFinishTaskPage(BaseDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = taskDao.selectNoFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageInfo<WaybillTaskVo>> getFinishTaskPage(BaseConditionDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<WaybillTaskVo> taskList = taskDao.selectFinishTaskPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageInfo<TaskDetailVo>> getDetail(DetailQueryDto dto) {

        return null;
    }
}
