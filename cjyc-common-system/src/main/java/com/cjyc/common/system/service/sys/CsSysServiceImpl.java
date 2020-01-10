package com.cjyc.common.system.service.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarrierVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsStoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * fegin接口传参和返回值处理接口
 *
 * @author JPG
 */
@Service
public class CsSysServiceImpl implements ICsSysService {

    private static final String YC_DEPT_ADMIN_ID = YmlProperty.get("cjkj.dept_admin_id");
    /**
     * 全国城市编码
     */
    private static final String COUNTRY_CODE = "000000";

    /**
     * 业务中心级别机构 存储key
     */
    private static final String STORE_DEPT_KEY = "store";
    /**
     * 地区级别机构（全国、大区、省、市） 存储key
     */
    private static final String CITY_DEPT_KEY = "city";
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICityDao cityDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private IRoleDao roleDao;

    /**
     * 获取角色业务范围: 0全国，-1无业务范围，StoreIds逗号分隔字符串
     *
     * @param roleId
     * @param isSearchCache
     * @author JPG
     * @since 2019/11/7 11:46
     */
    @Override
    public BizScope getBizScopeByRoleId(Long roleId, boolean isSearchCache) {
        BizScope bizScope = new BizScope();
        //默认无权限
        bizScope.setCode(BizScopeEnum.NONE.code);

        Set<Long> set = new HashSet<>();

        //查询角色信息
        ResultData<SelectRoleResp> resultData = sysRoleService.getById(roleId);
        if (resultData == null
                || resultData.getData() == null
                || resultData.getData().getRoleId() == null
                || resultData.getData().getDeptId() == null) {
            return bizScope;
        }
        Long deptId = resultData.getData().getDeptId();
        if (deptId.intValue() == Long.valueOf(YC_DEPT_ADMIN_ID)) {
            //全国权限
            bizScope.setCode(BizScopeEnum.CHINA.code);
            return bizScope;
        }
        //查询当前角色机构下的所有机构
        ResultData<List<SelectDeptResp>> multiLevelResultData = sysDeptService.getMultiLevelDeptList(deptId);
        if (multiLevelResultData == null || CollectionUtils.isEmpty(multiLevelResultData.getData())) {
            return bizScope;
        }
        List<SelectDeptResp> data = multiLevelResultData.getData();
        //获取deptId的Set集合
        Set<Long> collect = data.stream().map(SelectDeptResp::getDeptId).collect(Collectors.toSet());

        //查询全部业务中心ID
        List<Store> storeList = csStoreService.getAll();
        if (storeList == null) {
            return bizScope;
        }
        //计算当前机构下包含的业务中心
        for (Store store : storeList) {
            if (collect.contains(store.getDeptId())) {
                set.add(store.getId());
            }
        }
        if (CollectionUtils.isEmpty(set)) {
            return bizScope;
        }
        bizScope.setCode(BizScopeEnum.STORE.code);
        bizScope.setStoreIds(set);
        return bizScope;
    }

    /**
     * 根据用户ID查询业务范围
     *
     * @param loginId
     * @param isSearchCache
     * @author JPG
     * @since 2019/12/10 18:33
     */
    @Override
    public BizScope getBizScopeByLoginId(Long loginId, boolean isSearchCache) {
        Admin admin = csAdminService.getById(loginId, true);
        //返回实体
        BizScope bizScope = new BizScope();
        //默认无权限
        bizScope.setCode(BizScopeEnum.NONE.code);
        if (admin == null) {
            return bizScope;
        }
        //业务中心ID集合
        Set<Long> set = Sets.newHashSet();
        ResultData<List<SelectRoleResp>> resData = sysRoleService.getListByUserId(admin.getUserId());
        if (resData == null || resData.getData() == null) {
            return bizScope;
        }
        //查询用户角色列表
        List<SelectRoleResp> roleList = resData.getData();
        Set<Long> collect = roleList.stream().map(SelectRoleResp::getDeptId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(collect)) {
            return bizScope;
        }
        if(collect.contains(Long.valueOf(YC_DEPT_ADMIN_ID))){
            //全国权限
            bizScope.setCode(BizScopeEnum.CHINA.code);
            return bizScope;
        }

        //查询机构下所有业务中心
        Set<Long> deptIds = Sets.newHashSet();
        for (Long deptId : collect) {
            ResultData<List<SelectDeptResp>> deptResData = sysDeptService.getMultiLevelDeptList(deptId);
            if(deptResData == null || deptResData.getData() == null){
                continue;
            }
            Set<Long> oneRoleAllLevelDeptIds = deptResData.getData().stream().map(SelectDeptResp::getDeptId).collect(Collectors.toSet());
            deptIds.addAll(oneRoleAllLevelDeptIds);
        }
        if(CollectionUtils.isEmpty(deptIds) ){
            return bizScope;
        }

        //查询全部业务中心ID
        List<Store> storeList = csStoreService.getAll();
        if (storeList == null) {
            return bizScope;
        }
        //计算当前机构下包含的业务中心
        for (Store store : storeList) {
            if (collect.contains(store.getDeptId())) {
                set.add(store.getId());
            }
        }
        if (CollectionUtils.isEmpty(set)) {
            return bizScope;
        }
        bizScope.setCode(BizScopeEnum.STORE.code);
        bizScope.setStoreIds(set);

        return bizScope;
    }

