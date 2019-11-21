package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdateUserReq;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.CarrierVehicleDto;
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
import com.cjyc.common.system.service.ICsDriverService;
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
    private IWaybillDao waybillDao;
    @Resource
    private ICsDriverService csDriverService;

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
    public ResultVo saveOrModifyVehicle(CarrierVehicleDto dto) {
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
    private ResultVo modifyVehicle(CarrierVehicleDto dto) {
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
                csDriverService.bindDriverVeh(dto);
            }
        }
        return BaseResultUtil.success();
    }



}