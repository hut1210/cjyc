package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.*;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjkj.usercenter.dto.yc.UpdateBatchRoleMenusReq;
import com.cjyc.common.model.dao.IRoleDao;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.dto.web.role.ModifyRoleMenusDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.enums.role.RoleRangeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.SelectUserByRoleVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 角色信息service实现类
 */

@Service
public class RoleServiceImpl extends ServiceImpl<IRoleDao, Role> implements IRoleService {
    /**
     * 社会车辆事业部机构ID, 因为是初始化数据，所以此ID为固定值
     */
    private static final Long BIZ_TOP_DEPT_ID = Long.parseLong(YmlProperty.get("cjkj.dept_admin_id"));

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private IAdminService adminService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo addRole(AddRoleDto dto) {
        return doAddRole(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo deleteRole(Long id) {
        //角色信息删除操作
        Role role = baseMapper.selectById(id);
        if (null == role) {
            return BaseResultUtil.success();
        }
        return doDeleteRole(role);
    }

    @Override
    public ResultVo<List<SelectUserByRoleVo>> getUsersByRoleId(Long roleId) {
        //根据角色查询关联角色信息
        Role role = baseMapper.selectById(roleId);
        if (null == role) {
            return BaseResultUtil.success();
        }
        ResultData<List<SelectUsersByRoleResp>> listResultData = doGetUsersByRoleId(role);
        if (!ReturnMsg.SUCCESS.getCode().equals(listResultData.getCode())) {
            return BaseResultUtil.fail(listResultData.getMsg());
        }
        List<SelectUsersByRoleResp> list = listResultData.getData();
        if (CollectionUtils.isEmpty(list)) {
            return BaseResultUtil.success();
        }
        List<SelectUserByRoleVo> rsList = new ArrayList<>();
        list.forEach(l -> {
            if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == role.getRoleLevel()) {
                l.setBizCenter(l.getBizCity());
                l.setBizCity(null);
            }
            SelectUserByRoleVo vo = new SelectUserByRoleVo();
            BeanUtils.copyProperties(l, vo);
            Admin admin = adminService.getOne(new QueryWrapper<Admin>()
                    .eq("phone", l.getAccount()));
            if (null != admin) {
                vo.setCreateUser(admin.getCreateUser());
                vo.setCreateTime(admin.getCreateTime());
                vo.setBizDesc(admin.getBizDesc());
            }
            rsList.add(vo);
        });
        return BaseResultUtil.success(rsList);
    }

    @Override
    public ResultVo<List<SelectUsersByRoleResp>> getDeptRoleListByUserRoleId(Long roleId) {
        //sysDeptService.

        return null;
    }

    @Override
    public ResultVo<List<Role>> getAllList(String roleName) {
        return BaseResultUtil.success(this.list(new QueryWrapper<Role>()
            .like(!StringUtils.isEmpty(roleName), "role_name", roleName)));
    }

    @Override
    public ResultVo<Integer> getUserTypeByRole(Long roleId) {
        ResultData<SelectRoleResp> byIdRd = sysRoleService.getById(roleId);
        if (!isResultDataSuccess(byIdRd)) {
            return BaseResultUtil.fail("角色信息查询错误，原因：" + byIdRd.getMsg());
        }
        if (null == byIdRd.getData()) {
            return BaseResultUtil.success(0);
        }
        Long roleDeptId = byIdRd.getData().getDeptId();
        ResultData<List<SelectDeptResp>> multiDeptRd =
                sysDeptService.getMultiLevelDeptList(Long.parseLong(YmlProperty.get("cjkj.dept_customer_id")));
        if (!isResultDataSuccess(multiDeptRd)) {
            return BaseResultUtil.fail("机构信息查询失败，原因：" + multiDeptRd.getMsg());
        }
        //个人用户判断
        if (!CollectionUtils.isEmpty(multiDeptRd.getData())) {
            List<Long> cDeptIdList = multiDeptRd.getData().stream().map(m -> m.getDeptId())
                    .collect(Collectors.toList());
            if (cDeptIdList.contains(roleDeptId)) {
                return BaseResultUtil.success(UserTypeEnum.CUSTOMER.code);
            }
        }
        //管理员用户判断
        if (roleDeptId.equals(BIZ_TOP_DEPT_ID)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (getRegionGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (getProvinceGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (getCityGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (getBizCenterGovIdList() != null && getBizCenterGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        return BaseResultUtil.success(UserTypeEnum.DRIVER.code);
    }

    @Override
    public ResultVo modifyRoleMenus(ModifyRoleMenusDto dto) {
        Role role = baseMapper.selectById(dto.getId());
        if (null == role) {
            return BaseResultUtil.success();
        }
        List<Long> deptIdList = getGovIdsByRoleLevel(role.getRoleLevel());
        if (CollectionUtils.isEmpty(deptIdList)) {
            return BaseResultUtil.fail("机构id列表信息为空，请检查角色信息");
        }
        UpdateBatchRoleMenusReq req = new UpdateBatchRoleMenusReq();
        req.setDeptIdList(deptIdList);
        req.setMenuIdList(dto.getMenuIdList());
        req.setRoleName(role.getRoleName());
        ResultData rd = sysRoleService.batchUpdateRoleMenus(req);
        if (!isResultDataSuccess(rd)) {
            return BaseResultUtil.fail("变更角色菜单列表失败，原因: " + rd.getMsg());
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<List<String>> getBtmMenuIdsByRoleId(Long roleId) {
        Role role = baseMapper.selectById(roleId);
        if (null == role) {
            return BaseResultUtil.fail("角色信息错误，请检查");
        }
        List<Long> deptIdList = getGovIdsByRoleLevel(role.getRoleLevel());
        if (CollectionUtils.isEmpty(deptIdList)) {
            return BaseResultUtil.fail("角色级别错误，请检查");
        }
        ResultData<List<SelectRoleResp>> rolesRd =
                sysRoleService.getSingleLevelList(deptIdList.get(0));
        if (!isResultDataSuccess(rolesRd)) {
            return BaseResultUtil.fail("查询角色信息错误，原因：" + rolesRd.getMsg());
        }
        if (CollectionUtils.isEmpty(rolesRd.getData())) {
            return BaseResultUtil.fail("根据机构id ：" + deptIdList.get(0) + "未查询到角色信息");
        }
        AtomicReference<Long> finalRoleId = new AtomicReference<>();
        rolesRd.getData().forEach(r -> {
            if (r.getRoleName().equals(role.getRoleName())) {
                finalRoleId.set(r.getRoleId());
            }
        });
        if (finalRoleId.get() == null) {
            return BaseResultUtil.fail("查询角色信息错误，机构下无此角色");
        }
        ResultData<List<Long>> rsRd = sysRoleService.getBottomMenuIdsByRoleId(finalRoleId.get());
        if (!isResultDataSuccess(rsRd)) {
            return BaseResultUtil.fail("查询错误，原因：" + rsRd.getMsg());
        }
        if (CollectionUtils.isEmpty(rsRd.getData())) {
            return BaseResultUtil.success();
        }else {
            List<String> rsList = rsRd.getData().stream().map(id -> String.valueOf(id)).collect(Collectors.toList());
            return BaseResultUtil.success(rsList);
        }
    }


    /**
     * 根据角色id查询获取用户列表信息
     * @param role
     * @return
     */
    private ResultData<List<SelectUsersByRoleResp>> doGetUsersByRoleId(Role role) {
        if (RoleRangeEnum.INNER.getValue() == role.getRoleRange()) {
            //内部
            SelectUsersByRoleReq req = new SelectUsersByRoleReq();
            req.setRoleName(role.getRoleName());
            req.setDeptIdList(getGovIdsByRoleLevel(role.getRoleLevel()));
            return sysRoleService.getUsersByRoleId(req);
        }else if (RoleRangeEnum.OUTER.getValue() == role.getRoleRange()) {
            //TODO 外部
            return null;
        }else {
            return ResultData.failed("机构范围：" + role.getRoleRange() + "错误，请检查");
        }
    }

    /**
     * 根据角色级别获取机构id列表
     * @param level
     * @return
     */
    private List<Long> getGovIdsByRoleLevel(int level) {
        if (RoleLevelEnum.COUNTRY_LEVEL.getLevel() == level) {
            return Arrays.asList(BIZ_TOP_DEPT_ID);
        }else if (RoleLevelEnum.REGION_LEVEL.getLevel() == level) {
            return getRegionGovIdList();
        }else if (RoleLevelEnum.PROVINCE_LEVEL.getLevel() == level) {
            return getProvinceGovIdList();
        }else if (RoleLevelEnum.CITY_LEVEL.getLevel() == level) {
            return getCityGovIdList();
        }else if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == level) {
            return getBizCenterGovIdList();
        }else {
            return null;
        }
    }

    /**
     * 删除角色信息
     * @param role
     * @return
     */
    private ResultVo doDeleteRole(Role role) {
        if (RoleRangeEnum.INNER.getValue() == role.getRoleRange()) {
            ResultData rd = doInnerDelete(role);
            if (!isResultDataSuccess(rd)) {
                return BaseResultUtil.fail("删除角色失败，原因: " + rd.getMsg());
            }
        }else if (RoleRangeEnum.OUTER.getValue() == role.getRoleRange()) {

        }else {
            return BaseResultUtil.fail("角色类型：" + role.getRoleRange() + "无效，请检查数据");
        }
        //删除role记录
        baseMapper.deleteById(role.getId());
        return BaseResultUtil.success();
    }

    /**
     * 删除内部机构角色信息
     * @param role
     * @return
     */
    private ResultData doInnerDelete(Role role) {
        int level = role.getRoleLevel();
        List<Long> idList = null;
        if (RoleLevelEnum.COUNTRY_LEVEL.getLevel() == level) {
            idList = new ArrayList<>();
            idList.add(BIZ_TOP_DEPT_ID);
        }else if (RoleLevelEnum.REGION_LEVEL.getLevel() == level) {
            idList = getRegionGovIdList();
        }else if (RoleLevelEnum.PROVINCE_LEVEL.getLevel() == level) {
            idList = getProvinceGovIdList();
        }else if (RoleLevelEnum.CITY_LEVEL.getLevel() == level) {
            idList = getCityGovIdList();
        }else if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == level) {
            idList = getBizCenterGovIdList();
        }else {
            return ResultData.failed("角色级别：" + role.getRoleLevel() + "无效，请检查数据");
        }
        if (CollectionUtils.isEmpty(idList)) {
            return ResultData.failed("机构列表信息获取失败");
        }
        DeleteBatchRoleReq deleteRole = new DeleteBatchRoleReq();
        deleteRole.setRoleName(role.getRoleName());
        deleteRole.setDeptIdList(idList);
        ResultData rd = sysRoleService.deleteBatch(deleteRole);
        return rd;
    }

    /**
     * 添加角色
     * @param dto
     * @return
     */
    private ResultVo doAddRole(AddRoleDto dto) {
        if (RoleRangeEnum.INNER.getValue() == dto.getRoleRange()) {
            //内部机构
            List<Role> list = baseMapper.selectList(new QueryWrapper<Role>()
                    .eq("role_name", dto.getRoleName()));
            if (!CollectionUtils.isEmpty(list)) {
                return BaseResultUtil.fail("角色名称重复，请检查");
            }
            ResultData rd = doInnerAdd(dto);
            if (!isResultDataSuccess(rd)) {
                return BaseResultUtil.fail("保存角色信息失败");
            }
        }else if (RoleRangeEnum.OUTER.getValue() == dto.getRoleRange()) {
            //外部机构

        }else {
            return BaseResultUtil.fail("不支持此机构范围: " + dto.getRoleRange());
        }
        //此处维护韵车角色信息
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setRoleLevel(dto.getRoleLevel());
        role.setRoleRange(dto.getRoleRange());
        role.setUpdateTime(System.currentTimeMillis());
        role.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(role);
        return BaseResultUtil.success();
    }

    /**
     * 添加内部机构角色
     * @param dto
     * @return
     */
    private ResultData doInnerAdd(AddRoleDto dto) {
        int level = dto.getRoleLevel();
        if (RoleLevelEnum.COUNTRY_LEVEL.getLevel() == level) {
            //全国机构添加角色
            return addRoleForCountryGov(dto);
        }else if (RoleLevelEnum.REGION_LEVEL.getLevel() == level) {
            //大区机构添加角色(给所有大区各添加一个角色)
            return addRoleForRegionGov(dto);
        }else if (RoleLevelEnum.PROVINCE_LEVEL.getLevel() == level) {
            //省机构添加角色（给所有省机构各添加一个角色)
            return addRoleForProvinceGov(dto);
        }else if (RoleLevelEnum.CITY_LEVEL.getLevel() == level) {
            //给各城市添加角色（给所有城市各添加一个角色)
            return addRoleForCityGov(dto);
        }else if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == level) {
            //给各业务中心添加角色 (给所有业务中心各添加一个角色)
            return addRoleForBizCenterGov(dto);
        }else {
            return ResultData.failed("不支持此机构级别，请检查");
        }
    }

    /**
     * 添加内部角色：给全国机构添加角色
     * @param dto
     * @return
     */
    private ResultData addRoleForCountryGov(AddRoleDto dto) {
        //角色添加
        AddRoleReq role = new AddRoleReq();
        BeanUtils.copyProperties(dto, role);
        role.setDeptId(BIZ_TOP_DEPT_ID);
        return sysRoleService.save(role);
    }

    /**
     * 添加内部角色：给各个大区添加角色
     * @param dto
     * @return
     */
    private ResultData addRoleForRegionGov(AddRoleDto dto) {
        //给大区添加角色信息
        ResultData<List<SelectDeptResp>> deptListRd =
                sysDeptService.getSingleLevelDeptList(BIZ_TOP_DEPT_ID);
        if (!ReturnMsg.SUCCESS.getCode().equals(deptListRd.getCode())) {
            return ResultData.failed("查询机构信息错误，请检查");
        }
        List<SelectDeptResp> deptList = deptListRd.getData();
        if (CollectionUtils.isEmpty(deptList)) {
            return ResultData.ok("成功：但没有保存角色信息");
        }
        List<Long> deptIdList =
                deptList.stream().map(d -> d.getDeptId()).collect(Collectors.toList());
        AddBatchRoleReq role = new AddBatchRoleReq();
        BeanUtils.copyProperties(dto, role);
        role.setDeptIdList(deptIdList);
        return sysRoleService.saveBatch(role);
    }

    /**
     * 添加内部角色：给各省公司添加角色
     * @param dto
     * @return
     */
    private ResultData addRoleForProvinceGov(AddRoleDto dto) {
        List<Long> idList = getProvinceGovIdList();
        if (null == idList) {
            return ResultData.failed("获取省机构列表信息错误，请检查");
        }
        if (CollectionUtils.isEmpty(idList)) {
            return ResultData.ok("成功：但没有保存角色信息");
        }
        AddBatchRoleReq role = new AddBatchRoleReq();
        BeanUtils.copyProperties(dto, role);
        role.setDeptIdList(idList);
        return sysRoleService.saveBatch(role);
    }

    /**
     * 添加内部角色：给各城市公司添加角色
     * @param dto
     * @return
     */
    private ResultData addRoleForCityGov(AddRoleDto dto) {
        List<Long> idList = getCityGovIdList();
        if (null == idList) {
            return ResultData.failed("获取城市机构列表信息错误，请检查");
        }
        if (CollectionUtils.isEmpty(idList)) {
            return ResultData.ok("成功：但没有保存角色信息");
        }
        AddBatchRoleReq role = new AddBatchRoleReq();
        BeanUtils.copyProperties(dto, role);
        role.setDeptIdList(idList);
        return sysRoleService.saveBatch(role);
    }

    /**
     * 添加内部角色：给各业务中心添加角色
     * @param dto
     * @return
     */
    private ResultData addRoleForBizCenterGov(AddRoleDto dto) {
        List<Long> idList = getBizCenterGovIdList();
        if (null == idList) {
            return ResultData.failed("获取业务中心机构列表信息错误，请检查");
        }
        if (CollectionUtils.isEmpty(idList)) {
            return ResultData.ok("成功：但没有保存角色信息");
        }
        AddBatchRoleReq role = new AddBatchRoleReq();
        BeanUtils.copyProperties(dto, role);
        role.setDeptIdList(idList);
        return sysRoleService.saveBatch(role);
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
                .map(r -> r.getDeptId()).collect(Collectors.toList());
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
     * 返回结果状态是否正确
     * @param rd
     * @return
     */
    private boolean isResultDataSuccess(ResultData rd) {
        return ReturnMsg.SUCCESS.getCode().equals(rd.getCode())?true: false;
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
}
