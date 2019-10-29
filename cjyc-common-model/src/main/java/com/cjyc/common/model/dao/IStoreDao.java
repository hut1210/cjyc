package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 韵车业务中心信息表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IStoreDao extends BaseMapper<Store> {


    List<Store> findByCityCode(String cityCode);
}
