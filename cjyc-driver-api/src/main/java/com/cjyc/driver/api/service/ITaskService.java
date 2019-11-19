package com.cjyc.driver.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.driver.BaseConditionDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.FinishTaskVo;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 任务表(子运单) 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-11-19
 */
public interface ITaskService extends IService<Task> {

    /**
     * 功能描述: 分页查询首页已交付任务单列表
     * @author liuxingxiang
     * @date 2019/11/19
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.github.pagehelper.PageInfo<com.cjyc.common.model.vo.driver.task.FinishTaskVo>>
     */
    ResultVo<PageInfo<FinishTaskVo>> getPage(BaseConditionDto dto);
}
