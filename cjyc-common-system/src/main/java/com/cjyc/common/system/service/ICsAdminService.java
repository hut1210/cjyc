package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.model.vo.web.admin.CacheData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICsAdminService {
    /**
     *
     * @author JPG
     * @since 2019/11/5 14:29
     * @param adminId
     */
    Admin getByUserId(Long adminId, Boolean isSearchCache);

    /**
     * 根据业务中心查询所有业务员s
     * @author JPG
     * @since 2019/11/5 14:43
     * @param storeId
     */
    List<Admin> getListByStoreId(Long storeId);

    AdminVo getByPhone(String phone, boolean isSearchCache);

    Admin getById(Long adminId, boolean isSearchCache);

    Admin validate(Long adminId);

    /**
     * 仅查询业务员信息
     * @param phone
     * @param isSearchCache
     * @return
     */
    Admin getAdminByPhone(String phone, boolean isSearchCache);

    Admin findLoop(Long startStoreId);

    Map<Long, Admin> findLoops(Collection<Long> storeIds);
}
