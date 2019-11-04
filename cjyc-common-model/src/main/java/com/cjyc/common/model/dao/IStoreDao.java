package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.Store;

import java.util.List;

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

    List<Store> findByAreaCode(String areaCode);

    List<String> findAreaBizScope(Long id);

    List<Store> findBelongByAreaCode(String areaCode);

    Store findOneBelongByAreaCode(String areaCode);
}
