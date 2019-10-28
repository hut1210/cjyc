package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.TaskCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务明细表(车辆表) Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ITaskCarDao extends BaseMapper<TaskCar> {

    /**
     * 批量保存
     * @author JPG
     * @since 2019/10/18 14:04
     * @param list
     */
    int saveBatch(@Param("list") List<TaskCar> list);
}
