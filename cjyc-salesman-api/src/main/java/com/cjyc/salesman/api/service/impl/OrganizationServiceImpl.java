package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjyc.common.model.dao.IOrganizationDao;
import com.cjyc.common.model.dto.sys.SysDeptDto;
import com.cjyc.common.model.entity.Organization;
import com.cjyc.common.model.entity.sys.SysDeptEntity;
import com.cjyc.salesman.api.fegin.IDeptService;
import com.cjyc.salesman.api.service.IOrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 组织机构，关联架构sys_dept 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<IOrganizationDao, Organization> implements IOrganizationService {


}
