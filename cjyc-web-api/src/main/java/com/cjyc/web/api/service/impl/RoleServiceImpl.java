package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddBatchRoleReq;
import com.cjkj.usercenter.dto.common.AddRoleReq;
import com.cjkj.usercenter.dto.common.DeleteBatchRoleReq;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dao.IRoleDao;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.enums.role.RoleLevelEnum;
import com.cjyc.common.model.enums.role.RoleRangeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.AdminListVo;
import com.cjyc.web.api.feign.ISysDeptService;
import com.cjyc.web.api.feign.ISysRoleService;
import com.cjyc.web.api.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 角色信息service实现类
 */

@Service
public class RoleServiceImpl extends ServiceImpl<IRoleDao, Role> implements IRoleService {
    /**
     * 社会车辆事业部机构ID, 因为是初始化数据，所以此ID为固定值
     */
    private static final Long BIZ_TOP_DEPT_ID = 120000L;

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;

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
    public ResultVo<List<SelectUsersByRoleResp>> getUsersByRoleId(Long roleId) {
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
        list.stream().forEach(l -> {
            if (RoleLevelEnum.BIZ_CENTER_LEVEL.getLevel() == role.getRoleLevel()) {
                l.setBizCenter(l.getBizCity());
                l.setBizCity(null);
            }
        });
        return BaseResultUtil.success(list);
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
        if (RoleRangeEnum.INNER.getValue() == dto.getRange()) {
            //内部机构
            ResultData rd = doInnerAdd(dto);
            if (!isResultDataSuccess(rd)) {
                return BaseResultUtil.fail("保存角色信息失败");
            }
        }else if (RoleRangeEnum.OUTER.getValue() == dto.getRange()) {
            //外部机构

        }else {
            return BaseResultUtil.fail("不支持此机构范围: " + dto.getRange());
        }
        //此处维护韵车角色信息
        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        role.setRoleLevel(dto.getLevel());
        role.setRoleRange(dto.getRange());
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
        int level = dto.getLevel();
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
        ResultData<List<SelectDeptResp>> deptListRd =
                sysDeptService.getSingleLevelDeptList(BIZ_TOP_DEPT_ID);
        if (!ReturnMsg.SUCCESS.getCode().equals(deptListRd.getCode())) {
            return null;
        }
        if (CollectionUtils.isEmpty(deptListRd.getData())) {
            return null;
        }
        List<Long> idList = new ArrayList<>();
        AtomicBoolean hasError = new AtomicBoolean(false);
        deptListRd.getData().stream().forEach(g -> {
            ResultData<List<SelectDeptResp>> rsListRd =
                    sysDeptService.getSingleLevelDeptList(g.getDeptId());
            if (!isResultDataSuccess(rsListRd)) {
                hasError.set(true);
                return;
            }
            if (!CollectionUtils.isEmpty(rsListRd.getData())) {
                idList.addAll(rsListRd.getData().stream()
                        .map(d -> d.getDeptId()).collect(Collectors.toList()));
            }
        });
        if (hasError.get()) {
            return null;
        }
        return idList;
    }

    /**
     * 获取城市机构id列表信息
     * @return
     */
    private List<Long> getCityGovIdList() {
        List<Long> provinceGovIds = getProvinceGovIdList();
        if (CollectionUtils.isEmpty(provinceGovIds)) {
            return null;
        }
        List<Long> cityGovIdList = new ArrayList<>();
        AtomicBoolean hasError = new AtomicBoolean(false);
        provinceGovIds.stream().forEach(pid -> {
            ResultData<List<SelectDeptResp>> rsListRd =
                    sysDeptService.getSingleLevelDeptList(pid);
            if (!isResultDataSuccess(rsListRd)) {
                hasError.set(true);
                return;
            }
            if (!CollectionUtils.isEmpty(rsListRd.getData())) {
                cityGovIdList.addAll(rsListRd.getData().stream()
                        .map(d -> d.getDeptId()).collect(Collectors.toList()));
            }
        });
        if (hasError.get()) {
            return null;
        }
        return cityGovIdList;
    }

    /**
     * 获取业务中心id列表信息
     * @return
     */
    private List<Long> getBizCenterGovIdList() {
        List<Long> cityGovIds = getCityGovIdList();
        if (CollectionUtils.isEmpty(cityGovIds)) {
            return null;
        }
        List<Long> bizCenterGovIdList = new ArrayList<>();
        AtomicBoolean hasError = new AtomicBoolean(false);
        cityGovIds.stream().forEach(cid -> {
            ResultData<List<SelectDeptResp>> rsListRd =
                    sysDeptService.getSingleLevelDeptList(cid);
            if (!isResultDataSuccess(rsListRd)) {
                hasError.set(true);
                return;
            }
            if (!CollectionUtils.isEmpty(rsListRd.getData())) {
                bizCenterGovIdList.addAll(rsListRd.getData().stream()
                        .map(d -> d.getDeptId()).collect(Collectors.toList()));
            }
        });
        if (hasError.get()) {
            return null;
        }
        return bizCenterGovIdList;
    }

    /**
     * 返回结果状态是否正确
     * @param rd
     * @return
     */
    private boolean isResultDataSuccess(ResultData rd) {
        return ReturnMsg.SUCCESS.getCode().equals(rd.getCode())?true: false;
    }
}
