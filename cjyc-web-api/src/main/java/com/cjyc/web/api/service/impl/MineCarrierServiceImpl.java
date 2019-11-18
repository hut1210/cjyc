package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mineCarrier.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyDriverVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyFreeDriverVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyWaybillVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.web.api.service.IMineCarrierService;
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
public class MineCarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements IMineCarrierService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

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
    private ITaskDao taskDao;

    @Resource
    private ICarrierDao carrierDao;

    @Resource
    private IWaybillDao waybillDao;

    @Resource
    private ISysUserService sysUserService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo<PageVo<MyWaybillVo>> findWaybill(MyWaybillDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<MyWaybillVo> waybillVos = waybillDao.findByCarrierId(dto);
        PageInfo<MyWaybillVo> pageInfo = new PageInfo<>(waybillVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo saveOrModifyDriver(MyDriverDto dto) {
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
            driver.setBusinessState(BusinessStateEnum.OUTAGE.code);
            if(dto.getCarrierId().equals(dto.getLoginId())){
                //承运商超级管理员登陆
                driver.setSource(DriverSourceEnum.CARRIER_ADMIN.code);
            }else {
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
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<MyDriverVo>> findPageDriver(QueryMyDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<MyDriverVo> myDriverVos =  driverDao.findMyDriver(dto);
        if(!CollectionUtils.isEmpty(myDriverVos)){
            for(MyDriverVo vo : myDriverVos){
                CarrierCarCount count = carrierCarCountDao.driverCount(vo.getDriverId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                }
            }
        }
        PageInfo<MyDriverVo> pageInfo = new PageInfo<>(myDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo verifyDriver(OperateDto dto) {
        Driver driver = driverDao.selectById(dto.getId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda().eq(CarrierDriverCon::getDriverId,dto.getId()));
        if(dto.getFlag() == FlagEnum.ADMINISTRATOR.code){
            //设为管理员
            driver.setIdentity(DriverIdentityEnum.ADMIN.code);
            //修改身份
            cdc.setRole(DriverIdentityEnum.ADMIN.code);
            carrierDriverConDao.updateById(cdc);
        }else if(dto.getFlag() == FlagEnum.REMOVE_ADMINISTRATOR.code){
            //解除管理员
            driver.setIdentity(DriverIdentityEnum.SUB_DRIVER.code);
            //修改身份
            cdc.setRole(DriverIdentityEnum.SUB_DRIVER.code);
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
    public ResultVo saveOrModifyVehicle(MyVehicleDto dto) {
        if(dto.getVehicleId() == null){
            Vehicle vehicle = vehicleDao.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getPlateNo,dto.getPlateNo()));
            if(vehicle != null){
                return BaseResultUtil.fail("该车辆已添加，请核对");
            }
            Vehicle veh = new Vehicle();
            BeanUtils.copyProperties(dto,veh);
            veh.setOwnershipType(VehicleOwnerEnum.CARRIER.code);
            veh.setCarrierId(dto.getCarrierId());
            veh.setCreateUserId(dto.getLoginId());
            veh.setCreateTime(NOW);
            vehicleDao.insert(veh);
            //选择绑定司机
            if(StringUtils.isNotBlank(dto.getPlateNo())){
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
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<MyCarVo> myCarVos = vehicleDao.findMyCar(dto);
        PageInfo<MyCarVo> pageInfo = new PageInfo<>(myCarVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<List<MyFreeDriverVo>> findFreeDriver(Long carrierId, String realName) {
        //查询该承运商下的符合的全部司机
        List<MyFreeDriverVo> freeDriverVos = driverDao.findMyAllDriver(carrierId,realName);
        //查询已被绑定的司机
        List<Long> driverIds = driverDao.findMyBusyDriver(carrierId);
        //去除已绑定司机
        for (Long driverId : driverIds) {
            for (MyFreeDriverVo vo : freeDriverVos) {
                if(driverId.equals(vo.getDriverId())){
                    freeDriverVos.remove(vo);
                    break;
                }
            }
        }
        return BaseResultUtil.success(freeDriverVos);
    }

    /**
     * 修改车辆
     * @param dto
     * @return
     */
    private ResultVo modifyVehicle(MyVehicleDto dto) {
        DriverVehicleCon dvc = driverVehicleConDao.selectOne(new QueryWrapper<DriverVehicleCon>().lambda().eq(DriverVehicleCon::getVehicleId,dto.getVehicleId()));
        if((dvc != null && dto.getDriverId().equals(dvc.getDriverId()))
            || (dvc == null && dto.getDriverId() == null)){
            //选择与之前相同的司机绑定或者之前与现在都没绑定司机
            return BaseResultUtil.success();
        }
        if(dto.getDriverId() != null){
            if(dvc != null){
                //之前绑定与现在绑定不相同，先解绑再绑定新的
                driverVehicleConDao.deleteById(dvc);
                vehicleRunningDao.updateVehicleRunning(dto.getVehicleId());
            }
            if(dvc == null || !dto.getDriverId().equals(dvc.getDriverId())){
                //绑定新的
                bindDriverVeh(dto);
            }
        }
        return BaseResultUtil.success();
    }


    /**
     * 修改承运商下司机与车辆关系
     * @param dto
     * @return
     */
    private ResultVo modifyDriver(MyDriverDto dto) {
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
    private void bindDriverVeh(MyVehicleDto dto){
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
    /**
     * 保存承运商下司机到物流平台
     * @param driver
     * @param dto
     * @return
     */
    private ResultData<Long> addDriverToPlatform(Driver driver, MyDriverDto dto) {
        List<Driver> existList = driverDao.selectList(new QueryWrapper<Driver>()
                .eq("phone", driver.getPhone()));
        if (!CollectionUtils.isEmpty(existList)) {
            return ResultData.failed("手机号已存在，请检查");
        }
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        if (null == carrier || carrier.getDeptId() == null || carrier.getDeptId() <= 0L) {
            return ResultData.failed("承运商信息错误，可能因为该承运商未审核通过");
        }
        ResultData<AddUserResp> rd = sysUserService.getByAccount(driver.getPhone());
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

    /**
     * 更新司机信息到物流平台
     * @param dto
     * @return
     */
    private ResultData updateDriverToPlatform(MyDriverDto dto){
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
            ResultData<AddUserResp> rd = sysUserService.getByAccount(dto.getPhone());
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
}