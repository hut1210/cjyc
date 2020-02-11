package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mineCarrier.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.enums.role.RoleNameEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineCarrier.SettlementDetailVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyDriverVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyWaybillVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.web.api.service.IMineCarrierService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MineCarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements IMineCarrierService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private ICarrierCarCountDao carrierCarCountDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ICsVehicleService csVehicleService;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICsRoleService csRoleService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<PageVo<MyWaybillVo>> findWaybill(MyWaybillDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<MyWaybillVo> waybillVos = waybillDao.findByCarrierId(dto);
        PageInfo<MyWaybillVo> pageInfo = new PageInfo<>(waybillVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<MyDriverVo>> findPageDriver(QueryMyDriverDto dto) {
        Carrier carrier = csSysService.getCarrierByRoleId(dto.getRoleId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在,请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        dto.setCarrierId(carrier.getId());
        List<MyDriverVo> myDriverVos =  driverDao.findMyDriver(dto);
        if(!CollectionUtils.isEmpty(myDriverVos)){
            for(MyDriverVo vo : myDriverVos){
                CarrierCarCount count = carrierCarCountDao.driverCount(vo.getDriverId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                }
            }
            PageInfo<MyDriverVo> pageInfo = new PageInfo<>(myDriverVos);
            return BaseResultUtil.success(pageInfo);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo verifyDriver(OperateDto dto) {
        Driver driver = driverDao.selectById(dto.getId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId,dto.getId())
                .eq(CarrierDriverCon::getCarrierId,dto.getCarrierId()));
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(driver == null || cdc == null || carrier == null){
            return BaseResultUtil.fail("数据错误,请联系管理员");
        }
        //处理角色
        Long userId = null;
        Long roleId = null;
        ResultData<List<SelectRoleResp>> rd = null;
        if(dto.getFlag() == FlagEnum.ADMINISTRATOR.code || dto.getFlag() == FlagEnum.REMOVE_ADMINISTRATOR.code){
            //获取架构组角色集合
            userId = driver.getUserId();
            rd = sysRoleService.getSingleLevelList(carrier.getDeptId());
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return BaseResultUtil.fail("查询组织下的所有角色失败");
            }
        }
        //获取升级为管理员所需的机构id
        if(dto.getFlag() == FlagEnum.ADMINISTRATOR.code){
            for(SelectRoleResp roleResp : rd.getData()){
                //管理员
                if(roleResp.getRoleName().equals(RoleNameEnum.ADMINSTRATOR.getName())){
                    roleId = roleResp.getRoleId();
                    break;
                }
            }
        }
        if(dto.getFlag() == FlagEnum.ADMINISTRATOR.code){
            //更新架构组角色
            List<Long> roleIds = new ArrayList<>(10);
            roleIds.add(roleId);
            UpdateUserReq uur = new UpdateUserReq();
            uur.setUserId(userId);
            uur.setRoleIdList(roleIds);
            ResultData update = sysUserService.update(uur);
            if (!ReturnMsg.SUCCESS.getCode().equals(update.getCode())) {
                return BaseResultUtil.fail("更新组织下的所有角色失败");
            }
            //更新韵车管理员
            driver.setIdentity(DriverIdentityEnum.CARRIER_MANAGER.code);
            //修改身份
            cdc.setRole(DriverRoleEnum.ADMIN.code);
            carrierDriverConDao.updateById(cdc);
        }else if(dto.getFlag() == FlagEnum.REMOVE_ADMINISTRATOR.code){
            //封装获取用户角色
            ResultVo<Long> resultVo = csDriverService.findRoleId(rd.getData(), cdc);
            //调用架构组撤销角色
            ResultData resultData = sysRoleService.revokeRole(userId, resultVo.getData());
            if (!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
                return BaseResultUtil.fail("解除管理员失败");
            }
            //解除管理员
            driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
            //修改身份
            cdc.setRole(DriverRoleEnum.SUB_DRIVER.code);
            carrierDriverConDao.updateById(cdc);
        }else if(dto.getFlag() == FlagEnum.FROZEN.code){
            //冻结
            cdc.setState(CommonStateEnum.FROZEN.code);
        }else if(dto.getFlag() == FlagEnum.THAW.code){
            //解冻
            cdc.setState(CommonStateEnum.CHECKED.code);
        }
        driverDao.updateById(driver);
        carrierDriverConDao.updateById(cdc);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo saveOrModifyVehicle(CarrierVehicleDto dto) {
        Carrier carrier = csSysService.getCarrierByRoleId(dto.getRoleId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在,请检查");
        }
        carrier = carrierDao.selectById(carrier.getId());
        if(carrier.getState() != CommonStateEnum.CHECKED.code){
            return BaseResultUtil.fail("该承运商审核未通过，不可添加车辆");
        }
        if(dto.getVehicleId() == null){
            Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo,dto.getPlateNo()));
            if(vehicle != null){
                return BaseResultUtil.fail("该车辆已添加，请核对");
            }
            if(dto.getDriverId() != null){
                boolean result = csVehicleService.verifyDriverVehicle(dto.getDriverId(), null);
                if(!result){
                    return BaseResultUtil.fail("该司机已与车辆绑定，请检查");
                }
            }
            Vehicle veh = new Vehicle();
            BeanUtils.copyProperties(dto,veh);
            veh.setOwnershipType(VehicleOwnerEnum.CARRIER.code);
            veh.setCarrierId(carrier.getId());
            veh.setCreateUserId(dto.getLoginId());
            veh.setCreateTime(NOW);
            vehicleDao.insert(veh);
            //选择绑定司机
            if(dto.getDriverId() != null){
                //有司机，绑定与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setDriverId(dto.getDriverId());
                dvc.setVehicleId(veh.getId());
                driverVehicleConDao.insert(dvc);
                //保存运力关系
                VehicleRunning vr = new VehicleRunning();
                vr.setDriverId(dto.getDriverId());
                vr.setVehicleId(veh.getId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(RunningStateEnum.EFFECTIVE.code);
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vr.setCreateTime(NOW);
                vehicleRunningDao.insert(vr);
            }
        }else{
            return modifyVehicle(dto);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<MyCarVo>> findPageCar(QueryMyCarDto dto) {
        Carrier carrier = csSysService.getCarrierByRoleId(dto.getRoleId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在，请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        dto.setCarrierId(carrier.getId());
        List<MyCarVo> myCarVos = vehicleDao.findMyCar(dto);
        PageInfo<MyCarVo> pageInfo = new PageInfo<>(myCarVos);
        return BaseResultUtil.success(pageInfo);
    }

    /**
     * 修改车辆
     * @param dto
     * @return
     */
    private ResultVo modifyVehicle(CarrierVehicleDto dto) {
        //更新车辆信息
        Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda()
                .eq(dto.getVehicleId()!= null,Vehicle::getId, dto.getVehicleId()));
        if(vehicle == null){
            return BaseResultUtil.fail("该车辆不存在，请检查");
        }
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(dto.getVehicleId() != null, VehicleRunning::getVehicleId, dto.getVehicleId()));
        //判断该运力是否在运输中
        if(vr != null){
            List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(!CollectionUtils.isEmpty(taskList)){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }

        vehicle.setDefaultCarryNum(dto.getDefaultCarryNum());
        vehicleDao.updateById(vehicle);
        vehicle = vehicleDao.selectById(dto.getVehicleId());

        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                                .eq(dto.getVehicleId() != null,DriverVehicleCon::getVehicleId,dto.getVehicleId()));
        //新增没有绑定，修改绑定
        if(dvc == null && vr == null && dto.getDriverId() != null){
            csVehicleService.saveTransport(dto,null,vehicle);
        }
        if(dvc!= null && vr != null){
            if(dto.getDriverId() != null && (!dvc.getDriverId().equals(dto.getDriverId()))){
                //新增与修改时不同的司机
                csVehicleService.updateTransport(dto,null,vehicle);
            }else if(dto.getDriverId() == null){
                //新增时有司机，修改时不绑定
                vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
                driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<SettlementDetailVo>> getSettlementDetail(SettlementDetailQueryDto settlementDetailQueryDto) {
        PageHelper.startPage(settlementDetailQueryDto.getCurrentPage(),settlementDetailQueryDto.getPageSize());
        List<SettlementDetailVo> settlementDetailVoList = waybillDao.getSettlementDetail(settlementDetailQueryDto);
        PageInfo<SettlementDetailVo> pageInfo = new PageInfo<>(settlementDetailVoList);
        return BaseResultUtil.success(pageInfo);
    }




    /************************************韵车集成改版 st***********************************/

    @Override
    public ResultVo<PageVo<MyDriverVo>> findPageDriverNew(QueryMyDriverDto dto){
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在,请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        dto.setCarrierId(carrier.getId());
        List<MyDriverVo> driverVos =  driverDao.findMyDriverNew(dto);
        if(!CollectionUtils.isEmpty(driverVos)){
            for(MyDriverVo driverVo : driverVos){
                CarrierCarCount count = carrierCarCountDao.driverCount(driverVo.getDriverId());
                if(count != null){
                    driverVo.setCarNum(count.getCarNum());
                }
            }
        }
        PageInfo<MyDriverVo> pageInfo = new PageInfo<>(driverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo verifyDriverNew(OperateDto dto){
        Driver driver = driverDao.selectById(dto.getId());
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getDeptId, dto.getCarrierId())
                .eq(UserRoleDept::getUserId, dto.getId())
                .eq(UserRoleDept::getDeptType, DeptTypeEnum.CARRIER.code)
                .eq(UserRoleDept::getUserType, UserTypeEnum.DRIVER.code));
        if(driver == null || urd == null || carrier == null){
            return BaseResultUtil.fail("数据错误,请联系管理员");
        }
        Role adminRole = csRoleService.getByName(YmlProperty.get("cjkj.carrier_common_role_name"), DeptTypeEnum.CARRIER.code);
        if(adminRole == null){
            return BaseResultUtil.fail("承运商管理员角色不存在,请先添加角色");
        }
        Role subRole = csRoleService.getByName(YmlProperty.get("cjkj.carrier_sub_driver_role_name"), DeptTypeEnum.CARRIER.code);
        if(subRole == null){
            return BaseResultUtil.fail("承运商下属司机角色不存在,请先添加角色");
        }
        if(dto.getFlag() == FlagEnum.ADMINISTRATOR.code){
            //设为管理员
            ResultData updateRd = updatePlatformRole(driver.getUserId(),adminRole.getRoleId());
            if (!ReturnMsg.SUCCESS.getCode().equals(updateRd.getCode())) {
                return BaseResultUtil.fail("更新组织下的所有角色失败");
            }
            //更新韵车司机管理员
            driver.setIdentity(DriverIdentityEnum.CARRIER_MANAGER.code);
            urd.setRoleId(adminRole.getId());
        }else if(dto.getFlag() == FlagEnum.REMOVE_ADMINISTRATOR.code){
            //解除管理员
            ResultData resultData = sysRoleService.revokeRole(driver.getUserId(), adminRole.getRoleId());
            if (!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
                return BaseResultUtil.fail("解除承运商管理员角色失败");
            }
            //更新韵车司机普通司机
            driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
            urd.setRoleId(subRole.getId());
        }else if(dto.getFlag() == FlagEnum.FROZEN.code){
            //冻结
            urd.setState(CommonStateEnum.FROZEN.code);
        }else if(dto.getFlag() == FlagEnum.THAW.code){
            //解冻
            urd.setState(CommonStateEnum.CHECKED.code);
        }
        driver.setCheckTime(NOW);
        driver.setCheckUserId(dto.getLoginId());
        driverDao.updateById(driver);
        userRoleDeptDao.updateById(urd);
        return BaseResultUtil.success();
    }

    /**
     * 更新架构组用户与角色
     * @param userId
     * @param roleId
     * @return
     */
    private ResultData updatePlatformRole(Long userId,Long roleId){
        UpdateUserReq uur = new UpdateUserReq();
        uur.setUserId(userId);
        uur.setRoleIdList(Arrays.asList(roleId));
        ResultData updateRd = sysUserService.update(uur);
        return updateRd;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo saveOrModifyVehicleNew(CarrierVehicleDto dto){
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在,请检查");
        }
        if(carrier.getState() != CommonStateEnum.CHECKED.code){
            return BaseResultUtil.fail("该承运商审核未通过，不可添加车辆");
        }
        if(dto.getVehicleId() == null){
            Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo,dto.getPlateNo()));
            if(vehicle != null){
                return BaseResultUtil.fail("该车辆已添加，请核对");
            }
            if(dto.getDriverId() != null){
                boolean result = csVehicleService.verifyDriverVehicle(dto.getDriverId(), null);
                if(!result){
                    return BaseResultUtil.fail("该司机已与车辆绑定，请检查");
                }
            }
            Vehicle veh = new Vehicle();
            BeanUtils.copyProperties(dto,veh);
            veh.setOwnershipType(VehicleOwnerEnum.CARRIER.code);
            veh.setCarrierId(carrier.getId());
            veh.setCreateUserId(dto.getLoginId());
            veh.setCreateTime(NOW);
            vehicleDao.insert(veh);
            //选择绑定司机
            if(dto.getDriverId() != null){
                //有司机，绑定与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setDriverId(dto.getDriverId());
                dvc.setVehicleId(veh.getId());
                driverVehicleConDao.insert(dvc);
                //保存运力关系
                VehicleRunning vr = new VehicleRunning();
                vr.setDriverId(dto.getDriverId());
                vr.setVehicleId(veh.getId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(RunningStateEnum.EFFECTIVE.code);
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vr.setCreateTime(NOW);
                vehicleRunningDao.insert(vr);
            }
        }else{
            return modifyVehicle(dto);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<MyCarVo>> findPageCarNew(QueryMyCarDto dto){
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if(carrier == null){
            return BaseResultUtil.fail("该承运商管理员不存在，请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<MyCarVo> carVos = vehicleDao.findMyCarNew(dto);
        PageInfo<MyCarVo> pageInfo = new PageInfo<>(carVos);
        return BaseResultUtil.success(pageInfo);
    }
}