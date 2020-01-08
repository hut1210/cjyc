package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.*;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.dao.IUserRoleDeptDao;
import com.cjyc.common.model.dto.web.salesman.AddDto;
import com.cjyc.common.model.dto.web.salesman.AssignRoleDto;
import com.cjyc.common.model.dto.web.salesman.AssignRoleNewDto;
import com.cjyc.common.model.dto.web.salesman.ResetStateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.entity.UserRoleDept;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.saleman.SalemanStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.util.ResultDataUtil;
import com.cjyc.web.api.service.IRoleService;
import com.cjyc.web.api.service.ISalesmanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 业务员service
 */
@Service
public class SalesmanServiceImpl extends ServiceImpl<IAdminDao, Admin> implements ISalesmanService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private IRoleService roleService;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ICustomerDao customerDao;
    /**
     * 社会车辆事业部机构id
     */
    @Value("${cjkj.dept_admin_id}")
    private Long YC_CT_DEPT_ID;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo saveAdmin(AddDto dto) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(dto, admin);
        admin.setCreateTime(System.currentTimeMillis());
        //物流平台用户创建调用
        if (dto.getId() != null && dto.getId() > 0L) {
            //更新
            admin.setCreateUser(null);
            admin.setCreateUserId(null);
            return updateUser(admin, dto);
        }else {
            //新增
            return addUser(admin, dto);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo assignRoles(AssignRoleDto dto) {
        Admin admin = baseMapper.selectById(dto.getId());
        if (null == admin) {
            return BaseResultUtil.fail("根据用户id: " + dto.getId() + "未获取到用户信息，请检查");
        }
        ResultData<Set<Long>> roleIdRd = resolveRoleIdsFromPlatform(dto);
        if (!ReturnMsg.SUCCESS.getCode().equals(roleIdRd.getCode())){
            return BaseResultUtil.fail("获取平台角色id列表信息错误，请检查");
        }

        if (!CollectionUtils.isEmpty(roleIdRd.getData())) {
            UpdateUserReq user = new UpdateUserReq();
            user.setUserId(admin.getUserId());
            user.setRoleIdList(new ArrayList<>(roleIdRd.getData()));
            ResultData updateRd = sysUserService.updateUser(user);
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                return BaseResultUtil.fail("分配角色信息失败");
            }
        }
        admin.setBizDesc(dto.getBizDesc());
        this.updateById(admin);
        return BaseResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo resetState(ResetStateDto dto) {
        Admin admin = baseMapper.selectById(dto.getId());
        if (null == admin) {
            return BaseResultUtil.fail("根据用户id：" + dto.getId() + "未查询到用户信息");
        }
        UpdateUserReq updateUser = new UpdateUserReq();
        updateUser.setUserId(admin.getUserId());
        updateUser.setStatus(dto.getState().equals(1)?1:0);
        ResultData rd = sysUserService.updateUser(updateUser);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("用户状态设置失败，原因：" + rd.getMsg());
        }
        admin.setState(
                dto.getState().equals(1)?SalemanStateEnum.CHECKED.code: SalemanStateEnum.LEAVE.code);
        this.updateById(admin);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo resetPwd(Long id) {
        //重置用户密码
        Admin admin = baseMapper.selectById(id);
        if (admin != null) {
            ResultData rd =
                    sysUserService.resetPwd(admin.getUserId(),
                            YmlProperty.get("cjkj.salesman.password"));
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return BaseResultUtil.fail("重置密码错误，原因：" + rd.getMsg());
            }
        }
        return BaseResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo assignRolesNew(AssignRoleNewDto dto) {
        Admin admin = baseMapper.selectById(dto.getId());
        if (null == admin || admin.getUserId() == null || admin.getUserId() <= 0L) {
            return BaseResultUtil.fail("用户信息有误，请确认");
        }
        Role role = roleService.getById(dto.getRoleId());
        if (null == role || role.getRoleId() == null || role.getRoleId() <= 0L) {
            return BaseResultUtil.fail("角色信息有误，请确认");
        }
        UpdateUserReq user = new UpdateUserReq();
        user.setUserId(admin.getUserId());
        //查询当前用户的非业务员角色列表
        List<Long> nonSalesmanRoleIds = userRoleDeptDao.getNonSalesmanRoleIds(admin.getUserId());
        if (!CollectionUtils.isEmpty(nonSalesmanRoleIds)) {
            nonSalesmanRoleIds.add(role.getRoleId());
            user.setRoleIdList(nonSalesmanRoleIds);
        }else {
            user.setRoleIdList(Arrays.asList(role.getRoleId()));
        }
        ResultData updateRd = sysUserService.updateUser(user);
        if (!ResultDataUtil.isSuccess(updateRd)) {
            return BaseResultUtil.fail("分配角色信息失败，原因：" + updateRd.getMsg());
        }
        //用户角色机构信息同步
        updateUserRoleDept(dto);
        admin.setBizDesc(dto.getBizDesc());
        this.updateById(admin);
        return BaseResultUtil.success();
    }

    /**
     * 解析角色id列表信息： 从物流平台
     * @param dto
     * @return
     */
    private ResultData<Set<Long>> resolveRoleIdsFromPlatform(AssignRoleDto dto) {
        Set<Long> roleIds = new HashSet<>();
        AtomicReference<Boolean> hasError = new AtomicReference<>(Boolean.FALSE);
        if (dto.getDeptType().equals(1)) {
            if (!CollectionUtils.isEmpty(dto.getRoleDeptList())) {
                //解析角色id列表信息
                dto.getRoleDeptList().stream().forEach(rd -> {
                    if (!StringUtils.isEmpty(rd.getRoleName())
                            && !CollectionUtils.isEmpty(rd.getDeptIdList())) {
                        rd.getDeptIdList().stream().forEach(dId -> {
                            ResultData<List<SelectRoleResp>> rolesRd =
                                    sysUserService.getSingleLevelRolesByDeptId(dId);
                            if (!ReturnMsg.SUCCESS.getCode().equals(rolesRd.getCode())) {
                                hasError.set(true);
                                return;
                            }
                            if (!CollectionUtils.isEmpty(rolesRd.getData())) {
                                rolesRd.getData().stream().forEach(r -> {
                                    if (rd.getRoleName().equals(r.getRoleName())) {
                                        roleIds.add(r.getRoleId());
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }else if(dto.getDeptType().equals(2)) {
            if (!CollectionUtils.isEmpty(dto.getRoleDeptCodeList())) {
                //解析角色id列表信息
                dto.getRoleDeptCodeList().stream().forEach(rd -> {
                    if (!StringUtils.isEmpty(rd.getRoleName())
                            && !CollectionUtils.isEmpty(rd.getDeptCodeList())) {
                        rd.getDeptCodeList().stream().forEach(dCode -> {
                            ResultData<SelectDeptResp> deptByCityCodeRd = sysDeptService.getDeptByCityCode(dCode);
                            if (!ReturnMsg.SUCCESS.getCode().equals(deptByCityCodeRd.getCode())) {
                                hasError.set(true);
                                return;
                            }
                            if (deptByCityCodeRd.getData() != null) {
                                ResultData<List<SelectRoleResp>> rolesRd =
                                        sysUserService.getSingleLevelRolesByDeptId(
                                                deptByCityCodeRd.getData().getDeptId());
                                if (!ReturnMsg.SUCCESS.getCode().equals(rolesRd.getCode())) {
                                    hasError.set(true);
                                    return;
                                }
                                if (!CollectionUtils.isEmpty(rolesRd.getData())) {
                                    rolesRd.getData().stream().forEach(r -> {
                                        if (rd.getRoleName().equals(r.getRoleName())) {
                                            roleIds.add(r.getRoleId());
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        } else {
            return  ResultData.failed("不支持的机构类型，请检查");
        }
        if (hasError.get()) {
            return ResultData.failed("获取机构下角色信息异常，请检查");
        }
        return ResultData.ok(roleIds);
    }

    /**
     * 实际授权操作
     * @param userId
     * @param roleName
     * @param deptIdList
     * @return
     */
    private ResultData doAssign(Long userId, String roleName, List<Long> deptIdList){
        //TODO 将角色信息授权给指定用户
        return null;
    }

    /**
     * 更新用户信息
     * @param admin
     * @param dto
     * @return
     */
    private ResultVo updateUser(Admin admin, AddDto dto){
        UpdateUserReq updateUser = new UpdateUserReq();
        packUpdateUserCommonInfo(updateUser, dto);
        //校验规则
        List<Admin> list = this.list(new QueryWrapper<Admin>()
                .eq("phone", dto.getPhone())
                .ne("id", dto.getId()));
        if (!CollectionUtils.isEmpty(list)) {
            return BaseResultUtil.fail("手机号已被使用，请检查");
        }
        ResultData<UserResp> accountRd = sysUserService.getByAccount(dto.getAccount());
        if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
            return BaseResultUtil.fail("用户信息更新失败， 原因:" + accountRd.getMsg());
        }
       /* if (accountRd.getData() != null) {
            return BaseResultUtil.fail("被更新账号已存在，请检查");
        }*/
        ResultData rd = sysUserService.update(updateUser);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())){
            return BaseResultUtil.fail("用户信息更新失败, 原因：" + rd.getMsg());
        }
        this.saveOrUpdate(admin);
        return BaseResultUtil.success();
    }

    /**
     * 新增用户信息
     * @param admin
     * @param dto
     * @return
     */
    private ResultVo addUser(Admin admin, AddDto dto){
        List<Admin> phoneList = this.list(new QueryWrapper<Admin>()
                .eq("phone", dto.getPhone()));
        if (!CollectionUtils.isEmpty(phoneList)) {
            return BaseResultUtil.fail("手机号已存在，请检查");
        }
        ResultData<UserResp> existRd = sysUserService.getByAccount(dto.getAccount());
        if (!ReturnMsg.SUCCESS.getCode().equals(existRd.getCode())) {
            return BaseResultUtil.fail("用户信息保存失败，原因：根据账号" + dto.getAccount() +
                    "获取物流平台用户信息失败");
        }
        Long userId = null;
        if (existRd.getData() != null && existRd.getData().getUserId() != null
                && existRd.getData().getUserId() > 0L){
            //物流平台以存在此用户，不需要新增
            userId = existRd.getData().getUserId();
        }else {
            //物流平台不存在此账号，需要新增
            AddUserReq user = new AddUserReq();
            packAddUserCommonInfo(user, dto);
            user.setDeptId(YC_CT_DEPT_ID);
            ResultData<AddUserResp> saveRd = sysUserService.save(user);
            if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
                return BaseResultUtil.fail("物流平台用户保存失败，原因：" + saveRd.getMsg());
            }
            userId = saveRd.getData().getUserId();
        }
        if (null != userId) {
            //韵车用户添加
            admin.setUserId(userId);
            admin.setState(SalemanStateEnum.CHECKED.code);
            this.saveOrUpdate(admin);
        }else {
            return BaseResultUtil.fail("物流平台新增用户信息失败");
        }
        return BaseResultUtil.success();
    }

    /**
     * 请求信息封装到物理平台更新用户信息
     * @param user 封装后用户信息
     * @param dto
     * @return
     */
    private void packUpdateUserCommonInfo(UpdateUserReq user, AddDto dto){
        //封装用户信息
        Admin admin = baseMapper.selectById(dto.getId());
        BeanUtils.copyProperties(dto, user);
        user.setUserId(admin.getUserId());
        user.setMobile(dto.getPhone());
        if (dto.getState() != null){
            user.setStatus(SalemanStateEnum.CHECKED.code == dto.getState()?1:0);
        }
    }

    /**
     * 请求信息封装到物流平台用户添加信息
     * @param user
     * @param dto
     * @return
     */
    private void packAddUserCommonInfo(AddUserReq user, AddDto dto) {
        //用户信息封装
        BeanUtils.copyProperties(dto, user);
        user.setMobile(dto.getPhone());
        user.setPassword(YmlProperty.get("cjkj.salesman.password"));
    }

    /**
     * 更新用户角色机构关系
     * @param dto
     */
    private void updateUserRoleDept(AssignRoleNewDto dto) {
        userRoleDeptDao.delete(new QueryWrapper<UserRoleDept>().lambda()
            .eq(UserRoleDept::getUserId, dto.getId())
            .eq(UserRoleDept::getDeptType, UserTypeEnum.ADMIN.code)
            .eq(UserRoleDept::getUserType, UserTypeEnum.ADMIN.code));
        dto.getDeptIdList().forEach(dId -> {
            //关系表中存在有效关系无需变更否则新增一条
            List<UserRoleDept> userRoleDeptList = userRoleDeptDao.selectList(new QueryWrapper<UserRoleDept>()
                    .eq("user_id", dto.getId())
                    .eq("role_id", dto.getRoleId())
                    .eq("dept_id", dId)
                    .eq("dept_type", UserTypeEnum.ADMIN.code)
                    .eq("user_type", UserTypeEnum.ADMIN.code)
                    .eq("state", UseStateEnum.USABLE.code));
            if (CollectionUtils.isEmpty(userRoleDeptList)) {
                UserRoleDept userRoleDept = new UserRoleDept();
                userRoleDept.setUserId(dto.getId());
                userRoleDept.setRoleId(dto.getRoleId());
                userRoleDept.setDeptId(dId);
                userRoleDept.setDeptLevel(dto.getDeptType().equals(5)?5: 1);
                userRoleDept.setDeptType(UserTypeEnum.ADMIN.code);
                userRoleDept.setUserType(UserTypeEnum.ADMIN.code);
                userRoleDept.setCreateTime(NOW);
                userRoleDept.setCreateUserId(dto.getLoginId());
                userRoleDeptDao.insert(userRoleDept);
            }else {
                userRoleDeptList.forEach(ur -> {
                    ur.setUpdateTime(NOW);
                    ur.setUpdateUserId(dto.getLoginId());
                    userRoleDeptDao.updateById(ur);
                });
            }
        });
    }
}
