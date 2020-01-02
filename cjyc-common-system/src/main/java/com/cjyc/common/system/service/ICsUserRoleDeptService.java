package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Customer;

public interface ICsUserRoleDeptService {

    /**
     * 保存用户到用户角色机构关系表中
     * @param customer
     * @param roleId
     */
    void saveCustomerToUserRoleDept(Customer customer, Long roleId,Long loginId);

    /**
     * 修改用戶更新關係表
     * @param userId
     * @param loginId
     */
    void updateCustomerToUserRoleDept(Long userId,Long loginId);
}
