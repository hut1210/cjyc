package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.mine.*;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.RunningStateEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.enums.transport.VehicleRunStateEnum;
import com.cjyc.common.model.util.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.*;
import com.cjyc.common.system.service.ICsBankInfoService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.driver.api.service.IMineService;
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
import java.util.List;

@Service
@Slf4j
public class MineServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IMineService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private IBankCardBindDao bankCardBindDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IExistDriverDao existDriverDao;
    @Resource
    private IRoleDao roleDao;
    @Resource
    private ICsSmsService csSmsService;
    @Resource
    private ICsVehicleService csVehicleService;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ICsBankInfoService bankInfoService;

    @Override
    public ResultVo<BankCardVos> findBinkCard(AppDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                .eq(CarrierDriverCon::getId, dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        BankCardVos cardVos = new BankCardVos();
        List<BankCardVo> bankCardVos = bankCardBindDao.findBinkCardInfo(cdc.getCarrierId());
        cardVos.setCardVos(bankCardVos);
        return BaseResultUtil.success(cardVos);
    }

    @Override
    public ResultVo<PageVo<DriverInfoVo>> findDriver(BaseDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                .eq(CarrierDriverCon::getId, dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<DriverInfoVo> driverInfo = driverDao.findDriverInfo(cdc.getCarrierId());
        PageInfo<DriverInfoVo> pageInfo = new PageInfo(driverInfo);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo frozenDriver(FrozenDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId,dto.getLoginId())
                .eq(CarrierDriverCon::getId,dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getId, dto.getDriverId()));
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(System.currentTimeMillis());
        driverDao.updateById(driver);

        cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId,dto.getDriverId())
                .eq(CarrierDriverCon::getCarrierId,cdc.getCarrierId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        cdc.setState(CommonStateEnum.FROZEN.code);
        carrierDriverConDao.updateById(cdc);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo saveOrModifyVehicle(AppCarrierVehicleDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId,dto.getLoginId())
                .eq(CarrierDriverCon::getId,dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        //根据车牌号查询库中有没有添加
        Vehicle veh = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda()
                .eq(StringUtils.isNotBlank(dto.getPlateNo()),Vehicle::getPlateNo,dto.getPlateNo()));
        if(dto.getVehicleId() == null){
            if(veh != null){
                return BaseResultUtil.fail("该车辆已存在,请检查");
            }
            if(dto.getDriverId() != null){
                boolean result = csVehicleService.verifyDriverVehicle(dto.getDriverId(), null);
                if(!result){
                    return BaseResultUtil.fail("该司机已绑定，请检查");
                }
            }
            //新增车辆
            veh = new Vehicle();
            BeanUtils.copyProperties(dto,veh);
            veh.setCarrierId(cdc.getCarrierId());
            veh.setOwnershipType(VehicleOwnerEnum.CARRIER.code);
            veh.setCreateUserId(cdc.getCarrierId());
            veh.setCreateTime(System.currentTimeMillis());
            vehicleDao.insert(veh);
            if(dto.getDriverId() != null){
                csVehicleService.saveTransport(null,dto,veh);
            }
        }else{
            if(veh != null && !veh.getId().equals(dto.getVehicleId())){
                return BaseResultUtil.fail("该车辆已存在,请检查");
            }
            VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                    .eq(dto.getVehicleId() != null,VehicleRunning::getVehicleId, dto.getVehicleId()));
            DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                    .eq(dto.getVehicleId() != null,DriverVehicleCon::getVehicleId, dto.getVehicleId()));
            if(vr != null && dvc != null){
                //判断该运力是否在运输中
                List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                        .eq(Task::getVehicleRunningId,vr.getId())
                        .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                if(!CollectionUtils.isEmpty(taskList)){
                    return BaseResultUtil.fail("该运力正在运输中，不可修改");
                }
            }
            //更新车辆
            veh = new Vehicle();
            BeanUtils.copyProperties(dto,veh);
            veh.setCreateUserId(cdc.getCarrierId());
            vehicleDao.updateById(veh);
            veh = vehicleDao.selectById(dto.getVehicleId());
            //新增没有绑定，修改绑定
            if(dvc == null && dto.getDriverId() != null){
                csVehicleService.saveTransport(null,dto,veh);
            }else if(dvc!= null && vr != null && dto.getDriverId() != null && (!dvc.getDriverId().equals(dto.getDriverId()))){
                //新增与修改时不同的司机
                csVehicleService.updateTransport(null,dto,veh);
            }else if(dvc != null && vr != null && dto.getDriverId() == null){
                //新增时有司机，修改时不绑定
                vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
                driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo removeVehicle(RemoveVehicleDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        //判断该运力是否在运输中
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(VehicleRunning::getDriverId, dto.getDriverId())
                .eq(VehicleRunning::getVehicleId, dto.getVehicleId()));
        if(vr != null){
            List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(!CollectionUtils.isEmpty(taskList)){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        if(dto.getDriverId() != null || dto.getDriverId() != 0){
            //车辆与司机有绑定关系
            //删除与司机关系
            driverVehicleConDao.removeCon(dto.getDriverId(),dto.getVehicleId());
            vehicleRunningDao.removeRun(dto.getDriverId(),dto.getVehicleId());
        }
        vehicleDao.deleteById(dto.getVehicleId());
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<DriverVehicleVo>> findVehicle(BaseDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<DriverVehicleVo> vehicleVos = driverDao.findVehicle(cdc.getCarrierId());
        PageInfo<DriverVehicleVo> pageInfo = new PageInfo(vehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo authOrModifyInfo(SocietyDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        //验证在承运商中是否存在
        Integer count = driverDao.existEnterPriseDriver(dto);
        if(count > 0){
            //个人承运商下记录
            ExistDriver existDriver = new ExistDriver();
            existDriver.setDriverId(dto.getLoginId());
            existDriver.setName(dto.getRealName());
            existDriver.setIdCard(dto.getIdCard());
            existDriver.setExistIdCard(dto.getIdCard());
            existDriver.setCreateTime(System.currentTimeMillis());
            existDriverDao.insert(existDriver);
            return BaseResultUtil.fail("账号已存在于该企业承运商中");
        }
        //验证在个人司机中是否存在
        count = driverDao.existPersonDriver(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于个人司机中");
        }
        //获取承运商
        Carrier carrier = carrierDao.selectById(cdc.getCarrierId());
        carrier.setName(dto.getRealName());
        carrier.setLinkman(dto.getRealName());
        carrier.setLegalName(dto.getRealName());
        carrier.setLegalIdCard(dto.getIdCard());
        carrier.setMode(dto.getMode());
        carrierDao.updateById(carrier);

        //更新司机信息
        Driver driver = driverDao.selectById(dto.getLoginId());
        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setCreateUserId(dto.getLoginId());
        driverDao.updateById(driver);

        //更新状态(审核中)
        cdc.setState(CommonStateEnum.IN_CHECK.code);
        cdc.setMode(dto.getMode());
        carrierDriverConDao.updateById(cdc);
        //运力信息
        DriverVehicleCon vehicleCon = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                .eq(DriverVehicleCon::getDriverId, dto.getLoginId()));
        if(dto.getFlag() == 0 && dto.getVehicleId() != null && vehicleCon == null){
            vehicleCon = new DriverVehicleCon();
            vehicleCon.setDriverId(dto.getLoginId());
            vehicleCon.setVehicleId(dto.getVehicleId());
            driverVehicleConDao.insert(vehicleCon);

            VehicleRunning vr = new VehicleRunning();
            BeanUtils.copyProperties(dto,vr);
            vr.setDriverId(dto.getLoginId());
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            vr.setState(RunningStateEnum.EFFECTIVE.code);
            vr.setRunningState(VehicleRunStateEnum.FREE.code);
            vr.setCreateTime(System.currentTimeMillis());
            vehicleRunningDao.insert(vr);
        }else if(dto.getFlag() == 1 && dto.getVehicleId() != null){
            VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                    .eq(VehicleRunning::getDriverId, dto.getLoginId()));
            if(vr != null){
                List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                        .eq(Task::getVehicleRunningId,vr.getId())
                        .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                if(!CollectionUtils.isEmpty(taskList)){
                    return BaseResultUtil.fail("该运力正在运输中，不可修改");
                }
            }
            //更新
            vehicleCon.setDriverId(dto.getLoginId());
            vehicleCon.setVehicleId(dto.getVehicleId());
            driverVehicleConDao.updateById(vehicleCon);

            vr.setDriverId(dto.getLoginId());
            vr.setVehicleId(dto.getVehicleId());
            vr.setPlateNo(dto.getPlateNo());
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            vr.setState(RunningStateEnum.EFFECTIVE.code);
            vr.setRunningState(VehicleRunStateEnum.FREE.code);
            vehicleRunningDao.updateById(vr);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<AppDriverInfoVo> findNewDriverInfo(AppDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId())
                .eq(CarrierDriverCon::getId, dto.getRoleId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        AppDriverInfoVo appDriverInfo = carrierDao.findAppDriverInfo(dto.getRoleId(), dto.getLoginId());
        return BaseResultUtil.success(appDriverInfo);
    }

    @Override
    public ResultVo updateDriverState(DriverStateDto dto) {
        Driver driver = driverDao.selectById(dto.getLoginId());
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        driver.setBusinessState(dto.getBusinessState());
        driverDao.updateById(driver);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<SocietyDriverVo> showDriverInfo(AppDriverDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        SocietyDriverVo personInfo = driverDao.findPersonInfo(dto);
        return BaseResultUtil.success(personInfo);
    }

    @Override
    public ResultVo addBankCard(BankCardDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        BankCardBind bcb = new BankCardBind();
        bcb.setUserId(cdc.getCarrierId());
        bcb.setCardNo(dto.getCardNo());
        bcb.setUserType(UserTypeEnum.DRIVER.code);
        bcb.setCardType(CardTypeEnum.PRIVATE.code);
        bcb.setCardName(dto.getRealName());
        bcb.setCardPhone(dto.getPhone());
        bcb.setIdCard(dto.getIdCard());
        bcb.setCardColour(RandomUtil.getIntRandom());
        bcb.setBankName(dto.getBankName());
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCreateTime(System.currentTimeMillis());
        bankCardBindDao.insert(bcb);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo removeBankCard(RemoveBankCardDto dto) {
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        if(cdc == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        boolean result = csSmsService.validateCaptcha(dto.getPhone(),dto.getCode(),CaptchaTypeEnum.valueOf(dto.getType()), ClientEnum.APP_DRIVER);
        if(!result){
            return BaseResultUtil.fail("验证码与手机号不匹配或者过期，请核对发送");
        }
        BankCardBind bcb = bankCardBindDao.selectById(dto.getCardId());
        bcb.setState(UseStateEnum.DISABLED.code);
        bankCardBindDao.updateById(bcb);
        return BaseResultUtil.success();
    }



    /************************************韵车集成改版 st***********************************/

    @Override
    public ResultVo<BankCardVos> findBinkCardNew(AppDriverDto dto){
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(null == urd){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        BankCardVos cardVos = new BankCardVos();
        List<BankCardVo> bankCardVos = bankCardBindDao.findBinkCardInfo(Long.valueOf(urd.getDeptId()));
        cardVos.setCardVos(bankCardVos);
        return BaseResultUtil.success(cardVos);
    }

    @Override
    public ResultVo<PageVo<DriverInfoVo>> findDriverNew(BaseDriverDto dto){
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<DriverInfoVo> driverInfo = driverDao.findDriverInfoNew(Long.valueOf(urd.getDeptId()));
        PageInfo<DriverInfoVo> pageInfo = new PageInfo(driverInfo);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo saveOrModifyVehicleNew(AppCarrierVehicleDto dto){
        log.info("新增/修改车辆请求json数据 :: "+ JsonUtils.objectToJson(dto));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        //判断是社会司机还是管理员
        Role role = roleDao.selectOne(new QueryWrapper<Role>().lambda()
                .eq(Role::getId, urd.getRoleId()));
        if(role == null){
            return BaseResultUtil.fail("该司机角色不存在,请检查");
        }
        Role roleName = csRoleService.getByName(YmlProperty.get("cjkj.carrier_personal_driver_role_name"), DeptTypeEnum.CARRIER.code);
        if(roleName == null){
            return BaseResultUtil.fail("个人司机角色不存在，请先添加");
        }
        //根据车牌号查询库中有没有添加x
        Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda()
                .eq(StringUtils.isNotBlank(dto.getPlateNo()),Vehicle::getPlateNo,dto.getPlateNo()));
        if(role.getRoleName().equals(roleName.getRoleName())){
            //社会司机时只添加车辆
            if(vehicle != null){
                return BaseResultUtil.fail("该车辆已存在,请检查");
            }
            //新增
            DriverVehicleCon vehicleCon = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                    .eq(DriverVehicleCon::getDriverId, urd.getUserId()));
            if(dto.getVehicleId() == null){
                if(vehicleCon != null){
                    return BaseResultUtil.fail("该司机已绑定车辆,请检查");
                }
                if(vehicleCon == null){
                    //保存车辆
                    vehicle = new Vehicle();
                    BeanUtils.copyProperties(dto,vehicle);
                    vehicle.setCarrierId(Long.valueOf(urd.getDeptId()));
                    vehicle.setOwnershipType(VehicleOwnerEnum.PERSONAL.code);
                    vehicle.setCreateUserId(urd.getUserId());
                    vehicle.setCreateTime(System.currentTimeMillis());
                    vehicleDao.insert(vehicle);
                    //保存运力
                    VehicleRunning vr = new VehicleRunning();
                    BeanUtils.copyProperties(dto,vr);
                    vr.setDriverId(dto.getLoginId());
                    vr.setCarryCarNum(dto.getDefaultCarryNum());
                    vr.setVehicleId(vehicle.getId());
                    vr.setState(RunningStateEnum.EFFECTIVE.code);
                    vr.setRunningState(VehicleRunStateEnum.FREE.code);
                    vr.setCreateTime(System.currentTimeMillis());
                    vehicleRunningDao.insert(vr);
                    //保存运力与司机关系
                    DriverVehicleCon dvc = new DriverVehicleCon();
                    dvc.setDriverId(dto.getLoginId());
                    dvc.setVehicleId(vehicle.getId());
                    driverVehicleConDao.insert(dvc);
                }
            }else{
                //修改
                //车辆信息没有修改与之前相同直接成功
                if(vehicle != null && vehicle.getId().equals(dto.getVehicleId())){
                    return BaseResultUtil.success();
                }
                //车辆信息与之前不相同，更新信息
                VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                        .eq(dto.getLoginId() != null,VehicleRunning::getDriverId, dto.getLoginId()));
                DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                        .eq(dto.getLoginId() != null,DriverVehicleCon::getDriverId, dto.getLoginId()));
                if(vr != null && dvc != null){
                    //判断该运力是否在运输中
                    List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                            .eq(Task::getVehicleRunningId,vr.getId())
                            .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                    if(!CollectionUtils.isEmpty(taskList)){
                        return BaseResultUtil.fail("该运力正在运输中，不可修改");
                    }
                }
                //获取之前绑定的车辆信息
                Vehicle oldVehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getCarrierId, Long.valueOf(urd.getDeptId())));
                //更新车辆信息
                oldVehicle.setPlateNo(dto.getPlateNo());
                oldVehicle.setDefaultCarryNum(dto.getDefaultCarryNum());
                vehicleDao.updateById(oldVehicle);
                //更新运力
                VehicleRunning vR = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                        .eq(VehicleRunning::getDriverId, dto.getLoginId())
                        .eq(VehicleRunning::getVehicleId, oldVehicle.getId()));
                vR.setPlateNo(dto.getPlateNo());
                vR.setCarryCarNum(dto.getDefaultCarryNum());
                vehicleRunningDao.updateById(vR);
            }
        }else{
            //管理员添加车辆
            if(dto.getVehicleId() == null){
                if(vehicle != null){
                    return BaseResultUtil.fail("该车辆已存在,请检查");
                }
                if(dto.getDriverId() != null){
                    boolean result = csVehicleService.verifyDriverVehicle(dto.getDriverId(), null);
                    if(!result){
                        return BaseResultUtil.fail("该司机已绑定，请检查");
                    }
                }
                //新增车辆
                vehicle = new Vehicle();
                BeanUtils.copyProperties(dto,vehicle);
                vehicle.setCarrierId(Long.valueOf(urd.getDeptId()));
                vehicle.setOwnershipType(VehicleOwnerEnum.CARRIER.code);
                vehicle.setCreateUserId(Long.valueOf(urd.getDeptId()));
                vehicle.setCreateTime(System.currentTimeMillis());
                vehicleDao.insert(vehicle);
                if(dto.getDriverId() != null){
                    csVehicleService.saveTransport(null,dto,vehicle);
                }
            }else{
                if(vehicle != null && !vehicle.getId().equals(dto.getVehicleId())){
                    return BaseResultUtil.fail("该车辆已存在,请检查");
                }
                VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                        .eq(dto.getVehicleId() != null,VehicleRunning::getVehicleId, dto.getVehicleId()));
                DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                        .eq(dto.getVehicleId() != null,DriverVehicleCon::getVehicleId, dto.getVehicleId()));
                if(vr != null && dvc != null){
                    //判断该运力是否在运输中
                    List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                            .eq(Task::getVehicleRunningId,vr.getId())
                            .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                    if(!CollectionUtils.isEmpty(taskList)){
                        return BaseResultUtil.fail("该运力正在运输中，不可修改");
                    }
                }
                //更新车辆
                vehicle = new Vehicle();
                BeanUtils.copyProperties(dto,vehicle);
                vehicle.setCreateUserId(Long.valueOf(urd.getDeptId()));
                vehicleDao.updateById(vehicle);
                vehicle = vehicleDao.selectById(dto.getVehicleId());
                //新增没有绑定，修改绑定
                if(dvc == null && dto.getDriverId() != null){
                    csVehicleService.saveTransport(null,dto,vehicle);
                }else if(dvc!= null && vr != null && dto.getDriverId() != null && (!dvc.getDriverId().equals(dto.getDriverId()))){
                    //新增与修改时不同的司机
                    csVehicleService.updateTransport(null,dto,vehicle);
                }else if(dvc != null && vr != null && dto.getDriverId() == null){
                    //新增时有司机，修改时不绑定
                    vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
                    driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
                }
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo frozenDriverNew(FrozenDriverDto dto){
        log.info("删除(冻结)承运商下司机请求json数据 :: "+ JsonUtils.objectToJson(dto));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getId, dto.getDriverId()));
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(System.currentTimeMillis());
        driverDao.updateById(driver);

        urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId,dto.getDriverId())
                .eq(UserRoleDept::getDeptId,urd.getDeptId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        urd.setState(CommonStateEnum.FROZEN.code);
        urd.setUpdateTime(System.currentTimeMillis());
        urd.setUpdateUserId(dto.getLoginId());
        userRoleDeptDao.updateById(urd);
        return BaseResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo removeVehicleNew(RemoveVehicleDto dto){
        log.info("个人司机删除车辆绑定关系请求json数据 :: "+ JsonUtils.objectToJson(dto));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        //判断该运力是否在运输中
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                .eq(VehicleRunning::getDriverId, dto.getDriverId())
                .eq(VehicleRunning::getVehicleId, dto.getVehicleId()));
        if(vr != null){
            List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(!CollectionUtils.isEmpty(taskList)){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        if(dto.getDriverId() != null || dto.getDriverId() != 0){
            //车辆与司机有绑定关系
            //删除与司机关系
            driverVehicleConDao.removeCon(dto.getDriverId(),dto.getVehicleId());
            vehicleRunningDao.removeRun(dto.getDriverId(),dto.getVehicleId());
        }
        vehicleDao.deleteById(dto.getVehicleId());
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<DriverVehicleVo>> findVehicleNew(BaseDriverDto dto){
        List<DriverVehicleVo> vehicleVos = null;
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机管理员不存在,请检查");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        Role role = roleDao.selectOne(new QueryWrapper<Role>().lambda().eq(Role::getId, urd.getRoleId()));
        if(role != null && "下属司机".equals(role.getRoleName())){
            vehicleVos = driverDao.findSubDriverVehicle(Long.valueOf(urd.getDeptId()),dto.getLoginId());
        }else{
            vehicleVos = driverDao.findVehicle(Long.valueOf(urd.getDeptId()));
        }
        PageInfo<DriverVehicleVo> pageInfo = new PageInfo(vehicleVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo authOrModifyInfoNew(SocietyDriverDto dto){
        log.info("个人司机认证/修改个人信息请求json数据 :: "+ JsonUtils.objectToJson(dto));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        //验证在承运商中是否存在
        Integer count = driverDao.existEnterPriseDriverNew(dto);
        if(count > 0){
            //个人承运商下记录
            ExistDriver existDriver = new ExistDriver();
            existDriver.setDriverId(dto.getLoginId());
            existDriver.setName(dto.getRealName());
            existDriver.setIdCard(dto.getIdCard());
            existDriver.setExistIdCard(dto.getIdCard());
            existDriver.setCreateTime(System.currentTimeMillis());
            existDriverDao.insert(existDriver);
            return BaseResultUtil.fail("账号已存在于该企业承运商中");
        }
        //验证在个人司机中是否存在
        count = driverDao.existPersonDriverNew(dto);
        if(count > 0){
            return BaseResultUtil.fail("账号已存在于个人司机中");
        }
        //获取承运商
        Carrier carrier = carrierDao.selectById(Long.valueOf(urd.getDeptId()));
        if(carrier == null){
            return BaseResultUtil.fail("该承运商不存在，请检查");
        }
        Driver driver = driverDao.selectById(dto.getLoginId());
        if(driver == null){
            return BaseResultUtil.fail("该司机不存在，请检查");
        }
        if(!(dto.getPhone().equals(carrier.getLinkmanPhone()) && dto.getPhone().equals(driver.getPhone()))){
            return BaseResultUtil.fail("手机号不是该登录用户手机号，请修改一致");
        }
        //更新承运商信息
        carrier.setState(CommonStateEnum.IN_CHECK.code);
        carrier.setName(dto.getRealName());
        carrier.setLinkman(dto.getRealName());
        carrier.setLegalName(dto.getRealName());
        carrier.setLegalIdCard(dto.getIdCard());
        carrier.setMode(dto.getMode());
        carrierDao.updateById(carrier);
        //更新司机信息
        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setCreateUserId(dto.getLoginId());
        driverDao.updateById(driver);

        //更新状态(审核中)
        urd.setState(CommonStateEnum.IN_CHECK.code);
        urd.setMode(dto.getMode());
        urd.setUpdateTime(System.currentTimeMillis());
        urd.setUpdateUserId(dto.getLoginId());
        userRoleDeptDao.updateById(urd);
        //运力信息
        DriverVehicleCon vehicleCon = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda()
                .eq(DriverVehicleCon::getDriverId, dto.getLoginId()));
        if(dto.getFlag() == 0 && dto.getVehicleId() != null && vehicleCon == null){
            vehicleCon = new DriverVehicleCon();
            vehicleCon.setDriverId(dto.getLoginId());
            vehicleCon.setVehicleId(dto.getVehicleId());
            driverVehicleConDao.insert(vehicleCon);

            VehicleRunning vr = new VehicleRunning();
            BeanUtils.copyProperties(dto,vr);
            vr.setDriverId(dto.getLoginId());
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            vr.setState(RunningStateEnum.EFFECTIVE.code);
            vr.setRunningState(VehicleRunStateEnum.FREE.code);
            vr.setCreateTime(System.currentTimeMillis());
            vehicleRunningDao.insert(vr);
        }else if(dto.getFlag() == 1 && dto.getVehicleId() != null){
            VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                    .eq(VehicleRunning::getDriverId, dto.getLoginId()));
            if(vr != null){
                List<Task> taskList = taskDao.selectList(new QueryWrapper<Task>().lambda()
                        .eq(Task::getVehicleRunningId,vr.getId())
                        .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                if(!CollectionUtils.isEmpty(taskList)){
                    return BaseResultUtil.fail("该运力正在运输中，不可修改");
                }
            }
            //更新
            if(vehicleCon != null){
                vehicleCon.setDriverId(dto.getLoginId());
                vehicleCon.setVehicleId(dto.getVehicleId());
                driverVehicleConDao.updateById(vehicleCon);
            }
            if(vr != null){
                vr.setDriverId(dto.getLoginId());
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(RunningStateEnum.EFFECTIVE.code);
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vehicleRunningDao.updateById(vr);
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<AppDriverInfoVo> findNewDriverInfoNew(AppDriverDto dto){
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        AppDriverInfoVo appDriverInfo = carrierDao.findAppDriverInfoNew(dto.getRoleId(), dto.getLoginId());
        return BaseResultUtil.success(appDriverInfo);
    }

    @Override
    public ResultVo<SocietyDriverVo> showDriverInfoNew(AppDriverDto dto){
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        SocietyDriverVo personInfo = driverDao.findPersonInfoNew(dto);
        return BaseResultUtil.success(personInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo addBankCardNew(BankCardDto dto){
        log.info("个人司机添加银行卡请求json数据 :: "+ JsonUtils.objectToJson(dto));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }

        //司机审核通过放可以添加银行卡
        if(CommonStateEnum.CHECKED.code != urd.getState()){
            return BaseResultUtil.fail("该司机审核未通过,不可添加银行卡");
        }
        //司机只可添加一张银行卡
        int bankCardInfoNum = bankCardBindDao.findBankCardInfoNum(Long.parseLong(urd.getDeptId()));
        if(bankCardInfoNum >= 1){
            return BaseResultUtil.fail("该司机已绑定过银行卡,不可再绑定");
        }
        BankCardBind bcb = new BankCardBind();
        bcb.setUserId(Long.valueOf(urd.getDeptId()));
        bcb.setCardNo(dto.getCardNo());
        bcb.setUserType(UserTypeEnum.DRIVER.code);
        bcb.setCardType(CardTypeEnum.PRIVATE.code);
        bcb.setCardName(dto.getRealName());
        bcb.setCardPhone(dto.getPhone());
        bcb.setIdCard(dto.getIdCard());
        bcb.setCardColour(RandomUtil.getIntRandom());
        bcb.setBankName(dto.getBankName());
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCreateTime(System.currentTimeMillis());
        //获取银行编码
        BankInfo bankInfo = bankInfoService.findBankCode(bcb.getBankName());
        if(bankInfo != null){
            bcb.setBankCode(bankInfo.getBankCode());
        }
        bankCardBindDao.insert(bcb);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo removeBankCardNew(RemoveBankCardDto dto){
        log.info("删除银行卡请求json数据 :: "+ JsonUtils.objectToJson(dto));
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getId, dto.getRoleId()));
        if(urd == null){
            return BaseResultUtil.fail("该司机不存在,请检查");
        }
        Role role = roleDao.selectOne(new QueryWrapper<Role>().lambda().eq(Role::getId, urd.getRoleId()));
        if(role != null && !"个人司机".equals(role.getRoleName())){
            return BaseResultUtil.fail("非个人司机不可删除");
        }
        boolean result = csSmsService.validateCaptcha(dto.getPhone(),dto.getCode(),CaptchaTypeEnum.valueOf(dto.getType()), ClientEnum.APP_DRIVER);
        if(!result){
            return BaseResultUtil.fail("验证码与手机号不匹配或者过期，请核对发送");
        }
        BankCardBind bcb = bankCardBindDao.selectById(dto.getCardId());
        log.info("删除银行卡信息：：银行卡id: "+bcb.getId()+" 银行卡卡号："+bcb.getCardNo()+" 手机号：："+bcb.getCardName());
        bankCardBindDao.deleteById(bcb);
        return BaseResultUtil.success();
    }
}
