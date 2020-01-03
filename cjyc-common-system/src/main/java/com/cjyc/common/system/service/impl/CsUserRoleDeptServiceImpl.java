package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IUserRoleDeptDao;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.UserRoleDept;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.system.service.ICsUserRoleDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class CsUserRoleDeptServiceImpl implements ICsUserRoleDeptService {

    @Resource
    private IUserRoleDeptDao userRoleDeptDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public void saveCustomerToUserRoleDept(Customer customer, Long roleId,Long loginId) {
        UserRoleDept urd = new UserRoleDept();
        urd.setUserId(customer.getId());
        urd.setRoleId(roleId);
        urd.setDeptType(DeptTypeEnum.CUSTOMER.code);
        urd.setUserType(UserTypeEnum.CUSTOMER.code);
        if(customer.getType() == CustomerTypeEnum.INDIVIDUAL.code){
            urd.setState(CommonStateEnum.CHECKED.code);
        }else{
            urd.setState(CommonStateEnum.WAIT_CHECK.code);
        }
        urd.setCreateTime(NOW);
        urd.setCreateUserId(loginId);
        userRoleDeptDao.insert(urd);
    }

    @Override
    public void updateCustomerToUserRoleDept(Customer customer,Long loginId) {
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, customer.getId())
                .eq(UserRoleDept::getDeptType, DeptTypeEnum.CUSTOMER.code)
                .eq(UserRoleDept::getUserType, UserTypeEnum.CUSTOMER.code));
        if(customer.getType() != CustomerTypeEnum.INDIVIDUAL.code){
            urd.setState(CommonStateEnum.WAIT_CHECK.code);
        }
        urd.setUpdateUserId(loginId);
        urd.setUpdateTime(NOW);
        userRoleDeptDao.updateById(urd);
    }
}