package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IUserRoleDeptDao;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.UserRoleDept;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsUserRoleDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class CsUserRoleDeptServiceImpl implements ICsUserRoleDeptService {

    @Resource
    private IUserRoleDeptDao userRoleDeptDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo saveCustomerToUserRoleDept(Customer customer, Long roleId, Long loginId) {
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
        return BaseResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateCustomerToUserRoleDept(Customer customer,Long loginId) {
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, customer.getId())
                .eq(UserRoleDept::getDeptType, DeptTypeEnum.CUSTOMER.code)
                .eq(UserRoleDept::getUserType, UserTypeEnum.CUSTOMER.code));
        if(urd == null){
            return BaseResultUtil.fail("该用户不存在，请联系管理员");
        }
        if(customer.getType() != CustomerTypeEnum.INDIVIDUAL.code){
            urd.setState(CommonStateEnum.WAIT_CHECK.code);
        }else if(customer.getType() == CustomerTypeEnum.INDIVIDUAL.code && customer.getSource() == CustomerSourceEnum.UPGRADE.code){
            urd.setState(CommonStateEnum.IN_CHECK.code);
        }
        urd.setUpdateUserId(loginId);
        urd.setUpdateTime(NOW);
        userRoleDeptDao.updateById(urd);
        return BaseResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo<UserRoleDept> saveDriverToUserRoleDept(Carrier carrier, Driver driver,Integer mode, Long roleId, Long loginId,Integer flag) {
        UserRoleDept urd = new UserRoleDept();
        urd.setUserId(driver.getId());
        urd.setRoleId(roleId);
        urd.setDeptId(carrier.getId()+"");
        urd.setDeptType(DeptTypeEnum.CARRIER.code);
        urd.setUserType(UserTypeEnum.DRIVER.code);
        if(flag == 0){
            urd.setState(CommonStateEnum.CHECKED.code);
            urd.setMode(mode);
        }else{
            urd.setState(CommonStateEnum.IN_CHECK.code);
            urd.setMode(carrier.getMode());
        }
        urd.setCreateTime(NOW);
        urd.setCreateUserId(loginId);
        userRoleDeptDao.insert(urd);
        return BaseResultUtil.success(urd);
    }

    @Override
    public ResultVo updateDriverToUserRoleDept(Carrier carrier, Driver driver,Integer mode, Long loginId,Integer flag) {
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, driver.getId())
                .eq(UserRoleDept::getDeptId, carrier.getId())
                .eq(UserRoleDept::getDeptType, DeptTypeEnum.CARRIER.code)
                .eq(UserRoleDept::getUserType, UserTypeEnum.DRIVER.code));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在，请联系管理员");
        }
        urd.setMode(carrier.getMode());
        if(flag == 0){
            urd.setState(CommonStateEnum.CHECKED.code);
            urd.setMode(mode);
        }else{
            urd.setState(CommonStateEnum.WAIT_CHECK.code);
            urd.setMode(carrier.getMode());
        }
        urd.setUpdateUserId(loginId);
        urd.setUpdateTime(NOW);
        userRoleDeptDao.updateById(urd);
        return BaseResultUtil.success();
    }
}