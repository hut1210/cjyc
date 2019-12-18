package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.common.utils.ExcelUtil;
import com.cjkj.usercenter.dto.common.AddDeptResp;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.UpdateDeptReq;
import com.cjkj.usercenter.dto.yc.AddInnerDeptAndFillReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dao.IStoreCityConDao;
import com.cjyc.common.model.dao.IStoreDao;
import com.cjyc.common.model.dto.web.city.StoreDto;
import com.cjyc.common.model.dto.web.store.GetStoreDto;
import com.cjyc.common.model.dto.web.store.StoreAddDto;
import com.cjyc.common.model.dto.web.store.StoreQueryDto;
import com.cjyc.common.model.dto.web.store.StoreUpdateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.Store;
import com.cjyc.common.model.entity.StoreCityCon;
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
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.common.system.util.ResultDataUtil;
import com.cjyc.web.api.service.IStoreService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StoreServiceImpl extends ServiceImpl<IStoreDao, Store> implements IStoreService {
    @Resource
    private IStoreDao storeDao;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IStoreCityConDao storeCityConDao;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICityDao cityDao;

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

        // 查询历史业务中心未删除的数据
        List<Store> list = super.list(new QueryWrapper<Store>().lambda()
                .eq(Store::getIsDelete, DeleteStateEnum.NO_DELETE.code));
        if (!CollectionUtils.isEmpty(list)) {
            store.setDeptId(list.get(0).getDeptId());
        }

        //将业务中心信息添加到物流平台
        ResultData<Long> saveRd = addBizCenterToPlatform(store);
        if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
            log.error("保存业务中心失败，原因：" + saveRd.getMsg());
            return BaseResultUtil.fail();
        }

        // 保存业务中心到韵车系统
        store.setDeptId(saveRd.getData());
        boolean result =  super.save(store);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modify(StoreUpdateDto storeUpdateDto) {
        Store store = new Store();
        BeanUtils.copyProperties(storeUpdateDto,store);
        store.setUpdateTime(System.currentTimeMillis());
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>().lambda().eq(Admin::getId, storeUpdateDto.getUpdateUserId()).select(Admin::getName));
        if (!Objects.isNull(admin)) {
            store.setOperationName(admin.getName());
        }
        //修改业务中心信息
        ResultData rd = updateBizCenterToPlatform(store);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            log.error("修改业务中心失败，原因：" + rd.getMsg());
            return false;
        }
        return super.updateById(store);
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
        ResultData<List<SelectUsersByRoleResp>> usersRd =
                sysUserService.getUsersByDeptId(store.getDeptId());
        if (!ReturnMsg.SUCCESS.getCode().equals(usersRd.getCode())) {
            return BaseResultUtil.fail("查询用户列表信息失败，原因：" + usersRd.getMsg());
        }
        if (CollectionUtils.isEmpty(usersRd.getData())) {
            return BaseResultUtil.success();
        }
        List<Admin> adminList = new ArrayList<>();
        usersRd.getData().stream().forEach(u -> {
            Admin admin = adminDao.selectOne(new QueryWrapper<Admin>()
                    .eq("phone", u.getAccount()));
            if (null != admin) {
                admin.setBizDesc(u.getRoles());
                adminList.add(admin);
            }
        });
        return BaseResultUtil.success(adminList);
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
        Store store = super.getById(id);
        if (null == store || store.getDeptId() == null || store.getDeptId() <= 0L) {
            return BaseResultUtil.fail("业务中心信息有误，请检查");
        }
        ResultData<List<SelectUsersByRoleResp>> usersRd = sysUserService.getUsersByDeptId(store.getDeptId());
        if (ResultDataUtil.isSuccess(usersRd)) {
            if (CollectionUtils.isEmpty(usersRd.getData())) {
                ResultData deleteRd = sysDeptService.delete(store.getDeptId());
                if (!ReturnMsg.SUCCESS.getCode().equals(deleteRd.getCode())) {
                    return BaseResultUtil.fail("业务中心删除失败，原因：" + deleteRd.getMsg());
                }
                boolean result = super.update(new UpdateWrapper<Store>().lambda()
                        .set(Store::getIsDelete, DeleteStateEnum.YES_DELETE.code).eq(Store::getId,id));
                return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
            }else {
                return BaseResultUtil.fail("此业务中心有关联用户信息，不能删除");
            }
        }else {
            return BaseResultUtil.fail("删除失败，原因：" + usersRd.getMsg());
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

    /**
     * 将业务中心添加到物流平台
     * @param store
     * @return
     */
    private ResultData<Long> addBizCenterToPlatform(Store store) {
        ResultData<SelectDeptResp> deptRd = sysDeptService.getDeptByCityCode(store.getCityCode());
        if (!ReturnMsg.SUCCESS.getCode().equals(deptRd.getCode())) {
            return ResultData.failed("根据城市编码查询机构信息错误，原因：" + deptRd.getMsg());
        }
        Long parentId = deptRd.getData().getDeptId();
        AddInnerDeptAndFillReq deptReq = new AddInnerDeptAndFillReq();
        deptReq.setName(store.getName());
        deptReq.setParentId(parentId);
        deptReq.setTemplateDeptId(store.getDeptId());
        ResultData<AddDeptResp> saveRd = sysDeptService.addInnerDeptAndFill(deptReq);
        if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
            return ResultData.failed("保存业务中心失败，原因：" + saveRd.getMsg());
        }
        return ResultData.ok(saveRd.getData().getDeptId());
    }

    /**
     * 修改业务中心信息
     * @param store
     * @return
     */
    private ResultData updateBizCenterToPlatform(Store store) {
        //1.验证cityCode是否变更
        Store originalStore = storeDao.selectById(store.getId());
        if (null == originalStore) {
            return ResultData.failed("根据业务中心id：" + store.getId() + ", 未查询到机构信息");
        }
        Long parentDeptId = null;
        if (!store.getCityCode().equals(originalStore.getCityCode())) {
            //上级机构变更，物流平台需要变更部门
            ResultData<SelectDeptResp> deptRd = sysDeptService.getDeptByCityCode(store.getCityCode());
            if (!ReturnMsg.SUCCESS.getCode().equals(deptRd.getCode())) {
                return ResultData.failed("查询机构信息错误，原因: " + deptRd.getMsg());
            }
            parentDeptId = deptRd.getData().getDeptId();
        }
        UpdateDeptReq deptReq = new UpdateDeptReq();
        deptReq.setDeptId(originalStore.getDeptId());
        deptReq.setName(store.getName());
        deptReq.setParentId(parentDeptId);
        return sysDeptService.update(deptReq);
    }
}
