package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cjyc.common.model.entity.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典配置表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IDictionaryDao extends BaseMapper<Dictionary> {

    Dictionary findByItemKey(@Param("item") String item, @Param("key") String key);

    /**
     * 获取系统配置
     * @return
     */
    List<Map<String,String>> getSystemConfig(@Param("item") String item);
}
