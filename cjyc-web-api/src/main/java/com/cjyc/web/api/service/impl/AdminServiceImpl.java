package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.PageData;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.SelectDeptListByParentIdsReq;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.yc.SelectPageUsersForSalesmanReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.IRoleDao;
import com.cjyc.common.model.dto.web.salesman.AdminPageDto;
import com.cjyc.common.model.dto.web.salesman.TypeSalesmanDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.admin.CacheData;
import com.cjyc.common.model.vo.web.admin.TypeSalesmanVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsStoreService;
import com.cjyc.web.api.service.IAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 韵车后台管理员表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
@Service
public class AdminServiceImpl extends ServiceImpl<IAdminDao, Admin> implements IAdminService {


    /**
     * 社会车辆事业部机构ID, 因为是初始化数据，所以此ID为固定值
     */
    private static final Long BIZ_TOP_DEPT_ID = Long.parseLong(YmlProperty.get("cjkj.dept_admin_id"));

    @Resource
    private IAdminDao adminDao;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private ISysDeptService deptService;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private IRoleDao roleDao;


    @Override
    public Admin getByUserId(Long userId) {
        return adminDao.findByUserId(userId);
    }

    @Override
    public ResultVo deliverySalesman(TypeSalesmanDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<TypeSalesmanVo> salesmanVos = adminDao.deliverySalesman(dto);
        if(dto.getIsPage() == 0){
            //不分页
            return BaseResultUtil.success(salesmanVos);
        }
        PageInfo<TypeSalesmanVo> pageInfo =  new PageInfo<>(salesmanVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public PageVo<AdminPageVo> page(AdminPageDto paramsDto) {

        List<AdminPageVo> resList = new ArrayList<>();

        SelectPageUsersForSalesmanReq req = new SelectPageUsersForSalesmanReq();
        BeanUtils.copyProperties(paramsDto, req);

        //计算业务中心
        List<Long> deptIds= new ArrayList<>();
        //查询机构
        ResultData<SelectRoleResp> resultData = sysRoleService.getById(paramsDto.getRoleId());
        if(resultData == null || resultData.getData() == null){
            return null;
        }
        ResultData<List<SelectDeptResp>> resultDeptData = sysDeptService.getMultiLevelDeptList(resultData.getData().getDeptId());
        if(resultDeptData == null || CollectionUtils.isEmpty(resultDeptData.getData())){
            return null;
        }
        //范围
        List<Long> baseList = resultDeptData.getData().stream().map(SelectDeptResp::getDeptId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(baseList)){
            return null;
        }

        if(paramsDto.getStoreId() == null || StringUtils.isBlank(paramsDto.getRoleName())){

            deptIds.add(resultData.getData().getDeptId());
        }else if(paramsDto.getStoreId() == null || StringUtils.isNotBlank(paramsDto.getRoleName())){
            Role role = roleDao.findByName(paramsDto.getRoleName());
            if(role == null){
                return null;
            }
            if(role.getRoleLevel() == RoleLevelEnum.COUNTRY_LEVEL.getLevel()){
                deptIds.add(BIZ_TOP_DEPT_ID);
            }else if(role.getRoleLevel() == RoleLevelEnum.REGION_LEVEL.getLevel()){
                List<Long> list = getRegionGovIdList();
                if(CollectionUtils.isEmpty(list)){
                    return null;
                }
                baseList.forEach(deptId -> {
                    if(list.contains(deptId)){
                        deptIds.add(deptId);
                    }
                });

            }else if(role.getRoleLevel() == RoleLevelEnum.PROVINCE_LEVEL.getLevel()){
                List<Long> list = getProvinceGovIdList();
                if(CollectionUtils.isEmpty(list)){
                    return null;
                }
                baseList.forEach(deptId -> {
                    if(list.contains(deptId)){
                        deptIds.add(deptId);
                    }
                });
            }else if(role.getRoleLevel() == RoleLevelEnum.CITY_LEVEL.getLevel()){
                List<Long> list = getCityGovIdList();
                if(CollectionUtils.isEmpty(list)){
                    return null;
                }
                baseList.forEach(deptId -> {
                    if(list.contains(deptId)){
                        deptIds.add(deptId);
                    }
                });
            }else{
                List<Long> list = getBizCenterGovIdList();
                if(CollectionUtils.isEmpty(list)){
                    return null;
                }
                baseList.forEach(deptId -> {
                    if(list.contains(deptId)){
                        deptIds.add(deptId);
                    }
                });
            }
        }else{
            Store store = csStoreService.getById(paramsDto.getStoreId(), true);
            if(!baseList.contains(store.getDeptId())){
                return null;
            }
            deptIds.add(store.getDeptId());
        }
        req.setDeptIdList(deptIds);
        ResultData<PageData<SelectUsersByRoleResp>> resData = sysUserService.getPageUsersForSalesman(req);
        if(resData == null || resData.getData() == null || CollectionUtils.isEmpty(resData.getData().getList())){
            return null;
        }


        List<SelectUsersByRoleResp> list = resData.getData().getList();
        Set<Long> collect = list.stream().map(SelectUsersByRoleResp::getUserId).collect(Collectors.toSet());
        List<Admin> adminList = adminDao.findListByUserIds(collect);
        if(CollectionUtils.isEmpty(adminList)){
            return null;
        }
        //附加数据
        for (SelectUsersByRoleResp selectUsersByRoleResp : list) {
            AdminPageVo adminPageVo = new AdminPageVo();
            BeanUtils.copyProperties(selectUsersByRoleResp, adminPageVo);
            for (Admin admin : adminList) {
                if(admin.getUserId().equals(selectUsersByRoleResp.getUserId())){
                    adminPageVo.setBizDesc(admin.getBizDesc());
                    adminPageVo.setState(admin.getState());
                    adminPageVo.setCreateTime(admin.getCreateTime());
                    adminPageVo.setCreateUser(Long.parseLong(admin.getCreateUser()));
                    adminPageVo.setId(admin.getId());
                }
            }
            resList.add(adminPageVo);
        }
        PageVo<AdminPageVo> pageVo = PageVo.<AdminPageVo>builder()
                .totalRecords(resData.getData().getTotal())
                .pageSize(paramsDto.getPageSize())
                .currentPage(paramsDto.getCurrentPage())
                .totalPages(getPages(resData.getData().getTotal(), paramsDto.getPageSize()))
                .list(resList)
                .build();

        return pageVo;

    }


    @Override
    public CacheData getCacheData(Long userId, Long roleId) {
        CacheData cacheData = new CacheData();
        //根据角色查询机构ID
        ResultData<SelectRoleResp> resultData = sysRoleService.getById(roleId);
        if (resultData == null || resultData.getData() == null) {
            return null;
        }

        //业务员
        Integer userType = 0;
        if(userType == UserTypeEnum.ADMIN.code){
            Admin admin = adminDao.findByUserId(userId);
            if (admin != null) {
                cacheData.setDeptId(resultData.getData().getDeptId());
                cacheData.setLoginId(admin.getId());
                cacheData.setLoginName(admin.getName());
                cacheData.setLoginPhone(admin.getPhone());
                cacheData.setRoleId(roleId);
                cacheData.setLoginType(UserTypeEnum.ADMIN.code);
            }

        }else if(userType == UserTypeEnum.CUSTOMER.code){
            //客户
            Customer customer = csCustomerService.getByUserId(userId, true);
            if(customer != null){
                cacheData.setDeptId(resultData.getData().getDeptId());
                cacheData.setLoginId(customer.getId());
                cacheData.setLoginName(customer.getName());
                cacheData.setLoginPhone(customer.getContactPhone());
                cacheData.setRoleId(roleId);
                cacheData.setLoginType(UserTypeEnum.CUSTOMER.code);
            }
        }else if(userType == UserTypeEnum.DRIVER.code){
            //司机
            Driver driver = csDriverService.getByUserId(userId);
            if(driver != null){
                cacheData.setDeptId(resultData.getData().getDeptId());
                cacheData.setLoginId(driver.getId());
                cacheData.setLoginName(driver.getName());
                cacheData.setLoginPhone(driver.getPhone());
                cacheData.setRoleId(roleId);
                cacheData.setLoginType(UserTypeEnum.DRIVER.code);
            }
        }else{
            return null;
        }
        return cacheData;
    }

    private int getPages(long total, Integer pageSize) {
        if(total <= 0){
            return 0;
        }
        return new BigDecimal(total).divide(new BigDecimal(pageSize), BigDecimal.ROUND_CEILING).intValue();
    }

    /**
     * 获取大区机构id列表
     * @return
     */
    private List<Long> getRegionGovIdList() {
        ResultData<List<SelectDeptResp>> regionGovListRd =
                sysDeptService.getSingleLevelDeptList(BIZ_TOP_DEPT_ID);
        if (!ReturnMsg.SUCCESS.getCode().equals(regionGovListRd.getCode())) {
            return null;
        }
        if (CollectionUtils.isEmpty(regionGovListRd.getData())) {
            return null;
        }
        return regionGovListRd.getData().stream()
                .map(SelectDeptResp::getDeptId).collect(Collectors.toList());
    }

    /**
     * 获取省机构id列表
     * @return
     */
    private List<Long> getProvinceGovIdList() {
        //大区信息
        List<Long> regionIdList = getRegionGovIdList();
        return batchQuery(regionIdList);
    }

    /**
     * 获取城市机构id列表信息
     * @return
     */
    private List<Long> getCityGovIdList() {
        List<Long> provinceGovIds = getProvinceGovIdList();
        return batchQuery(provinceGovIds);
    }

    /**
     * 获取业务中心id列表信息
     * @return
     */
    private List<Long> getBizCenterGovIdList() {
        List<Long> cityGovIds = getCityGovIdList();
        return batchQuery(cityGovIds);
    }

    /**
     * 根据父id列表查询子机构列表信息
     * @param parentIdList
     * @return
     */
    private List<Long> batchQuery(List<Long> parentIdList) {
        if (CollectionUtils.isEmpty(parentIdList)) {
            return null;
        }
        List<Long> idList = new ArrayList<>();
        SelectDeptListByParentIdsReq req = new SelectDeptListByParentIdsReq();
        req.setParentIdList(parentIdList);
        ResultData<List<SelectDeptResp>> rd = sysDeptService.getSonDeptsByParentIds(req);
        if (!isResultDataSuccess(rd)) {
            return null;
        }else {
            if (!CollectionUtils.isEmpty(rd.getData())) {
                idList.addAll(rd.getData().stream()
                        .map(d -> d.getDeptId()).collect(Collectors.toList()));
            }
        }
        return idList;
    }


    /**
     * 返回结果状态是否正确
     * @param rd
     * @return
     */
    private boolean isResultDataSuccess(ResultData rd) {
        return ReturnMsg.SUCCESS.getCode().equals(rd.getCode());
    }

}
