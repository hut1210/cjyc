package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.BaseWebDto;
import com.cjyc.common.model.dto.web.city.StoreDto;
import com.cjyc.common.model.dto.web.store.GetStoreDto;
import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.enums.BizScopeEnum;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.DeleteStateEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.store.StoreExportExcel;
import com.cjyc.common.model.dto.web.store.StoreImportExcel;
import com.cjyc.common.model.vo.web.store.StoreVo;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 韵车业务中心信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
@Slf4j
@Service
public class StoreServiceImpl extends ServiceImpl<IStoreDao, Store> implements IStoreService {
    @Resource
    private IStoreDao storeDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IStoreCityConDao storeCityConDao;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICityDao cityDao;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;

    @Override
    public List<Store> getByCityCode(String cityCode) {
        return storeDao.findByCityCode(cityCode);
    }

    @Override
    public List<Store> listByWebLogin(BaseWebDto reqDto) {
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(reqDto.getLoginId(), reqDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }else if(bizScope.getCode() == BizScopeEnum.CHINA.code){
            return storeDao.findAll();
        }else{
            return storeDao.findByIds(bizScope.getStoreIds());
        }
    }
    @Override
    public List<StoreVo> listVoByWebLogin(BaseWebDto reqDto) {
        BizScope bizScope = csSysService.getBizScopeBySysRoleIdNew(reqDto.getLoginId(), reqDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }else if(bizScope.getCode() == BizScopeEnum.CHINA.code){
            return storeDao.findVoAll();
        }else{
            return storeDao.findVoByIds(bizScope.getStoreIds());
        }
    }

    @Override
    public List<Store> getByAreaCode(String areaCode) {
        return storeDao.findByAreaCode(areaCode);
    }

    @Override
    public ResultVo queryPage(StoreQueryDto dto) {
        log.info("====>web端-分页查询业务中心列表,请求json数据 :: "+ JsonUtils.objectToJson(dto));
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<Store> list = getStoreList(dto);
        PageInfo<Store> pageInfo = new PageInfo<>(list);
        List<Store> storeVoList = new ArrayList<>(20);
        List<Store> pageInfoList = pageInfo.getList();
        // 查询管辖范围与所属大区
        this.getAreaCountAndRegion(storeVoList, pageInfoList);
        pageInfo.setList(storeVoList);
        return BaseResultUtil.success(pageInfo);
    }

