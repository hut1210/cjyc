package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Store;

import java.util.List;

/**
 * 公用业务中心类
 * @author JPG
 */
public interface ICsStoreService {
    /**
     * 查询区县所属业务中心-业务范围
     * @author JPG
     * @since 2019/11/5 9:27
     * @param areaCode
     */
    Store findOneBelongByAreaCode(String areaCode);
    /**
     * 查询区县所属业务中心列表-业务范围
     * @author JPG
     * @since 2019/11/5 9:27
     * @param areaCode
     */
    List<Store> findBelongByAreaCode(String areaCode);

    /**
     * 根据ID查询业务中心
     * @author JPG
     * @since 2019/11/5 17:26
     * @param storeId
     * @param isSearchCache
     */
    Store getById(Long storeId, boolean isSearchCache);

    /**
     * 根据ID查询业务中心覆盖范围
     * @param storeId
     * @return
     */
    List<String> findAreaBizScope(Long storeId);

    /**
     * 查询所有业务中心
     * @author JPG
     * @since 2019/11/7 13:21
     * @param
     */
    List<Store> getAll();
    /**
     * 查询所有业务中心dept_id
     * @author JPG
     * @since 2019/11/7 13:21
     * @param
     */
    List<Long> getAllDeptId();

}
