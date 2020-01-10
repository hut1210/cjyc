package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dao.IUserRoleDeptDao;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.entity.UserRoleDept;
import com.cjyc.common.model.enums.CityLevelEnum;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.store.StoreVo;
import com.cjyc.salesman.api.service.IStoreService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  业务员-业务中心
 */
@Service
public class StoreServiceImpl implements IStoreService {
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
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private ICityDao cityDao;

    @Override
    public ResultVo<List<StoreVo>> listByLoginId(Long loginId) {
        List<UserRoleDept> roleList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>()
                .eq("user_id", loginId)
                .eq("dept_type", UserTypeEnum.ADMIN.code)
                .eq("state", CommonStateEnum.CHECKED.code));
        if (CollectionUtils.isEmpty(roleList)) {
            return BaseResultUtil.success(new ArrayList<>());
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
            return BaseResultUtil.success(convertStoreListToStoreVoList(storeDao.findAll()));
        }
        if (CollectionUtils.isEmpty(map.get(CITY_DEPT_KEY))) {
            List<Store> storeList = storeDao.selectList(new QueryWrapper<Store>().lambda()
                    .in(Store::getId, map.get(STORE_DEPT_KEY)
                            .stream().map(d -> Long.parseLong(d)).collect(Collectors.toList())));
            return BaseResultUtil.success(convertStoreListToStoreVoList(storeList));
        }
        List<City> cityList = cityDao.selectList(new QueryWrapper<City>().lambda()
                .eq(City::getLevel, CityLevelEnum.PROVINCE_LEVEL.getLevel())
                .in(City::getParentCode, map.get(CITY_DEPT_KEY)));
        List<String> cityCodeList = new ArrayList<>();
        cityCodeList.addAll(map.get(CITY_DEPT_KEY));
        if (!CollectionUtils.isEmpty(cityList)) {
            cityList.forEach(c -> {
                cityCodeList.add(c.getCode());
            });
        }
        List<Store> stores = storeDao.selectList(new QueryWrapper<Store>().lambda()
            .eq(Store::getIsDelete, 0)
            .and(wrapper ->
                    wrapper.in(Store::getProvinceCode, cityCodeList).or()
                            .in(Store::getCityCode, cityCodeList).or()
                            .in(!CollectionUtils.isEmpty(map.get(STORE_DEPT_KEY)),
                                    Store::getId, map.get(STORE_DEPT_KEY).stream()
                                            .map(d -> Long.parseLong(d)).collect(Collectors.toList()))
            ).groupBy(Store::getId));
        if (!CollectionUtils.isEmpty(stores)) {
            return BaseResultUtil.success(convertStoreListToStoreVoList(stores));
        }
        return BaseResultUtil.success(new ArrayList<>());
    }

    /**
     * 将Store转为视图
     * @param storeList
     * @return
     */
    private List<StoreVo> convertStoreListToStoreVoList(List<Store> storeList) {
        if (CollectionUtils.isEmpty(storeList)) {
            return new ArrayList<>();
        }
        List<StoreVo> rsList = new ArrayList<>();
        storeList.forEach(s -> {
            StoreVo vo = new StoreVo();
            BeanUtils.copyProperties(s, vo);
            rsList.add(vo);
        });
        return rsList;
    }
}
