package com.cjyc.common.system.service.sys;

import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.defined.BizScope;

/**
 * fegin接口传参和返回值处理接口
 * @author JPG
 */
public interface ICsSysService {
    BizScope getBizScopeByRoleId(Long roleId, boolean isSearchCache);

    BizScope getBizScopeByLoginId(Long loginId, boolean isSearchCache);

    Carrier getCarrierByRoleId(Long roleId);
}
