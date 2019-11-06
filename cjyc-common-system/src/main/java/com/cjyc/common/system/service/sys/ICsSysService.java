package com.cjyc.common.system.service.sys;

import java.util.List;

/**
 * fegin接口传参和返回值处理接口
 * @author JPG
 */
public interface ICsSysService {
    List<Long> getStoreIdsByDeptId(Long deptId);
}
