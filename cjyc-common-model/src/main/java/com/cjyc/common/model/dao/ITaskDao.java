package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.driver.FinishTaskQueryDto;
import com.cjyc.common.model.dto.driver.NoFinishTaskQueryDto;
import com.cjyc.common.model.dto.web.task.CrTaskDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.vo.driver.task.WaybillTaskVo;
import com.cjyc.common.model.vo.web.task.CrTaskVo;
import com.cjyc.common.model.vo.web.task.ListByWaybillTaskVo;
import com.cjyc.common.model.vo.web.task.TaskVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务表(子运单) Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ITaskDao extends BaseMapper<Task> {

    /**
     * 根据运单ID更新任务状态
     * @author JPG
     * @since 2019/10/21 8:55
     * @param newState
     * @param cancelWaybillIdList
     */
    int updateListByWaybillIds(@Param("newState") int newState, @Param("cancelWaybillIdList") List<Long> cancelWaybillIdList);

    int cancelBywaybillId(@Param("newState") int newState, @Param("waybillId")Long waybillId);

    List<ListByWaybillTaskVo> findListByWaybillId(Long waybillId);

    TaskVo findVoById(Long taskId);

    int deleteByWaybillId(Long waybillId);

    Task findTopByWaybillId(Long id);

    int deleteByWaybillCarId(Long waybillCarId);

    String findMaxNo(String waybillNo);

    List<CrTaskVo> findListForMineCarrier(CrTaskDto paramsDto);

    /**
     * 功能描述: 查询已交付任务列表
     * @author liuxingxiang
     * @date 2019/11/19
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.WaybillTaskVo>
     */
    List<WaybillTaskVo> selectFinishTaskPage(FinishTaskQueryDto dto);

    /**
     * 功能描述: 查询提车，交车任务列表
     * @author liuxingxiang
     * @date 2019/11/20
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.WaybillTaskVo>
     */
    List<WaybillTaskVo> selectNoFinishTaskPage(NoFinishTaskQueryDto dto);
}
