package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.vo.web.admin.CacheAdminVo;

import java.util.List;

public interface ICsAdminService {
    /**
     *
     * @author JPG
     * @since 2019/11/5 14:29
     * @param userId
     */
    Admin getByUserId(Long userId, Boolean isSearchCache);

    /**
     * 根据业务中心查询所有业务员
     * @author JPG
     * @since 2019/11/5 14:43
     * @param storeId
     */
    List<Admin> getListByStoreId(Long storeId);

    CacheAdminVo getCacheData(Long userId, Integer roleId);
}
