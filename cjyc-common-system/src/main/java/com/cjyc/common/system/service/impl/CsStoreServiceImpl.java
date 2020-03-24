package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.IStoreCityConDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dao.IUserRoleDeptDao;
import com.cjyc.common.model.dto.customer.freightBill.FindStoreDto;
import com.cjyc.common.model.dto.salesman.StoreListLoopAdminDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CityLevelEnum;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.BusinessStoreVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;
import com.cjyc.common.model.vo.salesman.store.StoreLoopAdminVo;
import com.cjyc.common.model.vo.salesman.store.StoreVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsStoreService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CsStoreServiceImpl implements ICsStoreService {

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
    private IStoreDao storeDao;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICityDao cityDao;
    @Resource
    private IStoreCityConDao storeCityConDao;
    @Resource
    private ICsAdminService csAdminService;

    /**
     * 查询区县所属业务中心-业务范围
     *
     * @param areaCode
     * @author JPG
     * @since 2019/11/5 9:27
     */
    @Override
    public Store getOneBelongByAreaCode(String areaCode) {
        Store store = storeDao.findByAreaScope(areaCode);
        if(store == null){
            store = storeDao.findOneBelongByAreaCode(areaCode);
        }
        return store;
    }

    /**
     * 根据地址城市查询业务中心
     * @author JPG
     * @since 2019/12/11 8:38
     * @param cityCode
     */
    @Override
    public Store getOneBelongByCityCode(String cityCode) {
        return storeDao.findOneBelongByCityCode(cityCode);
    }

    @Override
    public ResultVo<List<StoreVo>> listByAdminId(Long adminId) {
        List<UserRoleDept> roleList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>()
                .eq("user_id", adminId)
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

    @Override
    public ResultVo<List<StoreLoopAdminVo>> listLoopAdmin(StoreListLoopAdminDto dto) {
        List<Store> stores = storeDao.findList(dto);
        List<StoreLoopAdminVo> list = Lists.newArrayList();
        for (Store store : stores) {
            StoreLoopAdminVo storeLoopAdminVo = new StoreLoopAdminVo();
            BeanUtils.copyProperties(store, storeLoopAdminVo);
            Admin admin = csAdminService.findLoop(store.getId());
            if(admin != null){
                storeLoopAdminVo.setStoreLooplinkUserId(admin.getId());
                storeLoopAdminVo.setStoreLooplinkName(admin.getName());
                storeLoopAdminVo.setStoreLooplinkPhone(admin.getPhone());
                list.add(storeLoopAdminVo);
            }
        }
        return BaseResultUtil.success(list);
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

    /**
     * 查询区县所属业务中心列表-业务范围
     *
     * @param areaCode
     * @author JPG
     * @since 2019/11/5 9:27
     */
    @Override
    public Store getBelongByAreaCode(String areaCode) {
        return storeDao.findBelongByAreaCode(areaCode);
    }

    /**
     * 根据ID查询业务中心
     *
     * @param storeId
     * @param isSearchCache
     * @author JPG
     * @since 2019/11/5 17:26
     */
    @Override
    public Store getById(Long storeId, boolean isSearchCache) {
        return storeDao.selectById(storeId);
    }

    /**
     * 根据ID查询业务中心覆盖范围
     *
     * @param storeId
     * @return
     */
    @Override
    public List<String> getAreaBizScope(Long storeId) {
        return storeDao.findAreaBizScope(storeId);
    }

    @Override
    public List<Store> getAll() {
        return storeDao.findAll();
    }

    @Override
    public List<Long> getAllDeptId() {
        return storeDao.findAllDeptId();
    }

    /**
     * 获取所属业务中心
     * @author JPG
     * @since 2019/11/15 10:04
     * @param storeId
     */
    @Override
    public Long getBelongStoreId(Long storeId, String areaCode) {
        if(storeId != null && storeId > 0){
            return storeId;
        }
        Store store = getOneBelongByAreaCode(areaCode);
        if(store == null){
            return 0L;
        }
        return store.getId();
    }

    @Override
    public ResultVo<StoreListVo> findStore(FindStoreDto dto) {
        log.info("申请合伙人请求json数据 :: "+ JsonUtils.objectToJson(dto));
        List<BusinessStoreVo> storeVos = Lists.newArrayList();
        //根据城市编码code获取区县
        List<City> cityList = cityDao.selectList(new QueryWrapper<City>().lambda().eq(City::getParentCode, dto.getCityCode()));
        if(!CollectionUtils.isEmpty(cityList)){
            List<StoreCityCon> storeCityConList = storeCityConDao.selectList(new QueryWrapper<StoreCityCon>().lambda().in(StoreCityCon::getAreaCode, cityList.stream().map(City::getCode).collect(Collectors.toList())));
            if(!CollectionUtils.isEmpty(storeCityConList)){
                Set<Long> storeIds = storeCityConList.stream().map(StoreCityCon::getStoreId).collect(Collectors.toSet());
                dto.setStoreIds(storeIds);
                storeVos = storeDao.findStore(dto);
            }
        }
        StoreListVo storeVoList = new StoreListVo();
        storeVoList.setStoreVoList(storeVos);
        return BaseResultUtil.success(storeVoList);
    }


    @Override
    public boolean validateStoreParam(Long endStoreId, String endStoreName) {
        endStoreId = endStoreId == null ? 0 : endStoreId;
        endStoreName = endStoreName == null ? "" : endStoreName;
        if(endStoreId > 0 && !endStoreName.endsWith("业务中心")){
            return false;
        }
        if(endStoreId <= 0 && endStoreName.endsWith("业务中心")){
            return false;
        }
        return true;
    }

    @Override
    public Store getStoreFromMap(Map<Long, Store> map, Long storeId) {
        Store store;
        if (map.containsKey(storeId)) {
            store = map.get(storeId);
        } else {
            store = storeDao.selectById(storeId);
            map.put(storeId, store);
        }
        return store;
    }

}
