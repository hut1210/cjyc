package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.IRoleDao;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.role.RoleRangeEnum;
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
    public List<Role> getValidInnerRoleList() {
        return roleDao.selectList(new QueryWrapper<Role>()
            .eq("role_range", RoleRangeEnum.INNER.getValue())
            .eq("state", 1));
    }

    @Override
    public Role getByName(String roleName, Integer roleType) {
        return roleDao.selectOne(new QueryWrapper<Role>()
                .eq("role_name", roleName)
                .eq("role_range", roleType)
                .eq("state", UseStateEnum.USABLE.code));
    }
}
