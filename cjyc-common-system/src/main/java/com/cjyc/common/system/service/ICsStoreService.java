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
}
