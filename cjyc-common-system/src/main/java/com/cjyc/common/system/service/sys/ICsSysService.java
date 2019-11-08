package com.cjyc.common.system.service.sys;

/**
 * fegin接口传参和返回值处理接口
 * @author JPG
 */
public interface ICsSysService {
    String getBizScopeByRoleId(Long roleId, boolean isSearchCache);
}
