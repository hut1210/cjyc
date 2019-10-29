package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
public interface IConfigDao extends BaseMapper<Config> {

    /**
     *  查询所有系统配置
     * @return
     */
    List<Config> getAllConfig();

}
