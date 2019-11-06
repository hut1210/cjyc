package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dto.web.salesman.AddDto;
import com.cjyc.common.model.dto.web.salesman.AssignRoleDto;
import com.cjyc.common.model.dto.web.salesman.ResetStateDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.saleman.SalemanStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.web.api.service.ISalesmanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 业务员service
 */
@Service
public class SalesmanServiceImpl extends ServiceImpl<IAdminDao, Admin> implements ISalesmanService {

    @Autowired
    private ISysUserService sysUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo saveAdmin(AddDto dto) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(dto, admin);
        admin.setCreateTime(System.currentTimeMillis());
        //物流平台用户创建调用
        if (dto.getId() != null && dto.getId() > 0L) {
            //更新
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

    /**
     * 解析角色id列表信息： 从物流平台
     * @param dto
     * @return
     */
    private ResultData<Set<Long>> resolveRoleIdsFromPlatform(AssignRoleDto dto) {
        Set<Long> roleIds = new HashSet<>();
        AtomicReference<Boolean> hasError = new AtomicReference<>(Boolean.FALSE);
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
        ResultData rd = sysUserService.update(updateUser);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())){
            return BaseResultUtil.fail("用户信息更新失败");
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
        ResultData<AddUserResp> existRd = sysUserService.getByAccount(dto.getAccount());
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
}
