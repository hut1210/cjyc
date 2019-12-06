package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IRoleDao;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.system.service.ICsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通用角色接口
 */
@Service
public class CsRoleServiceImpl implements ICsRoleService {
    @Resource
    private IRoleDao roleDao;


    @Override
    public List<Role> getRoleList() {
        return roleDao.selectList(new QueryWrapper<Role>());
    }
}