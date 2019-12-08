package com.cjyc.driver.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.mine.*;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.VehicleOwnerEnum;
import com.cjyc.common.model.enums.transport.VehicleRunStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.RandomUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.*;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.driver.api.service.IMineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
    private ICsSmsService csSmsService;
    @Resource
    private ICsVehicleService csVehicleService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

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
        if(!CollectionUtils.isEmpty(bankCardVos)){
            cardVos.setCardVos(bankCardVos);
        }
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
        driver.setCheckTime(NOW);
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
            veh.setCreateTime(NOW);
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
                Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda()
                        .eq(Task::getVehicleRunningId,vr.getId())
                        .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                if(task != null){
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
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda()
                    .eq(Task::getVehicleRunningId,vr.getId())
                    .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
            if(task != null){
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
            existDriver.setCreateTime(NOW);
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
            vr.setRunningState(VehicleRunStateEnum.FREE.code);
            vr.setCreateTime(NOW);
            vehicleRunningDao.insert(vr);
        }else if(dto.getFlag() == 1 && dto.getVehicleId() != null){
            VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda()
                    .eq(VehicleRunning::getDriverId, dto.getLoginId()));
            if(vr != null){
                Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda()
                        .eq(Task::getVehicleRunningId,vr.getId())
                        .eq(Task::getState,TaskStateEnum.TRANSPORTING.code));
                if(task != null){
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
        bcb.setCreateTime(NOW);
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
}
