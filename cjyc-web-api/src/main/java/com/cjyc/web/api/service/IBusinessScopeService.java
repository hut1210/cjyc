package com.cjyc.web.api.service;

import com.cjyc.common.model.vo.BusinessScopeDto;

public interface IBusinessScopeService {

    /**
     * 获取用户业务范围
     * @author JPG
     * @since 2019/10/15 16:38
     * @param
     */
    BusinessScopeDto getBusinessScope(Long userId);

}
