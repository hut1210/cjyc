package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Admin;

public interface ICsAdminService {
    /**
     *
     * @author JPG
     * @since 2019/11/5 14:29
     * @param userId
     */
    Admin getByUserId(Long userId, Boolean isSearchCache);
}
