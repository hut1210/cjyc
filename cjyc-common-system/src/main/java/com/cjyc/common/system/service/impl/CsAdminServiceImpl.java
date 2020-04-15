package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dto.BaseLoginDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    private ICsStoreService storeService;
    @Resource
    private RedisUtils redisUtil;
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
//        Store store = csStoreService.getById(storeId, true);
//        if(store == null){
//            return null;
//        }
//        ResultData<List<SelectUsersByRoleResp>> resultData = sysDeptService.getUsersByDeptId(store.getDeptId());
//        if(ResultDataUtil.isEmpty(resultData)){
//            return null;
//        }
//        Set<Long> userIds = resultData.getData().stream().map(SelectUsersByRoleResp::getUserId).collect(Collectors.toSet());
//        List<Admin> admins = adminDao.findListByUserIds(userIds);
//        return admins;
        return adminDao.findListByStoreId(storeId);
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
    public <T extends BaseLoginDto> T validateForFill(T t){
        if(t == null || t.getLoginId() == null){
            return t;
        }
        Admin admin = getById(t.getLoginId(), true);
        if(admin == null || AdminStateEnum.CHECKED.code != admin.getState()){
            throw new ParameterException("用户不存在或者已离职");
        }
        t.setLoginName(admin.getName());
        t.setLoginPhone(admin.getPhone());
        t.setLoginType(UserTypeEnum.ADMIN);
        return t;
    }
    @Override
    public Admin getAdminByPhone(String phone, boolean isSearchCache) {
        return adminDao.findByPhone(phone);
    }

    @Override
    public Admin findLoop(Long storeId) {
        if(storeId == null || storeId <= 0){
            return null;
        }
        String key = RedisKeys.getLoopAllotAdminKey(storeId);
        String value = redisUtil.get(key);
        Long cachedId  = StringUtils.isBlank(value) ? 0L : Long.valueOf(value);
        List<Admin> admins = adminDao.findListByStoreId(storeId);
        if(CollectionUtils.isEmpty(admins)){
            return null;
        }
        Admin selectAdmin = null;
        for (Admin admin: admins) {
            if(admin.getId() > cachedId){
                selectAdmin = admin;
            }
        }
        if(selectAdmin == null){
            selectAdmin = admins.get(0);
        }
        redisUtil.set(key, String.valueOf(selectAdmin.getId()));
        return selectAdmin;
    }

    @Override
    public Map<Long, Admin> findLoops(Collection<Long> storeIds) {
        Map<Long, Admin> map = Maps.newHashMap();
        if(CollectionUtils.isEmpty(storeIds)){
            return null;
        }
        for (Long storeId : storeIds) {
            Admin loop = findLoop(storeId);
            if(loop != null){
                map.put(storeId, loop);
            }
        }
        return map;
    }

    @Override
    public Admin findAdminByPhone(String phone) {
        return adminDao.findAdminByPhone(phone);
    }
}
