package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
