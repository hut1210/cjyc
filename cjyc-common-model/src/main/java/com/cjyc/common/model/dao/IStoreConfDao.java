package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.StoreConf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 业务中心配置表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IStoreConfDao extends BaseMapper<StoreConf> {

    Integer getSwitch(@Param("key") String key);

    void addSwitch(@Param("key")String key,@Param("value")Integer value);

    void updateSwitch(@Param("key")String key,@Param("value")Integer value);
}
