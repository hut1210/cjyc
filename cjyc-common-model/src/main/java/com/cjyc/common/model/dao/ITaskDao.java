package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.driver.task.NoFinishTaskQueryDto;
import com.cjyc.common.model.dto.driver.task.TaskQueryDto;
import com.cjyc.common.model.dto.salesman.BaseSalesDto;
import com.cjyc.common.model.dto.salesman.mine.OrderCarDto;
import com.cjyc.common.model.dto.salesman.task.OutAndInStorageQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskWaybillQueryDto;
import com.cjyc.common.model.dto.web.task.CrTaskDto;
import com.cjyc.common.model.dto.web.task.TaskPageDto;
import com.cjyc.common.model.entity.Task;
import com.cjyc.common.model.vo.driver.task.TaskBillVo;
import com.cjyc.common.model.vo.salesman.mine.StockTaskVo;
import com.cjyc.common.model.vo.salesman.task.TaskWaybillVo;
import com.cjyc.common.model.vo.web.task.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
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

    int cancelBywaybillId(@Param("waybillId")Long waybillId);

    List<ListByWaybillTaskVo> findListByWaybillId(Long waybillId);

    TaskVo findVoById(Long taskId);

    int deleteByWaybillId(Long waybillId);

    Task findTopByWaybillId(Long id);

    int deleteByWaybillCarId(Long waybillCarId);

    String findMaxNo(String waybillNo);

    List<CrTaskVo> findListForMineCarrier(@Param("paramsDto") CrTaskDto paramsDto);

    /**
     * 功能描述: 查询已交付任务列表
     * @author liuxingxiang
     * @date 2019/11/19
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.TaskBillVo>
     */
    List<TaskBillVo> selectFinishTaskPage(TaskQueryDto dto);

    /**
     * 功能描述: 查询提车，交车任务列表
     * @author liuxingxiang
     * @date 2019/11/20
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.TaskBillVo>
     */
    List<TaskBillVo> selectNoFinishTaskPage(NoFinishTaskQueryDto dto);

    /**
     * 功能描述: 查询历史任务记录列表
     * @author liuxingxiang
     * @date 2019/11/21
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.driver.task.TaskBillVo>
     */
    List<TaskBillVo> selectHistoryTaskPage(TaskQueryDto dto);

    int updateUnloadNum(@Param("taskId") Long taskId, @Param("count")int count);

    int updateStateById(@Param("taskId") Long taskId, @Param("state") int state);

    int countByTaskIdAndWaybillCarId(@Param("taskId") Long taskId, @Param("waybillCarId") Long waybillCarId);

    Task findByWaybillCarId(Long waybillCarId);

    List<Task> findListByWaybillCarIds(@Param("collection") Collection<Long> waybillCarIds);

    /**
     * 功能描述: 查询待提车，待交车列表
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.salesman.task.TaskBillVo>
     */
    List<TaskWaybillVo> selectCarryList(TaskWaybillQueryDto dto);

    /**
     * 查询库存提干送详情
     * @param dto
     * @return
     */
    List<StockTaskVo> findListStockTask(OrderCarDto dto);

    /**
     * 功能描述: 查询出入库,出入库历史记录列表
     * @author liuxingxiang
     * @date 2019/12/10
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.salesman.task.TaskWaybillVo>
     */
    List<TaskWaybillVo> selectOutAndInStorageList(OutAndInStorageQueryDto dto);

    List<DriverCarCountVo> findDriverCarCount(@Param("beforeStartDay") Long beforeStartDay,@Param("beforeEndDay") Long beforeEndDay);

    int updateForOutStore(Long id);

    /**
     * 功能描述: 查询我的任务列表
     * @author liuxingxiang
     * @date 2019/12/20
     * @param dto
     * @return java.util.List<com.cjyc.common.model.vo.web.task.TaskPageVo>
     */
    List<TaskPageVo> selectMyTaskList(TaskPageDto dto);

    int updateForFinish(Long id);

    int updateNum(@Param("taskId") Long taskId);

    int updateLoadNum(@Param("taskId") Long taskId, @Param("count") int count);

    Task findByWcCarId(Long id);

    int updateForCancel(Long id);

    int updateForOver(@Param("id") Long id, @Param("state") Integer state);
}