    private void getAreaCountAndRegion(List<Store> storeVoList, List<Store> pageInfoList) {
        if (!CollectionUtils.isEmpty(pageInfoList)) {
            StoreVo storeVo = null;
            for (Store store : pageInfoList) {
                storeVo = new StoreVo();
                BeanUtils.copyProperties(store,storeVo);
                // 查询业务中心所属大区
                City city = cityDao.selectOne(new QueryWrapper<City>().lambda().eq(City::getCode, store.getProvinceCode()));
                if (FieldConstant.NOT_REGION_CODE.equals(city.getParentCode())) {
                    storeVo.setRegionName("无");
                } else {
                    storeVo.setRegionName(city.getParentName());
                }

                // 查询业务中心管辖区数量
                Integer count = storeCityConDao.selectCount(new QueryWrapper<StoreCityCon>().lambda().eq(StoreCityCon::getStoreId, store.getId()));
                storeVo.setAreaCount(count);
                storeVoList.add(storeVo);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo add(StoreAddDto storeAddDto) {
        log.info("====>web端-新增业务中心,请求json数据 :: "+JsonUtils.objectToJson(storeAddDto));
        // 验证业务中心名称是否重复
        List<Store> storeList = storeDao.selectList(new QueryWrapper<Store>().lambda()
                .eq(Store::getName, storeAddDto.getName())
                .or()
                .eq(Store::getCityCode, storeAddDto.getCityCode())
                .eq(Store::getIsDelete,DeleteStateEnum.NO_DELETE.code));
        if(!CollectionUtils.isEmpty(storeList)){
            return BaseResultUtil.fail("该业务中心已存在或该城市已绑定业务中心,请重新输入");
        }
        // 封装入库参数
        Store store = getStore(storeAddDto);
        //新增城市下面的未覆盖的区县
        List<String> areaCodeList = findAreaCodeList(storeAddDto.getCityCode());
        // 保存业务中心
        boolean result =  super.save(store);
        //保存业务中心覆盖区县
        if(result && !CollectionUtils.isEmpty(areaCodeList)){
            for(String areaCode : areaCodeList){
                StoreCityCon scc = new StoreCityCon();
                scc.setStoreId(store.getId());
                scc.setAreaCode(areaCode);
                storeCityConDao.insert(scc);
            }
        }
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    Store getStore(StoreAddDto storeAddDto) {
        Store store = new Store();
        BeanUtils.copyProperties(storeAddDto,store);
        store.setIsDelete(DeleteStateEnum.NO_DELETE.code);
        store.setState(CommonStateEnum.CHECKED.code);
        store.setCreateTime(System.currentTimeMillis());
        // 查询操作人姓名
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>().lambda().eq(Admin::getId, storeAddDto.getCreateUserId()).select(Admin::getName));
        if (!Objects.isNull(admin)) {
            store.setOperationName(admin.getName());
        }
        return store;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo modify(StoreUpdateDto storeUpdateDto) {
        log.info("====>web端-修改业务中心,请求json数据 :: "+JsonUtils.objectToJson(storeUpdateDto));
        Store store = new Store();
        BeanUtils.copyProperties(storeUpdateDto,store);
        store.setUpdateTime(System.currentTimeMillis());
        // 查询用户名称
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>().lambda().eq(Admin::getId, storeUpdateDto.getUpdateUserId()).select(Admin::getName));
        if (!Objects.isNull(admin)) {
            store.setOperationName(admin.getName());
        }

        boolean result = super.updateById(store);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        StoreQueryDto storeQueryDto = getStoreQueryDto(request);
        // 查询列表
        List<Store> storeList = getStoreList(storeQueryDto);

        //if (!CollectionUtils.isEmpty(storeList)) {
        // 生成导出数据
        List<StoreExportExcel> exportExcelList = new ArrayList<>(10);
        for (Store store : storeList) {
            StoreExportExcel storeExportExcel = new StoreExportExcel();
            BeanUtils.copyProperties(store,storeExportExcel);
            // 查询业务中心所属大区
            City city = cityDao.selectOne(new QueryWrapper<City>().lambda().eq(City::getCode, store.getProvinceCode()));
            storeExportExcel.setRegionName(city.getParentName());
            // 查询业务中心管辖区数量
            Integer count = storeCityConDao.selectCount(new QueryWrapper<StoreCityCon>().lambda().eq(StoreCityCon::getStoreId, store.getId()));
            storeExportExcel.setAreaCount(count == null ? 0 : count);
            exportExcelList.add(storeExportExcel);
        }
        String title = "业务中心";
        String sheetName = "业务中心";
        String fileName = "业务中心表.xls";
        try {
            ExcelUtil.exportExcel(exportExcelList, title, sheetName, StoreExportExcel.class, fileName, response);
        } catch (IOException e) {
            log.error("导出业务中心异常:{}",e);
        }
        //}
    }

    @Override
    public ResultVo<List<Admin>> listAdminsByStoreId(Long storeId) {
        //业务中心列表获取
        Store store = baseMapper.selectById(storeId);
        if (null == store) {
            return BaseResultUtil.fail("业务中心获取错误, 根据id：" + storeId + "未获取到信息");
        }

        //List<Admin> adminList = userRoleDeptDao.selectAdminseByStoreId(storeId);
        return BaseResultUtil.success("");
    }

    @Override
    public ResultVo getStoreCoveredAreaList(StoreDto dto) {
        log.info("====>web端-根据业务中心ID查询覆盖区列表,请求json数据 :: "+JsonUtils.objectToJson(dto));
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<FullCity> list = cityDao.selectCoveredList(dto);
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo getStoreNoCoveredAreaList(StoreDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<FullCity> list = cityDao.selectNoCoveredList(dto);
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo addCoveredArea(StoreDto dto) {
        log.info("====>web端-新增前业务中心覆盖区域,请求json数据 :: "+JsonUtils.objectToJson(dto));
        StoreCityCon storeCityCon = new StoreCityCon();
        storeCityCon.setStoreId(dto.getStoreId());
        for (String areaCode : dto.getAreaCodeList()) {
            storeCityCon.setAreaCode(areaCode);
            StoreCityCon result = storeCityConDao.selectOne(new QueryWrapper<StoreCityCon>().lambda().eq(StoreCityCon::getAreaCode, areaCode));
            if (!Objects.isNull(result)) {
                continue;
            }
            storeCityConDao.insert(storeCityCon);
        }
        return BaseResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo removeCoveredArea(StoreDto dto) {
        log.info("====>web端-删除当前业务中心覆盖区域,请求json数据 :: "+JsonUtils.objectToJson(dto));
        LambdaUpdateWrapper<StoreCityCon> updateWrapper = null;
        for (String areaCode : dto.getAreaCodeList()) {
            updateWrapper = new UpdateWrapper<StoreCityCon>().lambda().eq(StoreCityCon::getStoreId,dto.getStoreId()).eq(StoreCityCon::getAreaCode,areaCode);
            storeCityConDao.delete(updateWrapper);
        }
        return BaseResultUtil.success();
    }

    @Override
    public List<Store> getListByRoleId(Long roleId) {
        BizScope bizScope = csSysService.getBizScopeByRoleId(roleId, true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }else if(bizScope.getCode() == BizScopeEnum.CHINA.code){
            return storeDao.findAll();
        }else{
            return storeDao.findByIds(bizScope.getStoreIds());
        }
    }

    @Override
    public List<StoreVo> getVoListByRoleId(Long roleId) {
        BizScope bizScope = csSysService.getBizScopeByRoleId(roleId, true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }else if(bizScope.getCode() == BizScopeEnum.CHINA.code){
            return storeDao.findVoAll();
        }else{
            return storeDao.findVoByIds(bizScope.getStoreIds());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo remove(Long id) {
        log.info("====>web端-删除业务中心,请求json数据 :: "+JsonUtils.objectToJson(id));
        // 查询该业务中心下是否关联了用户
        List<UserRoleDept> userRoleDeptList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>().lambda().eq(UserRoleDept::getDeptId, id));
        // 逻辑删除业务中心
        if (CollectionUtils.isEmpty(userRoleDeptList)) {
            boolean result = super.update(new UpdateWrapper<Store>().lambda()
                    .set(Store::getIsDelete, DeleteStateEnum.YES_DELETE.code).eq(Store::getId,id));
            return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
        }else {
            return BaseResultUtil.fail("此业务中心有关联用户信息，不能删除");
        }
    }

    @Override
    public List<Store> get(GetStoreDto reqDto) {
        BizScope bizScope = csSysService.getBizScopeByRoleId(reqDto.getRoleId(), true);
        if(bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code){
            return null;
        }else if(bizScope.getCode() == BizScopeEnum.CHINA.code){
            return storeDao.findByName(reqDto.getStoreName());
        }else{
            return storeDao.findByNameAndIds(reqDto.getStoreName(), bizScope.getStoreIds());
        }
    }

    private StoreQueryDto getStoreQueryDto(HttpServletRequest request) {
        StoreQueryDto storeQueryDto = new StoreQueryDto();
        storeQueryDto.setCurrentPage(Integer.valueOf(request.getParameter("currentPage")));
        storeQueryDto.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
        storeQueryDto.setName(request.getParameter("name"));
        storeQueryDto.setProvinceCode(request.getParameter("provinceCode"));
        storeQueryDto.setCityCode(request.getParameter("cityCode"));
        storeQueryDto.setAreaCode(request.getParameter("areaCode"));
        return storeQueryDto;
    }

    private List<Store> getStoreList(StoreQueryDto storeQueryDto) {
        LambdaQueryWrapper<Store> queryWrapper = new QueryWrapper<Store>().lambda()
                .eq(!StringUtils.isEmpty(storeQueryDto.getProvinceCode()),Store::getProvinceCode,storeQueryDto.getProvinceCode())
                .eq(!StringUtils.isEmpty(storeQueryDto.getCityCode()),Store::getCityCode,storeQueryDto.getCityCode())
                .eq(!StringUtils.isEmpty(storeQueryDto.getAreaCode()),Store::getAreaCode,storeQueryDto.getAreaCode())
                .eq(Store::getIsDelete,DeleteStateEnum.NO_DELETE.code)
                .like(!StringUtils.isEmpty(storeQueryDto.getName()),Store::getName,storeQueryDto.getName())
                .orderByDesc(Store::getCreateTime);
        return super.list(queryWrapper);
    }

    @Override
    public boolean importStoreExcel(MultipartFile file, Long loginId) {
        boolean result;
        try {
            List<StoreImportExcel> storeImportExcelList = ExcelUtil.importExcel(file, 0, 1, StoreImportExcel.class);
            if(!CollectionUtils.isEmpty(storeImportExcelList)){
                for(StoreImportExcel storeExcel : storeImportExcelList){
                    Store store = new Store();
                    store.setName(storeExcel.getName());
                    store.setProvince(storeExcel.getProvince());
                    store.setProvinceCode(cityDao.findProvinceCode(storeExcel.getProvince()));
                    store.setCity(storeExcel.getCity());
                    store.setCityCode(cityDao.findCodeByName(storeExcel.getCity(),storeExcel.getProvince(),2));
                    store.setArea(storeExcel.getArea());
                    store.setAreaCode(cityDao.findCodeByName(storeExcel.getArea(),storeExcel.getCity(),3));
                    store.setDetailAddr(storeExcel.getDetailAddr());
                    store.setState(CommonStateEnum.CHECKED.code);
                    store.setCreateUserId(loginId);
                    store.setCreateTime(System.currentTimeMillis());
                    store.setIsDelete(DeleteStateEnum.NO_DELETE.code);
                    storeDao.insert(store);
                }
                result = true;
            }else{
                result = false;
            }
        } catch (Exception e) {
            log.error("导入业务中心失败异常:{}",e);
            result = false;
        }
        return result;
    }

    @Override
    public ResultVo<List<Store>> findAllStore() {
        List<Store> stores = storeDao.selectList(new QueryWrapper<Store>().lambda()
                .eq(Store::getIsDelete,DeleteStateEnum.NO_DELETE.code)
                .eq(Store::getState,CommonStateEnum.CHECKED.code));
        return BaseResultUtil.success(stores);
    }

    /**
     * 获取该城市下没有被绑定的区县
     * @param cityCode
     * @return
     */
    private List<String> findAreaCodeList(String cityCode){
        //根据城市编码获取下面所有区县code
        List<City> cityList = cityDao.selectList(new QueryWrapper<City>().lambda().eq(City::getParentCode, cityCode));
        List<String> areaCodeList = null;
        if(!CollectionUtils.isEmpty(cityList)){
            areaCodeList = cityList.stream().map(City::getCode).collect(Collectors.toList());
        }
        //获取该城市下已绑定的业务中心的区县code
        List<String> coverAreaCodeList = null;
        List<StoreCityCon> storeCityConList = storeCityConDao.selectList(new QueryWrapper<StoreCityCon>().lambda()
                .in(StoreCityCon::getAreaCode, areaCodeList));
        if(!CollectionUtils.isEmpty(storeCityConList)){
            coverAreaCodeList = storeCityConList.stream().map(StoreCityCon::getAreaCode).collect(Collectors.toList());
        }
        if(!CollectionUtils.isEmpty(coverAreaCodeList)){
            areaCodeList.removeAll(coverAreaCodeList);
        }
        return areaCodeList;
    }

    @Override
    public List<Store> findStore(String cityCode) {
        List<Store> list = Lists.newArrayList();
        //根据城市编码code获取区县
        List<City> cityList = cityDao.selectList(new QueryWrapper<City>().lambda().eq(City::getParentCode, cityCode));
        if(!CollectionUtils.isEmpty(cityList)){
            List<StoreCityCon> storeCityConList = storeCityConDao.selectList(new QueryWrapper<StoreCityCon>().lambda().in(StoreCityCon::getAreaCode, cityList.stream().map(City::getCode).collect(Collectors.toList())));
            if(!CollectionUtils.isEmpty(storeCityConList)){
                Set<Long> storeIds = storeCityConList.stream().map(StoreCityCon::getStoreId).collect(Collectors.toSet());
                //获取业务中心
                list = storeDao.findByIds(storeIds);
            }
        }
        return list;
    }
}
