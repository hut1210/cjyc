package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dto.BaseLoginDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.AdminVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.sys.ICsSysService;
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
    private ISysUserService sysUserService;
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private ICsSysService csSysService;
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
    public <T extends BaseLoginDto> ResultVo<T> validateEnabled(T t){
        if(t == null || t.getLoginId() == null){
            return BaseResultUtil.fail("用户不存在");
        }
        Admin admin = getById(t.getLoginId(), true);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        if(AdminStateEnum.CHECKED.code != admin.getState()){
            return BaseResultUtil.fail("用户不可用");
        }
        t.setLoginName(admin.getName());
        t.setLoginPhone(admin.getPhone());
        t.setLoginType(UserTypeEnum.ADMIN);
        return BaseResultUtil.success(t);
    }
    @Override
    public <T extends BaseLoginDto> ResultVo<T> validateBizscope(T t){
        //验证业务范围
        BizScope bizScope = csSysService.getBizScopeByLoginIdNew(t.getLoginId(), true);
        if (BizScopeEnum.NONE.code == bizScope.getCode()) {
            return BaseResultUtil.fail("用户权限不足");
        }
        t.setBizScope(bizScope.getStoreIds());
        return BaseResultUtil.success(t);
    }
    @Override
    public <T extends BaseLoginDto> ResultVo<T> validateEnabledAndBizscope(T t){
        ResultVo<T> resVo1 = validateEnabled(t);
        if(ResultEnum.SUCCESS.getCode() != resVo1.getCode()){
            return BaseResultUtil.fail(resVo1.getMsg());
        }

        ResultVo<T> resVo2 = validateBizscope(resVo1.getData());
        if(ResultEnum.SUCCESS.getCode() != resVo2.getCode()){
            return BaseResultUtil.fail(resVo2.getMsg());
        }
        return BaseResultUtil.success(resVo2.getData());
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
