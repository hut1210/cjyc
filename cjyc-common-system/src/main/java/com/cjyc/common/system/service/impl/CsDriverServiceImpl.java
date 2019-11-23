package com.cjyc.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.driver.mine.CarrierDriverNameDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsDriverService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
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

    @Autowired
    private ISysUserService sysUserService;

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
    public ResultVo saveOrModifyDriver(com.cjyc.common.model.dto.CarrierDriverDto dto) {
        if(dto.getCarrierId() == null && dto.getRoleId() != null){
            //web承运商管理员登陆
            CarrierDriverCon carrierDriCon = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                    .eq(CarrierDriverCon::getDriverId, dto.getDriverId())
                    .eq(CarrierDriverCon::getId, dto.getRoleId()));
            dto.setCarrierId(carrierDriCon.getCarrierId());
        }
        //验证在个人司机池中是否存在
        Integer count = carrierDao.existPersonalCarrier(dto);
        if(count > 0){
            return BaseResultUtil.getVo(ResultEnum.EXIST_PERSONAL_CARRIER.getCode(),ResultEnum.EXIST_PERSONAL_CARRIER.getMsg());
        }
        //验证在该承运商下是否有相同的
        count = carrierDao.existBusinessCarrier(dto);
        if(count <= 0){
            return BaseResultUtil.getVo(ResultEnum.EXIST_ENTERPRISE_CARRIER.getCode(),ResultEnum.EXIST_ENTERPRISE_CARRIER.getMsg());
        }
        if(dto.getDriverId() == null){
            //保存司机
            Driver driver = new Driver();
            BeanUtils.copyProperties(dto,driver);
            driver.setName(dto.getRealName());
            driver.setType(DriverTypeEnum.SOCIETY.code);
            driver.setIdentity(DriverIdentityEnum.SUB_DRIVER.code);
            driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
            if(dto.getDriverId().equals(dto.getLoginId())){
                //承运商超级管理员登陆
                driver.setSource(DriverSourceEnum.CARRIER_ADMIN.code);
            }else if(dto.getCarrierId().equals(dto.getLoginId())){
                //业务员登陆
                driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
            }
            driver.setCreateUserId(dto.getLoginId());
            driver.setCreateTime(NOW);
            //司机信息保存
            ResultData<Long> rd = addDriverToPlatform(driver, dto);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                return BaseResultUtil.fail(rd.getMsg());
            }
            driver.setUserId(rd.getData());
            driverDao.insert(driver);

            //保存司机与承运商关系
            CarrierDriverCon driverCon = new CarrierDriverCon();
            driverCon.setCarrierId(dto.getCarrierId());
            driverCon.setDriverId(driver.getId());
            driverCon.setMode(dto.getMode());
            driverCon.setState(CommonStateEnum.CHECKED.code);
            driverCon.setRole(DriverIdentityEnum.SUB_DRIVER.code);
            carrierDriverConDao.insert(driverCon);
            //车牌号不为空
            if(StringUtils.isNotBlank(dto.getPlateNo())){
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
                vr.setRunningState(VehicleRunStateEnum.FREE.code);
                vr.setCreateTime(NOW);
                vehicleRunningDao.insert(vr);
            }
        }else{
            return modifyDriver(dto);
        }
        return null;
    }

    @Override
    public ResultData<Long> addDriverToPlatform(Driver driver, com.cjyc.common.model.dto.CarrierDriverDto dto) {
        List<Driver> existList = driverDao.selectList(new QueryWrapper<Driver>().lambda()
                .eq(Driver::getPhone, driver.getPhone()));
        if (!CollectionUtils.isEmpty(existList)) {
            return ResultData.failed("手机号已存在，请检查");
        }
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
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
            //司机不存在, 需新增
            AddUserReq userReq = new AddUserReq();
            userReq.setAccount(driver.getPhone());
            userReq.setDeptId(carrier.getDeptId());
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
    public ResultData updateDriverToPlatform(com.cjyc.common.model.dto.CarrierDriverDto dto) {
        Driver driver = driverDao.selectById(dto.getDriverId());
        if (null == driver) {
            return ResultData.failed("司机信息错误，根据id：" + dto.getDriverId() + "未查询到信息");
        }
        //比对是否需要变更手机号
        if (!driver.getPhone().equals(dto.getPhone())) {
            //需要手机号变更操作
            //手机号是否在司机表存在
            List<Driver> drivers = driverDao.selectList(new QueryWrapper<Driver>()
                    .eq("", dto.getPhone()));
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
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda()
                .eq(CarrierDriverCon::getId, dto.getRoleId())
                .eq(CarrierDriverCon::getDriverId, dto.getLoginId()));
        //查询该承运商下的符合的全部司机
        List<FreeDriverVo> freeDriverVos = driverDao.findCarrierDriver(dto);
        if(cdc != null){
            if(!CollectionUtils.isEmpty(freeDriverVos)){
                return freeDriver(freeDriverVos,cdc.getCarrierId());
            }
        }
        return BaseResultUtil.fail("数据有误，请联系管理员");
    }

    @Override
    public ResultVo<List<FreeDriverVo>> findCarrierDriver(CarrierDriverNameDto dto) {
        List<FreeDriverVo> freeDriverVos = driverDao.findCarrierAllDriver(dto);
        if(!CollectionUtils.isEmpty(freeDriverVos)){
           return freeDriver(freeDriverVos,dto.getCarrierId());
        }
        return BaseResultUtil.fail("该数据有误，请联系管理员");
    }

    /**
     * 获取该承运商下空闲司机
     * @param freeDriverVos
     * @param carrierId
     * @return
     */
    private ResultVo freeDriver(List<FreeDriverVo> freeDriverVos,Long carrierId){
        //查询已被绑定的司机
        List<Long> driverIds = driverDao.findCarrierBusyDriver(carrierId);
        //去除已绑定司机
        if(!CollectionUtils.isEmpty(driverIds)){
            for (Long driverId : driverIds) {
                for (FreeDriverVo vo : freeDriverVos) {
                    if(driverId.equals(vo.getDriverId())){
                        freeDriverVos.remove(vo);
                        break;
                    }
                }
            }
            return BaseResultUtil.success(freeDriverVos);
        }
        return BaseResultUtil.fail("数据有误，请联系管理员");
    }
    /**
     * 修改承运商下司机与车辆关系
     * @param dto
     * @return
     */
    private ResultVo modifyDriver(com.cjyc.common.model.dto.CarrierDriverDto dto) {
        //判断司机是否已存在
        Driver dri = driverDao.selectById(dto.getDriverId());
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getDriverId,dto.getDriverId()));
        VehicleRunning vr = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId,dri.getId()));
        if(vr != null) {
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda().eq(Task::getVehicleRunningId, vr.getId()));
            if (task != null && task.getState() == TaskStateEnum.TRANSPORTING.code) {
                return BaseResultUtil.getVo(ResultEnum.VEHICLE_RUNNING.getCode(),ResultEnum.VEHICLE_RUNNING.getMsg());
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
        vr.setRunningState(VehicleRunStateEnum.FREE.code);
        vr.setCreateTime(NOW);
        vehicleRunningDao.insert(vr);
    }
}