    @Override
    public Carrier getCarrierByRoleId(Long roleId) {
        ResultData<SelectRoleResp> resultData = sysRoleService.getById(roleId);
        if (resultData == null || resultData.getData() == null) {
            return null;
        }
        return carrierDao.findByDeptId(resultData.getData().getDeptId());
    }

    /*********************************韵车集成改版 st*****************************/
    @Override
    public BizScope getBizScopeByRoleIdNew(Long loginId, Long roleId, boolean isSearchCache) {
        List<UserRoleDept> roleList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>()
                .eq("user_id", loginId)
                .eq("role_id", roleId)
                .eq("dept_type", UserTypeEnum.ADMIN.code)
                .eq("state", CommonStateEnum.CHECKED.code));
        return resolveBizScopeByUserRoleDeptList(roleList);
    }

    @Override
    public BizScope getBizScopeByLoginIdNew(Long loginId, boolean isSearchCache) {
        List<UserRoleDept> roleList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>()
                .eq("user_id", loginId)
                .eq("dept_type", UserTypeEnum.ADMIN.code)
                .eq("state", CommonStateEnum.CHECKED.code));
        return resolveBizScopeByUserRoleDeptList(roleList);
    }

    @Override
    public List<MyCarrierVo> getCarriersByRoleId(Long loginId, Long roleId) {
        Role role = roleDao.selectOne(new QueryWrapper<Role>().lambda()
                .eq(Role::getRoleId, roleId));
        roleId = role.getId();
        return carrierDao.getListByLoginIdAndRoleId(loginId, roleId);
    }

    /**
     * 根据用户-角色-机构关系系统获取BizScope
     * @param roleList
     */
    private BizScope resolveBizScopeByUserRoleDeptList(List<UserRoleDept> roleList) {
        BizScope bizScope = new BizScope();
        bizScope.setCode(BizScopeEnum.NONE.code);
        if (CollectionUtils.isEmpty(roleList)) {
            return bizScope;
        }
        //将角色相关机构分类：1 业务中心级别 2 城市级别
        Map<String, List<String>> map = new HashMap<>();
        roleList.forEach(r -> {
            if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == r.getDeptLevel()) {
                if (!map.containsKey(STORE_DEPT_KEY)) {
                    map.put(STORE_DEPT_KEY, Lists.newArrayList(r.getDeptId()));
                }else {
                    map.get(STORE_DEPT_KEY).add(r.getDeptId());
                }
            }else {
                if (!map.containsKey(CITY_DEPT_KEY)) {
                    map.put(CITY_DEPT_KEY, Lists.newArrayList(r.getDeptId()));
                }else {
                    map.get(CITY_DEPT_KEY).add(r.getDeptId());
                }
            }
        });
        if (!CollectionUtils.isEmpty(map.get(CITY_DEPT_KEY)) && map.get(CITY_DEPT_KEY).contains(COUNTRY_CODE)) {
            bizScope.setCode(BizScopeEnum.CHINA.code);
            return bizScope;
        }

        if (CollectionUtils.isEmpty(map.get(CITY_DEPT_KEY))) {
            bizScope.setCode(BizScopeEnum.STORE.code);
            bizScope.setStoreIds(Sets.newHashSet(map.get(STORE_DEPT_KEY)
                    .stream().map(d -> Long.parseLong(d)).collect(Collectors.toList())));
            return bizScope;
        }
        List<City> cityList = cityDao.selectList(new QueryWrapper<City>()
                .eq("level", CityLevelEnum.PROVINCE_LEVEL.getLevel())
                .in("parent_code", map.get(CITY_DEPT_KEY)));
        List<String> cityCodeList = new ArrayList<>();
        cityCodeList.addAll(map.get(CITY_DEPT_KEY));
        if (!CollectionUtils.isEmpty(cityList)) {
            cityList.forEach(c -> {
                cityCodeList.add(c.getCode());
            });
        }
        List<String> storeIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(map.get(STORE_DEPT_KEY))) {
            storeIds.addAll(map.get(STORE_DEPT_KEY));
        }
        List<Store> stores = storeDao.selectList(new QueryWrapper<Store>()
                .eq("is_delete", 0)
                .and(wrapper ->
                        wrapper.in("province_code", cityCodeList).or()
                                .in("city_code", cityCodeList)
                ));
        if (!CollectionUtils.isEmpty(stores)) {
            stores.forEach(s -> {
                storeIds.add(s.getId()+"");
            });
        }
        if (!CollectionUtils.isEmpty(storeIds)) {
            bizScope.setCode(BizScopeEnum.STORE.code);
            Set storeSet = Sets.newHashSet(storeIds
                    .stream().map(s -> Long.parseLong(s)).collect(Collectors.toList()));
            bizScope.setStoreIds(storeSet);
            return bizScope;
        }
        return bizScope;
    }
    /*********************************韵车集成改版 ed*****************************/
}
