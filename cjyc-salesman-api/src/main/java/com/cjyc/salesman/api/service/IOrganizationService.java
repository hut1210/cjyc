package com.cjyc.salesman.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.Organization;
import com.cjyc.common.model.entity.sys.SysRoleEntity;

import java.util.List;

/**
 * <p>
 * 组织机构，关联架构sys_dept 服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
public interface IOrganizationService extends IService<Organization> {

    List<SysRoleEntity> getSysRoleList(long id);

}
