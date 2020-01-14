package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.*;
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
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.store.StoreExportExcel;
import com.cjyc.common.model.vo.store.StoreVo;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.IStoreService_1;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 韵车业务中心信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-23
 */
@Service
public class StoreServiceImpl_1 extends ServiceImpl<IStoreDao, Store> implements IStoreService_1 {
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
    public List<Store> getByAreaCode(String areaCode) {
        return storeDao.findByAreaCode(areaCode);
    }

    @Override
    public ResultVo queryPage(StoreQueryDto dto) {
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
        // 验证业务中心名称是否重复
        Store queryStore = super.getOne(new QueryWrapper<Store>().lambda()
                .eq(Store::getName, storeAddDto.getName())
                .eq(Store::getIsDelete,DeleteStateEnum.NO_DELETE.code));
        if(queryStore != null){
            return BaseResultUtil.getVo(ResultEnum.EXIST_STORE.getCode(),ResultEnum.EXIST_STORE.getMsg());
        }

        // 封装入库参数
        Store store = getStore(storeAddDto);

        // 保存业务中心
        boolean result =  super.save(store);
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

        if (!CollectionUtils.isEmpty(storeList)) {
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
        }
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
                .like(!StringUtils.isEmpty(storeQueryDto.getName()),Store::getName,storeQueryDto.getName());
        return super.list(queryWrapper);
    }
}
