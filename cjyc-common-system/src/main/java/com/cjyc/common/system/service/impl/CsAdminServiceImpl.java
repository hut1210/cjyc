package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.vo.web.admin.CacheAdminVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.common.system.util.ResultDataUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
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
    private ICsSysService csSysService;
    @Resource
    private ISysDeptService sysDeptService;
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
        ResultData<List<SelectUsersByRoleResp>> resultData = sysDeptService.getUsersByDeptId(store.getDeptId());
        if(ResultDataUtil.isEmpty(resultData)){
            return null;
        }
        Set<Long> userIds = resultData.getData().stream().map(SelectUsersByRoleResp::getUserId).collect(Collectors.toSet());
        List<Admin> admins = adminDao.findListByUserIds(userIds);
        return admins;
    }

    @Override
    public CacheAdminVo getCacheData(Long userId, Long roleId) {
        CacheAdminVo cacheAdminVo = new CacheAdminVo();
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null) {
            return null;
        }
        BeanUtils.copyProperties(admin, cacheAdminVo);

        return cacheAdminVo;
    }
}
