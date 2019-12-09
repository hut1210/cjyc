package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dto.salesman.task.TaskQueryConditionDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.task.TaskBillVo;
import com.cjyc.salesman.api.service.ITaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 任务业务接口实现类
 * @Author Liu Xing Xiang
 * @Date 2019/12/9 11:32
 **/
@Service
public class TaskServiceImpl implements ITaskService {
    @Autowired
    private ITaskDao taskDao;

    @Override
    public ResultVo<PageVo<TaskBillVo>> getCarryPage(TaskQueryConditionDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<TaskBillVo> list = taskDao.selectCarryList(dto);
        PageInfo<TaskBillVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }
}
