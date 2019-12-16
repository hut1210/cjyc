package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.model.vo.web.admin.CacheData;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.common.system.util.ResultDataUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 业务员公用业务
 *
 * @author JPG
 */
@Service
public class CsAdminServiceImpl implements ICsAdminService {
    @Resource
    private IAdminDao adminDao;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private StringRedisUtil redisUtil;
    /**
     * @param userId
     * @param isSearchCache
     * @author JPG
     * @since 2019/11/5 14:29
     */
    @Override
    public Admin getByUserId(Long userId, Boolean isSearchCache) {
        return adminDao.findByUserId(userId);
    }

    /**
     * 根据业务中心查询所有业务员
     *
     * @param storeId
     * @author JPG
     * @since 2019/11/5 14:43
     */
    @Override
    public List<Admin> getListByStoreId(Long storeId) {
        Store store = csStoreService.getById(storeId, true);
        if(store == null){
            return null;
        }
        ResultData<List<SelectUsersByRoleResp>> resultData = sysDeptService.getUsersByDeptId(store.getDeptId());
        if(ResultDataUtil.isEmpty(resultData)){
            return null;
        }
        Set<Long> userIds = resultData.getData().stream().map(SelectUsersByRoleResp::getUserId).collect(Collectors.toSet());
        List<Admin> admins = adminDao.findListByUserIds(userIds);
        return admins;
    }


    @Override
    public AdminVo getByPhone(String username, boolean isSearchCache) {
        AdminVo vo = adminDao.findVoByPhone(username);
        if(vo == null){
            return null;
        }
        ResultData<UserResp> resultData = sysUserService.getByAccount(username);
        if(resultData == null || resultData.getData() == null){
            return null;
        }
        vo.setDeptId(resultData.getData().getDeptId());
        return vo;
    }


    @Override
    public Admin getById(Long userId, boolean isSearchCache) {
        return adminDao.selectById(userId);
    }

    @Override
    public Admin validate(Long adminId) {
        Admin admin = getById(adminId, true);
        if(admin == null || AdminStateEnum.CHECKED.code != admin.getState()){
            throw new ParameterException("用户不存在或者已离职");
        }
        return admin;
    }

    @Override
    public Admin getAdminByPhone(String phone, boolean isSearchCache) {
        return adminDao.findByPhone(phone);
    }

    @Override
    public Admin findLoop(Long startStoreId) {
        Admin admin = adminDao.selectById(1L);
        return admin;
    }
}
