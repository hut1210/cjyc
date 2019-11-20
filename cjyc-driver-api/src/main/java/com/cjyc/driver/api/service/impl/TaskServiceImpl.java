package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dao.IWaybillDao;
import com.cjyc.common.model.dto.driver.BaseConditionDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.FinishTaskVo;
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

    @Override
    public ResultVo<PageInfo<FinishTaskVo>> getPage(BaseConditionDto dto) {
        // 查询当前登录人已完成任务列表
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        dto.setState(TaskStateEnum.FINISHED.code);
        /*StringBuilder sb = null;
        if (!StringUtils.isEmpty(dto.getLineStart())) {
            sb = new StringBuilder();
            sb.append(dto.getLineStart());
        }
        if (!StringUtils.isEmpty(dto.getLineEnd())) {
            if (sb == null) {
                sb = new StringBuilder();
                sb.append(dto.getLineEnd());
            } else {
                sb.append("-");
                sb.append(dto.getLineEnd());
            }
        }
        if (sb != null) {
            dto.setGuideLine(sb.toString());
        }*/

        List<FinishTaskVo> taskList = taskDao.selectPage(dto);
        PageInfo pageInfo = new PageInfo(taskList);
        return BaseResultUtil.success(pageInfo);
    }
}
