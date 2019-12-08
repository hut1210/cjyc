package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsUserServiceImpl implements ICsUserService {

    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsCustomerService csCustomerService;

    @Override
    public UserInfo getUserInfo(Long loginId, Integer loginType) {

        UserInfo userInfo = new UserInfo();
        if(UserTypeEnum.DRIVER.code == loginType){
            Driver driver = csDriverService.getById(loginId, true);
            if(driver != null){
                userInfo.setUserType(UserTypeEnum.DRIVER);
                userInfo.setId(driver.getId());
                userInfo.setName(driver.getName());
                userInfo.setPhone(driver.getPhone());
            }
        }else if(UserTypeEnum.CUSTOMER.code == loginType){
            Customer customer = csCustomerService.getById(loginId, true);
            if(customer != null){
                userInfo.setUserType(UserTypeEnum.DRIVER);
                userInfo.setId(customer.getId());
                userInfo.setName(customer.getName());
                userInfo.setPhone(customer.getContactPhone());
            }
        }else{
            Admin admin = csAdminService.getById(loginId, true);
            if(admin != null){
                userInfo.setUserType(UserTypeEnum.DRIVER);
                userInfo.setId(admin.getId());
                userInfo.setName(admin.getName());
                userInfo.setPhone(admin.getName());
            }
        }
        return userInfo;
    }
}
