package com.cjyc.salesman.api.service;

import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskQueryConditionDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.salesman.task.TaskWaybillVo;

/**
 * @Description 任务业务接口
 * @Author Liu Xing Xiang
 * @Date 2019/12/9 11:31
 **/
public interface ITaskService {
    /**
     * 功能描述: 根据条件查询提送车列表
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.salesman.task.TaskBillVo>
     */
    ResultVo<PageVo<TaskWaybillVo>> getCarryPage(TaskQueryConditionDto dto);

    /**
     * 功能描述: 提送车任务详情查询
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.driver.task.TaskDetailVo>
     */
    ResultVo<TaskDetailVo> getCarryDetail(DetailQueryDto dto);
}
