package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.*;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.driver.mine.CarrierDriverNameDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.enums.role.RoleNameEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.CarrierDriverVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsDriverServiceImpl implements ICsDriverService {
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsVehicleService csVehicleService;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsUserRoleDeptService csUserRoleDeptService;
    @Resource
    private IRoleDao roleDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public Driver getById(Long userId, boolean b) {
        return driverDao.selectById(userId);
    }

    @Override
    public Driver getByUserId(Long userId) {
        return driverDao.findByUserId(userId);
    }

    @Override
    public ResultData<Long> saveDriverToPlatform(Driver driver) {
        if (null == driver) {
            return ResultData.failed("司机信息错误，请检查");
        }
        ResultData<UserResp> accountRd = sysUserService.getByAccount(driver.getPhone());
        if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
            return ResultData.failed("司机信息查询失败：原因：" + accountRd.getMsg());
        }

        if (accountRd.getData() != null) {
            //司机信息已存在
            return ResultData.ok(accountRd.getData().getUserId());
        }else {
            //司机信息不存在，需新增
            AddUserReq user = new AddUserReq();
            user.setAccount(driver.getPhone());
            user.setPassword(YmlProperty.get("cjkj.driver.password"));
            user.setMobile(driver.getPhone());
            user.setName(driver.getName());
            user.setDeptId(Long.parseLong(YmlProperty.get("cjkj.dept_driver_id")));
            ResultData<AddUserResp> saveRd = sysUserService.save(user);
            if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
                return ResultData.failed("保存司机账户信息失败，原因：" + saveRd.getMsg());
            }
            return ResultData.ok(saveRd.getData().getUserId());
        }
    }

    @Override
    public ResultData updateUserToPlatform(Driver driver) {
        UpdateUserReq user = new UpdateUserReq();
        user.setUserId(driver.getUserId());
        user.setName(driver.getName());
        user.setAccount(driver.getPhone());
        user.setMobile(driver.getPhone());
        return sysUserService.updateUser(user);
    }

    @Override
    public ResultVo saveOrModifyDriver(CarrierDriverDto dto) {
        if(dto.getCarrierId() == null && dto.getRoleId() != null){
            CarrierDriverCon carrierDriCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                    .eq(CarrierDriverCon::getId, dto.getRoleId()));
            if(carrierDriCon == null){
                //web承运商管理员登陆
                Carrier carrier = csSysService.getCarrierByRoleId(dto.getRoleId());
                if(carrier != null){
                    dto.setCarrierId(carrier.getId());
                }
            }else{
                //司机端app管理员登录
                dto.setCarrierId(carrierDriCon.getCarrierId());
            }
        }
        if(dto.getCarrierId() != null){
            Carrier carrier = carrierDao.selectById(dto.getCarrierId());
            if(carrier.getState() != CommonStateEnum.CHECKED.code){
                return BaseResultUtil.fail("该承运商审核未通过，不可添加司机");
            }
        }
        //验证在个人司机池中是否存在
        Integer count = carrierDao.existPersonalCarrier(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于个人司机中");
        }
        //验证在该承运商下是否有相同的
        count = carrierDao.existBusinessCarrier(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于该企业承运商中");
        }
        if(dto.getDriverId() == null){
            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null){
                boolean result = csVehicleService.verifyDriverVehicle(null, dto.getVehicleId());
                if(!result){
                    return BaseResultUtil.fail("该车辆已绑定，请检查");
                }
            }
            Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                            .eq(Driver::getPhone, dto.getPhone()));
            if(driver == null){
                //保存司机
                driver = new Driver();
                BeanUtils.copyProperties(dto,driver);
                driver.setName(dto.getRealName());
                driver.setType(DriverTypeEnum.SOCIETY.code);
                driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
                driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
                if(dto.getRoleId() == null){
                    //业务员登陆
                    driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
                }else{
                    //承运商超级管理员登陆
                    driver.setSource(DriverSourceEnum.CARRIER_ADMIN.code);
                }
                driver.setCreateUserId(dto.getLoginId());
                driver.setCreateTime(NOW);
                driverDao.insert(driver);
            }
            //保存司机与承运商关系
            CarrierDriverCon driverCon = new CarrierDriverCon();
            driverCon.setCarrierId(dto.getCarrierId());
            driverCon.setDriverId(driver.getId());
            driverCon.setMode(dto.getMode());
            driverCon.setState(CommonStateEnum.CHECKED.code);
            driverCon.setRole(DriverRoleEnum.SUB_DRIVER.code);
            carrierDriverConDao.insert(driverCon);

            //司机信息保存
            ResultData<Long> rd = addDriverToPlatform(driver, dto.getCarrierId());
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return BaseResultUtil.fail(rd.getMsg());
            }
            //把userId更新到driver
            driver.setUserId(rd.getData());
            driverDao.updateById(driver);

            //车牌号不为空
            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null){
                //保存司机与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setVehicleId(dto.getVehicleId());
                dvc.setDriverId(driver.getId());
                driverVehicleConDao.insert(dvc);

                //保存运力信息
                VehicleRunning vr = new VehicleRunning();
                vr.setDriverId(driver.getId());
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(RunningStateEnum.EFFECTIVE.code);
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vr.setCreateTime(NOW);
                vehicleRunningDao.insert(vr);
            }
            return BaseResultUtil.success();
        }else{
            return modifyDriver(dto);
        }
    }

    @Override
    public ResultData<Long> addDriverToPlatform(Driver driver, Long carrierId) {
        Carrier carrier = carrierDao.selectById(carrierId);
        if (null == carrier || carrier.getDeptId() == null || carrier.getDeptId() <= 0L) {
            return ResultData.failed("承运商信息错误，可能因为该承运商未审核通过");
        }
        ResultData<UserResp> rd = sysUserService.getByAccount(driver.getPhone());
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return ResultData.failed("查询司机信息有误，原因：" + rd.getMsg());
        }
        if (rd.getData() != null) {
            //司机已存在
            return ResultData.ok(rd.getData().getUserId());
        }else {
            CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getCarrierId, carrierId)
                    .eq(CarrierDriverCon::getDriverId, driver.getId()));
            ResultData<List<SelectRoleResp>> resultData = sysRoleService.getSingleLevelList(carrier.getDeptId());
            if (!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
                return ResultData.failed("查询组织下的所有角色失败");
            }
            //封装获取用户角色
            ResultVo<Long> roleId = csDriverService.findRoleId(resultData.getData(), cdc);
            List<Long> roleIds = new ArrayList<>(1);
            roleIds.add(roleId.getData());

            //司机不存在, 需新增
            AddUserReq userReq = new AddUserReq();
            userReq.setAccount(driver.getPhone());
            userReq.setDeptId(carrier.getDeptId());
            userReq.setRoleIdList(roleIds);
            userReq.setPassword(YmlProperty.get("cjkj.salesman.password"));
            userReq.setMobile(driver.getPhone());
            userReq.setName(driver.getName());
            ResultData<AddUserResp> saveRd = sysUserService.save(userReq);
            if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
                return ResultData.failed("司机账户保存失败，原因：" + saveRd.getMsg());
            }
            return ResultData.ok(saveRd.getData().getUserId());
        }
    }

    @Override
    public ResultData updateDriverToPlatform(CarrierDriverDto dto) {
        Driver driver = driverDao.selectById(dto.getDriverId());
        if (null == driver) {
            return ResultData.failed("司机信息错误，根据id：" + dto.getDriverId() + "未查询到信息");
        }
        //比对是否需要变更手机号
        if (!driver.getPhone().equals(dto.getPhone())) {
            //需要手机号变更操作
            //手机号是否在司机表存在
            List<Driver> drivers = driverDao.selectList(new QueryWrapper<Driver>().lambda()
                    .eq(Driver::getPhone, dto.getPhone()));
            if (!CollectionUtils.isEmpty(drivers)) {
                return ResultData.failed("手机号已使用，请检查");
            }
            ResultData<UserResp> rd = sysUserService.getByAccount(dto.getPhone());
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return ResultData.failed("司机信息查询失败， 原因：" + rd.getMsg());
            }
            if (rd.getData() != null) {
                return ResultData.failed("新手机号已存在物流平台，请检查");
            }
            UpdateUserReq userReq = new UpdateUserReq();
            userReq.setUserId(driver.getUserId());
            userReq.setAccount(dto.getPhone());
            userReq.setMobile(dto.getPhone());
            ResultData updateRd = sysUserService.update(userReq);
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                return ResultData.failed("更新用户信息失败，原因：" + updateRd.getMsg());
            }
            return ResultData.ok("成功");
        }else {
            //不需要变更手机号
            return ResultData.ok("成功");
        }
    }

    @Override
    public Driver validate(Long driverId) {
        Driver driver = getById(driverId, true);
        if (driver == null) {
            throw new ParameterException("当前用户，不能执行操作");
        }
        return driver;

    }

    @Override
    public ResultVo<List<FreeDriverVo>> findCarrierFreeDriver(FreeDto dto) {
        //获取承运商
        Carrier carrier = csSysService.getCarrierByRoleId(dto.getRoleId());
        if(carrier != null){
            //查询该承运商下的符合的全部司机
            dto.setCarrierId(carrier.getId());
            List<FreeDriverVo> freeDriverVos = driverDao.findCarrierDriver(dto);
            if(!CollectionUtils.isEmpty(freeDriverVos)){
                freeDriverVos = freeDriver(freeDriverVos,carrier.getId());
                return BaseResultUtil.success(freeDriverVos);
            }
        }else{
            return BaseResultUtil.fail("该承运商管理员不存在，请检查");
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<CarrierDriverVo> findCompanyFreeDriver(CarrierDriverNameDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                .eq(CarrierDriverCon::getId, dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        List<FreeDriverVo> freeDriverVos = driverDao.findCarrierAllDriver(cdc.getCarrierId(),dto.getRealName());
        freeDriverVos = freeDriver(freeDriverVos,cdc.getCarrierId());
        CarrierDriverVo driverVo = new CarrierDriverVo();
        driverVo.setDriverVo(freeDriverVos);
        return BaseResultUtil.success(driverVo);
    }

    @Override
    public ResultVo<Long> findRoleId(List<SelectRoleResp> roleResps, CarrierDriverCon cdc) {
        Long roleId = null;
        for(SelectRoleResp roleResp : roleResps){
            if(cdc.getRole() == RoleNameEnum.COMMON.getValue()){
                //下属司机
                if(roleResp.getRoleName().equals(RoleNameEnum.COMMON.getName())){
                    roleId = roleResp.getRoleId();
                    break;
                }
            }else if(cdc.getRole() == RoleNameEnum.ADMINSTRATOR.getValue()){
                //管理员
                if(roleResp.getRoleName().equals(RoleNameEnum.ADMINSTRATOR.getName())){
                    roleId = roleResp.getRoleId();
                    break;
                }
            }else if(cdc.getRole() == RoleNameEnum.SUPER_ADMINSTRATOR.getValue()){
                //超级管理员
                if(roleResp.getRoleName().contains(RoleNameEnum.SUPER_ADMINSTRATOR.getName())){
                    roleId = roleResp.getRoleId();
                    break;
                }
            }
        }
        return BaseResultUtil.success(roleId);
    }

    @Override
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(DispatchDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<DispatchDriverVo> dispatchDriverVos = driverDao.getDispatchDriver(dto);
        PageInfo<DispatchDriverVo> pageInfo = new PageInfo<>(dispatchDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    /**
     * 获取该承运商下空闲司机
     * @param freeDriverVos
     * @param carrierId
     * @return
     */
    private List<FreeDriverVo> freeDriver(List<FreeDriverVo> freeDriverVos,Long carrierId){
        //查询已被绑定的司机
        List<Long> driverIds = driverDao.findCarrierBusyDriver(carrierId);
        //去除已绑定司机
        if(!CollectionUtils.isEmpty(driverIds)){
            for (int i = 0; i < driverIds.size(); i++) {
                if(null == driverIds.get(i)){
                    continue;
                }
                for (FreeDriverVo vo : freeDriverVos) {
                    if(driverIds.get(i).equals(vo.getDriverId())){
                        freeDriverVos.remove(vo);
                        break;
                    }
                }
            }
        }
        return freeDriverVos;
    }
    /**
     * 修改承运商下司机与车辆关系
     * @param dto
     * @return
     */
    private ResultVo modifyDriver(CarrierDriverDto dto) {
        //判断司机是否已存在
        Driver dri = driverDao.selectById(dto.getDriverId());
        if(dri == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getDriverId,dto.getDriverId()));
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId,dri.getId()));
        if(vr != null) {
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(task != null){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        //更新司机信息
        ResultData rd = updateDriverToPlatform(dto);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        //更新司机信息
        BeanUtils.copyProperties(dto,dri);
        dri.setName(dto.getRealName());
        dri.setRealName(dto.getRealName());
        driverDao.updateById(dri);
        if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc != null){
            //更新司机与车辆绑定关系
            dvc.setVehicleId(dvc.getVehicleId());
            driverVehicleConDao.updateById(dvc);
            //更新运力信息
            vr.setVehicleId(dto.getVehicleId());
            vehicleRunningDao.updateById(vr);
        }else if(StringUtils.isBlank(dto.getPlateNo()) && dvc != null){
            //为空删除绑定关系
            driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
            vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
        }else if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc == null){
            //之前没绑定，现在绑定
            bindDriverVeh(dto);
        }
        return BaseResultUtil.success();
    }

    /**
     * 绑定司机与车辆之间关系
     * @param dto
     */
    @Override
    public void bindDriverVeh(CarrierVehicleDto dto){
        //绑定新的
        DriverVehicleCon driverCon = new DriverVehicleCon();
        driverCon.setDriverId(dto.getDriverId());
        driverCon.setVehicleId(dto.getVehicleId());
        driverVehicleConDao.insert(driverCon);
        //保存运力关系
        VehicleRunning vr = new VehicleRunning();
        vr.setDriverId(dto.getDriverId());
        vr.setVehicleId(dto.getVehicleId());
        vr.setPlateNo(dto.getPlateNo());
        vr.setCarryCarNum(dto.getDefaultCarryNum());
        vr.setState(RunningStateEnum.EFFECTIVE.code);
        vr.setRunningState(VehicleRunStateEnum.FREE.code);
        vr.setCreateTime(NOW);
        vehicleRunningDao.insert(vr);
    }



    /************************************韵车集成改版 st***********************************/

    @Override
    public ResultVo<PageVo<DispatchDriverVo>> dispatchAppDriverNew(DispatchDriverDto dto){
        log.info("干线调度个人(承运商)司机信息请求json数据 :: "+ JsonUtils.objectToJson(dto));
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        dto.setKeyword(dto.getRealName());
        List<DispatchDriverVo> dispatchDriverVos = driverDao.findAppDispatchDriver(dto);
        PageInfo<DispatchDriverVo> pageInfo = new PageInfo<>(dispatchDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriverNew(DispatchDriverDto dto){
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<DispatchDriverVo> dispatchDriverVos = driverDao.findDispatchDriver(dto);
        PageInfo<DispatchDriverVo> pageInfo = new PageInfo<>(dispatchDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<CarrierDriverVo> findCompanyFreeDriverNew(CarrierDriverNameDto dto){
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        List<FreeDriverVo> freeDriverVos = driverDao.findCarrierAllDriverNew(Long.valueOf(urd.getDeptId()),dto.getRealName());
        freeDriverVos = freeDriver(freeDriverVos,Long.valueOf(urd.getDeptId()));
        CarrierDriverVo driverVo = new CarrierDriverVo();
        driverVo.setDriverVo(freeDriverVos);
        return BaseResultUtil.success(driverVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo saveOrModifyDriverAppNew(CarrierDriverDto dto){
        log.info("新增/修改承运商下司机请求json数据 :: "+ JsonUtils.objectToJson(dto));
        Role role = null;
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        dto.setCarrierId(Long.valueOf(urd.getDeptId()));
        Carrier carrier = carrierDao.selectById(Long.valueOf(urd.getDeptId()));
        if(carrier == null){
            return BaseResultUtil.fail("该承运商不存在,请联系管理员");
        }
        if(carrier.getState() != CommonStateEnum.CHECKED.code){
            return BaseResultUtil.fail("该承运商审核未通过，不可添加司机");
        }
        //验证在个人司机池中是否存在
        Integer count = carrierDao.existPersonalCarrierNew(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于个人司机中");
        }
        //验证在该承运商下是否有相同的
        count = carrierDao.existBusinessCarrierNew(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于该企业承运商中");
        }
        if(dto.getDriverId() == null){
            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null){
                boolean result = csVehicleService.verifyDriverVehicle(null, dto.getVehicleId());
                if(!result){
                    return BaseResultUtil.fail("该车辆已绑定，请检查");
                }
            }
            Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                    .eq(Driver::getPhone, dto.getPhone()));
            if(driver == null){
                role = csRoleService.getByName(YmlProperty.get("cjkj.carrier_sub_driver_role_name"), DeptTypeEnum.CARRIER.code);
                if(role == null){
                    return BaseResultUtil.fail("下属司机角色不存在，请先添加");
                }
                ResultData<Long> rd = csCustomerService.addUserToPlatform(dto.getPhone(),dto.getRealName(),role);
                if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                    return BaseResultUtil.fail("司机信息保存失败，原因：" + rd.getMsg());
                }
                if(rd.getData() == null){
                    return BaseResultUtil.fail("获取架构组userId失败");
                }
                //保存司机
                driver = new Driver();
                BeanUtils.copyProperties(dto,driver);
                driver.setUserId(rd.getData());
                driver.setName(dto.getRealName());
                driver.setType(DriverTypeEnum.SOCIETY.code);
                driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
                driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
                //承运商超级管理员登陆
                driver.setSource(DriverSourceEnum.CARRIER_ADMIN.code);
                driver.setCreateUserId(dto.getLoginId());
                driver.setCreateTime(NOW);
                driverDao.insert(driver);
            }
            //保存司机与承运商关系
            csUserRoleDeptService.saveDriverToUserRoleDept(carrier, driver,dto.getMode(), role.getId(), dto.getLoginId(),0);
            //车牌号不为空
            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null){
                //保存司机与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setVehicleId(dto.getVehicleId());
                dvc.setDriverId(driver.getId());
                driverVehicleConDao.insert(dvc);
                //保存运力信息
                VehicleRunning vr = new VehicleRunning();
                vr.setDriverId(driver.getId());
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(RunningStateEnum.EFFECTIVE.code);
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vr.setCreateTime(NOW);
                vehicleRunningDao.insert(vr);
            }
            return BaseResultUtil.success();
        }else{
            return modifyDriverNew(dto,urd);
        }
    }

    /**
     * 修改承运商下司机与车辆关系
     * @param dto
     * @return
     */
    private ResultVo modifyDriverNew(CarrierDriverDto dto,UserRoleDept urd) {
        //判断司机是否已存在
        Driver driver = driverDao.selectById(dto.getDriverId());
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        Carrier carrier = carrierDao.selectById(urd.getDeptId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商不存在，请检查");
        }
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getDriverId,driver.getId()));
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId,driver.getId()));
        if(vr != null) {
            List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(!CollectionUtils.isEmpty(taskList)){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        //更新架构组用户信息
        ResultData<Boolean> updateRd = csCustomerService.updateUserToPlatform(null,driver, dto.getPhone());
        if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
            return BaseResultUtil.fail("修改用户信息失败，原因：" + updateRd.getMsg());
        }
        //更新司机与用户角色机构关系
        ResultVo resultVo = csUserRoleDeptService.updateDriverToUserRoleDept(carrier, driver,dto.getMode(), dto.getLoginId(),0);
        if (!ResultEnum.SUCCESS.getCode().equals(resultVo.getCode())) {
            return BaseResultUtil.fail("修改用户信息失败，原因：" + resultVo.getMsg());
        }
        //更新司机信息
        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setRealName(dto.getRealName());
        driverDao.updateById(driver);
        if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc != null){
            //更新司机与车辆绑定关系
            dvc.setVehicleId(dvc.getVehicleId());
            driverVehicleConDao.updateById(dvc);
            //更新运力信息
            vr.setVehicleId(dto.getVehicleId());
            vehicleRunningDao.updateById(vr);
        }else if(StringUtils.isBlank(dto.getPlateNo()) && dvc != null){
            //为空删除绑定关系
            driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
            vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
        }else if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc == null){
            //之前没绑定，现在绑定
            bindDriverVeh(dto);
        }
        return BaseResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo saveOrModifyDriverWebNew(CarrierDriverDto dto){
        Role role = null;
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商不存在,请联系管理员");
        }
        if(carrier.getState() != CommonStateEnum.CHECKED.code){
            return BaseResultUtil.fail("该承运商审核未通过，不可添加司机");
        }
        //验证在个人司机池中是否存在
        Integer count = carrierDao.existPersonalCarrierNew(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于个人司机中");
        }
        //验证在该承运商下是否有相同的
        count = carrierDao.existBusinessCarrierNew(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于该企业承运商中");
        }
        if(dto.getDriverId() == null){
            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null){
                boolean result = csVehicleService.verifyDriverVehicle(null, dto.getVehicleId());
                if(!result){
                    return BaseResultUtil.fail("该车辆已绑定，请检查");
                }
            }
            Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda()
                    .eq(Driver::getPhone, dto.getPhone()));
            if(driver == null){
                role = csRoleService.getByName(YmlProperty.get("cjkj.carrier_sub_driver_role_name"), DeptTypeEnum.CARRIER.code);
                if(role == null){
                    return BaseResultUtil.fail("下属司机角色不存在，请先添加");
                }
                ResultData<Long> rd = csCustomerService.addUserToPlatform(dto.getPhone(),dto.getRealName(),role);
                if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                    return BaseResultUtil.fail("司机信息保存失败，原因：" + rd.getMsg());
                }
                if(rd.getData() == null){
                    return BaseResultUtil.fail("获取架构组userId失败");
                }
                //保存司机
                driver = new Driver();
                BeanUtils.copyProperties(dto,driver);
                driver.setUserId(rd.getData());
                driver.setName(dto.getRealName());
                driver.setType(DriverTypeEnum.SOCIETY.code);
                driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
                driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
                //承运商超级管理员登陆
                driver.setSource(DriverSourceEnum.CARRIER_ADMIN.code);
                driver.setCreateUserId(dto.getLoginId());
                driver.setCreateTime(NOW);
                driverDao.insert(driver);
            }
            //保存司机与承运商关系
            csUserRoleDeptService.saveDriverToUserRoleDept(carrier, driver,dto.getMode(), role.getId(), dto.getLoginId(),0);
            //车牌号不为空
            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null){
                //保存司机与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setVehicleId(dto.getVehicleId());
                dvc.setDriverId(driver.getId());
                driverVehicleConDao.insert(dvc);
                //保存运力信息
                VehicleRunning vr = new VehicleRunning();
                vr.setDriverId(driver.getId());
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(RunningStateEnum.EFFECTIVE.code);
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vr.setCreateTime(NOW);
                vehicleRunningDao.insert(vr);
            }
            return BaseResultUtil.success();
        }else{
            return modifyDriverWebNew(dto);
        }
    }

    /**
     * 修改承运商下司机与车辆关系(web端我的公司)
     * @param dto
     * @return
     */
    private ResultVo modifyDriverWebNew(CarrierDriverDto dto) {
        //判断司机是否已存在
        Driver driver = driverDao.selectById(dto.getDriverId());
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商不存在，请检查");
        }
        Carrier oldCarrier = carrierDao.selectOne(new QueryWrapper<Carrier>().lambda()
                .eq(Carrier::getLinkmanPhone, driver.getPhone()));
        if(oldCarrier != null && (!dto.getPhone().equals(driver.getPhone()) || !dto.getIdCard().equals(driver.getIdCard()))){
            return BaseResultUtil.fail("修改该承运商管理员手机号/身份证号，请到运力中心--承运商管理中修改...");
        }
        //修改承运商管理员时操作
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getDeptId, carrier.getId())
                .eq(UserRoleDept::getUserId, driver.getId())
                .eq(UserRoleDept::getDeptType, DeptTypeEnum.CARRIER.code)
                .eq(UserRoleDept::getUserType, UserTypeEnum.DRIVER.code));
        if(urd == null){
            return BaseResultUtil.fail("该数据错误，请检查");
        }
        Role role = roleDao.selectOne(new QueryWrapper<Role>().lambda()
                .eq(Role::getId, urd.getRoleId()));
        if(role == null){
            return BaseResultUtil.fail("该数据错误，请检查");
        }
        Role roleS = csRoleService.getByName(YmlProperty.get("cjkj.carrier_super_role_name"), DeptTypeEnum.CARRIER.code);
        if(roleS == null){
            return BaseResultUtil.fail("承运商超级管理员角色不存在，请先添加");
        }
        Role roleC = csRoleService.getByName(YmlProperty.get("cjkj.carrier_common_role_name"), DeptTypeEnum.CARRIER.code);
        if(roleC == null){
            return BaseResultUtil.fail("承运商管理员角色不存在，请先添加");
        }
        if(roleS.getRoleName().equals(role.getRoleName()) || roleC.getRoleName().equals(role.getRoleName())){
            BeanUtils.copyProperties(dto,carrier);
            carrier.setLinkmanPhone(dto.getPhone());
            carrier.setLegalName(dto.getRealName());
            carrier.setLegalIdCard(dto.getIdCard());
            carrierDao.updateById(carrier);
        }
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getDriverId,driver.getId()));
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId,driver.getId()));
        if(vr != null) {
            List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(!CollectionUtils.isEmpty(taskList)){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        //更新架构组用户信息
        ResultData<Boolean> updateRd = csCustomerService.updateUserToPlatform(null,driver, dto.getPhone());
        if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
            return BaseResultUtil.fail("修改用户信息失败，原因：" + updateRd.getMsg());
        }
        //更新司机与用户角色机构关系
        ResultVo resultVo = csUserRoleDeptService.updateDriverToUserRoleDept(carrier, driver,dto.getMode(), dto.getLoginId(),0);
        if (!ResultEnum.SUCCESS.getCode().equals(resultVo.getCode())) {
            return BaseResultUtil.fail("修改用户信息失败，原因：" + resultVo.getMsg());
        }
        //更新司机信息
        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setRealName(dto.getRealName());
        driverDao.updateById(driver);
        if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc != null){
            //更新司机与车辆绑定关系
            dvc.setVehicleId(dvc.getVehicleId());
            driverVehicleConDao.updateById(dvc);
            //更新运力信息
            vr.setVehicleId(dto.getVehicleId());
            vehicleRunningDao.updateById(vr);
        }else if(StringUtils.isBlank(dto.getPlateNo()) && dvc != null){
            //为空删除绑定关系
            driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
            vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
        }else if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc == null){
            //之前没绑定，现在绑定
            bindDriverVeh(dto);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<List<FreeDriverVo>> findCarrierFreeDriverNew(FreeDto dto){
        //获取承运商
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在，请检查");
        }
        //查询该承运商下的符合的全部司机
        List<FreeDriverVo> freeDriverVos = driverDao.findCarrierDriverNew(dto);
        if(!CollectionUtils.isEmpty(freeDriverVos)){
            freeDriverVos = freeDriver(freeDriverVos,carrier.getId());
        }
        return BaseResultUtil.success(freeDriverVos);
    }

}
