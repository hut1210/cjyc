package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.UserRoleDept;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsUserRoleDeptService {

    /**
     * 保存用户到用户角色机构关系表中
     * @param customer
     * @param roleId
     */
    ResultVo saveCustomerToUserRoleDept(Customer customer, Long roleId, Long loginId);

    /**
     * 修改用戶更新关系表
     * @param customer
     * @param loginId
     */
    ResultVo updateCustomerToUserRoleDept(Customer customer,Long loginId);

    /**
     * 保存司机到用户角色机构关系表中
     * @param carrier
     * @param driver
     * @param roleId
     * @param loginId
     * @param flag 承运商下司机:0  个人司机:1
     */
    ResultVo<UserRoleDept> saveDriverToUserRoleDept(Carrier carrier, Driver driver,Integer mode, Long roleId, Long loginId,Integer flag);

    /**
     * 修改司机更新关系表
     * @param carrier
     * @param driver
     * @param loginId
     * @param flag 承运商下司机:0  个人司机:1
     */
    ResultVo updateDriverToUserRoleDept(Carrier carrier,Driver driver,Integer mode,Long loginId,Integer flag);
}
