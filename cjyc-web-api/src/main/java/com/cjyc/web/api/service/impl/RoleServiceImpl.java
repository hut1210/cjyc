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
import com.cjyc.common.model.dto.web.role.SetRoleForAppDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.role.RoleBtnForAppEnum;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.enums.role.RoleRangeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.SelectUserByRoleVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.util.ClpDeptUtil;
import com.cjyc.common.system.util.ResultDataUtil;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IRoleService;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
    @Resource
    private ClpDeptUtil clpDeptUtil;
    @Resource
    private IRoleDao roleDao;

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
            if (!StringUtils.isEmpty(vo.getRoles())) {
                String[] roles = vo.getRoles().split(",");
                Set<String> rSet;
                if (roles.length > 1) {
                    rSet = Sets.newHashSet(roles);
                    StringBuilder sb = new StringBuilder();
                    rSet.forEach(r -> {
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append(r);
                    });
                    vo.setRoles(sb.toString());
                }
            }
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
        if (!ResultDataUtil.isSuccess(byIdRd)) {
            return BaseResultUtil.fail("角色信息查询错误，原因：" + byIdRd.getMsg());
        }
        if (null == byIdRd.getData()) {
            return BaseResultUtil.success(0);
        }
        Long roleDeptId = byIdRd.getData().getDeptId();
        ResultData<List<SelectDeptResp>> multiDeptRd =
                sysDeptService.getMultiLevelDeptList(Long.parseLong(YmlProperty.get("cjkj.dept_customer_id")));
        if (!ResultDataUtil.isSuccess(multiDeptRd)) {
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
        if (clpDeptUtil.getRegionGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (clpDeptUtil.getProvinceGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (clpDeptUtil.getCityGovIdList().contains(roleDeptId)) {
            return BaseResultUtil.success(UserTypeEnum.ADMIN.code);
        }
        if (clpDeptUtil.getBizCenterGovIdList() != null && clpDeptUtil.getBizCenterGovIdList().contains(roleDeptId)) {
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
        if (!ResultDataUtil.isSuccess(rd)) {
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
        if (!ResultDataUtil.isSuccess(rolesRd)) {
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
        if (!ResultDataUtil.isSuccess(rsRd)) {
            return BaseResultUtil.fail("查询错误，原因：" + rsRd.getMsg());
        }
        if (CollectionUtils.isEmpty(rsRd.getData())) {
            return BaseResultUtil.success();
        }else {
            List<String> rsList = rsRd.getData().stream().map(id -> String.valueOf(id)).collect(Collectors.toList());
            return BaseResultUtil.success(rsList);
        }
    }

    /*********************************韵车集成改版 st*****************************/
    @Override
    public ResultVo addRoleNew(AddRoleDto dto) {
        List<Role> list = baseMapper.selectList(new QueryWrapper<Role>()
                .eq("role_name", dto.getRoleName()));
        if (!CollectionUtils.isEmpty(list)) {
            return BaseResultUtil.fail("角色名称重复，请检查");
        }
        //角色添加
        AddRoleReq roleReq = new AddRoleReq();
        BeanUtils.copyProperties(dto, roleReq);
        roleReq.setDeptId(BIZ_TOP_DEPT_ID);
        ResultData<AddRoleResp> saveRd = sysRoleService.save(roleReq);
        if (!ResultDataUtil.isSuccess(saveRd)) {
            return BaseResultUtil.fail("角色信息保存失败，原因：" + saveRd.getMsg());
        }
        //此处维护韵车角色信息
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setRoleLevel(dto.getRoleLevel());
        role.setRoleRange(dto.getRoleRange());
        //将物流平台角色标识存储
        Long clpRoleId = saveRd.getData().getRoleId();
        role.setRoleId(clpRoleId);
        role.setUpdateTime(System.currentTimeMillis());
        role.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(role);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<List<SelectUserByRoleVo>> getUsersByRoleIdNew(Long roleId) {
        List<SelectUserByRoleVo> userList = baseMapper.getUsersByRoleId(roleId);
        if (CollectionUtils.isEmpty(userList)) {
            return BaseResultUtil.success();
        }
        userList.forEach(u -> {
            if (!StringUtils.isEmpty(u.getRoles())) {
                String[] roles = u.getRoles().split(",");
                Set<String> rSet;
                if (roles.length > 1) {
                    rSet = Sets.newHashSet(roles);
                    StringBuilder sb = new StringBuilder();
                    rSet.forEach(r -> {
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append(r);
                    });
                    u.setRoles(sb.toString());
                }
            }
        });
        return BaseResultUtil.success(userList);
    }

    @Override
    public ResultVo<List<Role>> getAllListNew(String roleName) {
        return BaseResultUtil.success(this.list(new QueryWrapper<Role>()
                .like(!StringUtils.isEmpty(roleName), "role_name", roleName)
                .eq("role_range", RoleRangeEnum.INNER.getValue())));
    }

    @Override
    public ResultVo<List<String>> getBtmMenuIdsByRoleIdNew(Long roleId) {
        Role role = baseMapper.selectById(roleId);
        if (null == role || role.getRoleId() == null || role.getRoleId() <= 0L) {
            return BaseResultUtil.fail("角色信息错误，请检查");
        }
        ResultData<List<Long>> rsRd = sysRoleService.getBottomMenuIdsByRoleId(role.getRoleId());
        if (!ResultDataUtil.isSuccess(rsRd)) {
            return BaseResultUtil.fail("查询错误，原因：" + rsRd.getMsg());
        }
        if (CollectionUtils.isEmpty(rsRd.getData())) {
            return BaseResultUtil.success();
        }else {
            List<String> rsList = rsRd.getData().stream().map(id -> String.valueOf(id)).collect(Collectors.toList());
            return BaseResultUtil.success(rsList);
        }
    }

    @Override
    public ResultVo modifyRoleMenusNew(ModifyRoleMenusDto dto) {
        Role role = baseMapper.selectById(dto.getId());
        if (null == role) {
            return BaseResultUtil.success();
        }
        UpdateBatchRoleMenusReq req = new UpdateBatchRoleMenusReq();
        req.setDeptIdList(Arrays.asList(BIZ_TOP_DEPT_ID));
        req.setMenuIdList(dto.getMenuIdList());
        req.setRoleName(role.getRoleName());
        ResultData rd = sysRoleService.batchUpdateRoleMenus(req);
        if (!ResultDataUtil.isSuccess(rd)) {
            return BaseResultUtil.fail("变更角色菜单列表失败，原因: " + rd.getMsg());
        }
        return BaseResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo setRoleForApp(SetRoleForAppDto dto) {
        Role role = roleDao.selectById(dto.getRoleId());
        if (null == role) {
            return BaseResultUtil.fail("根据角色id：{0}, 未获取到角色信息",
                    dto.getRoleId());
        }
        if (dto.getLoginApp() != null && (dto.getLoginApp().equals(1)
                || dto.getLoginApp().equals(2))) {
            role.setLoginApp(dto.getLoginApp());
        }
        if (!CollectionUtils.isEmpty(dto.getAppBtnList())) {
            List<String> btnList = new ArrayList<>();
            dto.getAppBtnList().forEach(bId -> {
                switch (bId) {
                    case 1:
                        if (!btnList.contains(RoleBtnForAppEnum.ALLOCATE_ORDER.name)) {
                            btnList.add(RoleBtnForAppEnum.ALLOCATE_ORDER.name);
                        }
                        break;
                    case 2:
                        if (!btnList.contains(RoleBtnForAppEnum.PICK_DISPATCH.name)) {
                            btnList.add(RoleBtnForAppEnum.PICK_DISPATCH.name);
                        }
                        break;
                    case 3:
                        if (!btnList.contains(RoleBtnForAppEnum.TRUNK_DISPATCH.name)) {
                            btnList.add(RoleBtnForAppEnum.TRUNK_DISPATCH.name);
                        }
                        break;
                    case 4:
                        if (!btnList.contains(RoleBtnForAppEnum.SEND_DISPATCH.name)) {
                            btnList.add(RoleBtnForAppEnum.SEND_DISPATCH.name);
                        }
                        break;
                    default:
                        log.debug("按钮类型不匹配");
                        break;
                }
            });
            if (!CollectionUtils.isEmpty(btnList)) {
                StringBuilder sb = new StringBuilder();
                btnList.forEach(btn -> {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(btn);
                });
                role.setAppBtns(sb.toString());
            }
        }
        roleDao.updateById(role);
        return BaseResultUtil.success();
    }
    /*********************************韵车集成改版 ed*****************************/


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
            return clpDeptUtil.getRegionGovIdList();
        }else if (RoleLevelEnum.PROVINCE_LEVEL.getLevel() == level) {
            return clpDeptUtil.getProvinceGovIdList();
        }else if (RoleLevelEnum.CITY_LEVEL.getLevel() == level) {
            return clpDeptUtil.getCityGovIdList();
        }else if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == level) {
            return clpDeptUtil.getBizCenterGovIdList();
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
            if (!ResultDataUtil.isSuccess(rd)) {
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
            idList = clpDeptUtil.getRegionGovIdList();
        }else if (RoleLevelEnum.PROVINCE_LEVEL.getLevel() == level) {
            idList = clpDeptUtil.getProvinceGovIdList();
        }else if (RoleLevelEnum.CITY_LEVEL.getLevel() == level) {
            idList = clpDeptUtil.getCityGovIdList();
        }else if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == level) {
            idList = clpDeptUtil.getBizCenterGovIdList();
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
            if (!ResultDataUtil.isSuccess(rd)) {
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
        List<Long> idList = clpDeptUtil.getProvinceGovIdList();
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
        List<Long> idList = clpDeptUtil.getCityGovIdList();
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
        List<Long> idList = clpDeptUtil.getBizCenterGovIdList();
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
}
