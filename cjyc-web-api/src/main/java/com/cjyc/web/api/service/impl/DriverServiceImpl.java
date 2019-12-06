package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserReq;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UserResp;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.*;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.driver.DriverIdentityEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.ExistCarrierVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ExistDriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.web.api.service.ICarrierCityConService;
import com.cjyc.web.api.service.IDriverService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DriverServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IDriverService {

    @Resource
    private IDriverDao driverDao;
    @Resource
    private IDriverVehicleConDao driverVehicleConDao;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private ICarrierDriverConDao carrierDriverConDao;
    @Resource
    private ICarrierCarCountDao carrierCarCountDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IExistDriverDao existDriverDao;
    @Resource
    private ICarrierCityConService carrierCityConService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ICsDriverService csDriverService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    /**
     * 查询司机列表
     *
     * @author JPG
     * @since 2019/10/16 16:15
     */
    @Override
    public ResultVo<PageVo<DriverListVo>> lineWaitDispatchCarCountList(DriverListDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<DriverListVo> list = driverDao.findList(paramsDto);
        return null;
    }

    @Override
    public ResultVo saveOrModifyDriver(DriverDto dto) {
        ExistCarrierVo carrierVo = carrierDao.existCarrier(dto);
        if(carrierVo != null){
            if(carrierVo.getType() == CarrierTypeEnum.PERSONAL.code){
                return BaseResultUtil.fail("账号已存在于个人司机中");
            }
            if(carrierVo.getType() == CarrierTypeEnum.ENTERPRISE.code){
                return BaseResultUtil.fail("该司机已存在于["+carrierVo.getName()+"]不可创建");
            }
        }
        if(dto.getDriverId() == null){
            //保存散户司机
            Driver driver = new Driver();
            BeanUtils.copyProperties(dto,driver);
            driver.setName(dto.getRealName());
            driver.setType(DriverTypeEnum.SOCIETY.code);
            driver.setIdentity(DriverIdentityEnum.GENERAL_DRIVER.code);
            driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
            driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
            driver.setCreateTime(NOW);
            driver.setCreateUserId(dto.getLoginId());
            ResultData<Long> saveRd = csDriverService.saveDriverToPlatform(driver);
            if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
                return BaseResultUtil.fail("司机信息保存失败，原因：" + saveRd.getMsg());
            }
            driver.setUserId(saveRd.getData());
            super.save(driver);

            if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null
                    && dto.getDefaultCarryNum() != null){
                //保存司机与车辆关系
                dto.setDriverId(driver.getId());
                bindDriverVehicle(dto);
            }
            //保存个人承运商
            Carrier carrier = new Carrier();
            carrier.setLegalName(dto.getRealName());
            carrier.setLegalIdCard(dto.getIdCard());
            carrier.setName(dto.getRealName());
            carrier.setLinkman(dto.getRealName());
            carrier.setLinkmanPhone(dto.getPhone());
            carrier.setMode(dto.getMode());
            carrier.setType(CarrierTypeEnum.PERSONAL.code);
            carrier.setSettleType(ModeTypeEnum.TIME.code);
            carrier.setState(CommonStateEnum.WAIT_CHECK.code);
            carrier.setBusinessState(BusinessStateEnum.BUSINESS.code);
            carrier.setCreateTime(NOW);
            carrier.setCreateUserId(dto.getLoginId());
            carrierDao.insert(carrier);

            //保存司机与承运商关系
            CarrierDriverCon cdc = new CarrierDriverCon();
            cdc.setCarrierId(carrier.getId());
            cdc.setDriverId(driver.getId());
            cdc.setMode(dto.getMode());
            cdc.setState(CommonStateEnum.WAIT_CHECK.code);
            cdc.setRole(DriverRoleEnum.PERSONAL_DRIVER.code);
            carrierDriverConDao.insert(cdc);
            //添加承运商业务范围
            carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        }else{
            return modifyDriver(dto);
        }
        return BaseResultUtil.success();
    }

    /**
     * 修改司机信息
     * @param dto
     * @return
     */
    private ResultVo modifyDriver(DriverDto dto) {
        //获取运力信息
        VehicleRunning vRun = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId,dto.getDriverId()));
        if(vRun != null){
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda().eq(Task::getVehicleRunningId,vRun.getId()));
            if(task != null && task.getState() == TaskStateEnum.TRANSPORTING.code){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        //更新司机信息
        Carrier carrier = carrierDao.selectById(dto.getCarrierId());
        Driver driver = driverDao.selectById(dto.getDriverId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>()
                .lambda().eq(CarrierDriverCon::getDriverId,driver.getId())
                .eq(CarrierDriverCon::getCarrierId,carrier.getId()));
        if(carrier == null || driver == null || cdc == null){
            return BaseResultUtil.fail("数据错误");
        }
        //修改司机信息
        //if(cdc.getState() == CommonStateEnum.CHECKED.code){
        ResultData rd = csDriverService.updateUserToPlatform(driver);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("司机信息同步失败，原因：" + rd.getMsg());
        }
       // }

        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setId(dto.getDriverId());
        driver.setCheckTime(NOW);
        driver.setCheckUserId(dto.getLoginId());
        super.updateById(driver);

        //车牌号不为空 & 之前司机绑定不为空 & 车牌号与之前不同
        DriverVehicleCon dvc = driverVehicleConDao.getDriVehConByDriId(dto.getDriverId());
        if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc != null
                && !dvc.getVehicleId().equals(dto.getVehicleId())){
            //更新绑定车辆信息
            dvc.setVehicleId(dto.getVehicleId());
            driverVehicleConDao.updateById(dvc);
            //更新运力池信息
            VehicleRunning vr = vehicleRunningDao.getVehiRunByDriverId(dto.getDriverId());
            if(vr != null){
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vehicleRunningDao.updateById(vr);
            }
        }else if(StringUtils.isBlank(dto.getPlateNo()) && dvc != null){
            //之前绑定不为空，现在不绑定车辆
            //删除之前绑定关系
            driverVehicleConDao.removeCon(dvc.getDriverId(),dvc.getVehicleId());
            vehicleRunningDao.removeRun(dvc.getDriverId(),dvc.getVehicleId());
        }else if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc == null){
            //之前绑定为空，现在不为空，需要绑定
            //车辆与司机绑定
            bindDriverVehicle(dto);
        }
        //更新承运商信息
        if(carrier != null){
            BeanUtils.copyProperties(dto,carrier);
            carrier.setState(CommonStateEnum.WAIT_CHECK.code);
            carrier.setName(dto.getRealName());
            carrier.setLegalName(dto.getRealName());
            carrier.setLegalIdCard(dto.getIdCard());
            carrier.setLinkman(dto.getRealName());
            carrier.setLinkmanPhone(dto.getPhone());
            carrier.setMode(dto.getMode());
            carrierDao.updateById(carrier);
        }
        //更新承运商业务范围
        //承运商业务范围,先批量删除，再添加
        carrierCityConService.batchDelete(carrier.getId());
        carrierCityConService.batchSave(carrier.getId(),dto.getCodes());

        //更新司机与承运商之间关系状态
        cdc.setMode(dto.getMode());
        cdc.setState(CommonStateEnum.WAIT_CHECK.code);
        carrierDriverConDao.updateById(cdc);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<DriverVo>> findDriver(SelectDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<DriverVo> driverVos = driverDao.getDriverByTerm(dto);
        if(!CollectionUtils.isEmpty(driverVos)){
            for(DriverVo vo : driverVos){
                CarrierCarCount count = carrierCarCountDao.count(vo.getCarrierId());
                if(count != null){
                    vo.setCarNum(count.getCarNum());
                    vo.setTotalIncome(count.getIncome());
                }
            }
        }
        PageInfo<DriverVo> pageInfo = new PageInfo<>(driverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo verifyDriver(OperateDto dto) {
        //获取承运商
        Carrier carr = carrierDao.selectById(dto.getId());
        CarrierDriverCon cdc = carrierDriverConDao.selectOne(new QueryWrapper<CarrierDriverCon>().lambda().eq(CarrierDriverCon::getCarrierId, carr.getId()));
        //获取司机,该id为承运商的id
        Driver driver = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getId,cdc.getDriverId()));
        if (null == carr || null == cdc || null == driver) {
            return BaseResultUtil.fail("承运商信息错误，请检查");
        }
        //审核通过
        if(dto.getFlag() == FlagEnum.AUDIT_PASS.code){
            //保存司机用户到平台，返回用户id
            cdc.setState(CommonStateEnum.CHECKED.code);
            //更新承运商
            carr.setState(CommonStateEnum.CHECKED.code);
        }else if(dto.getFlag() == FlagEnum.AUDIT_REJECT.code){
            //审核拒绝
            cdc.setState(CommonStateEnum.REJECT.code);
            //更新承运商
            carr.setState(CommonStateEnum.REJECT.code);
        }else if(dto.getFlag() == FlagEnum.FROZEN.code){
            //冻结
            cdc.setState(CommonStateEnum.FROZEN.code);
            //更新承运商
            carr.setState(CommonStateEnum.FROZEN.code);
        }else if(dto.getFlag() == FlagEnum.THAW.code){
            //解除
            cdc.setState(CommonStateEnum.CHECKED.code);
            //更新承运商
            carr.setState(CommonStateEnum.CHECKED.code);
        }
        carr.setCheckTime(NOW);
        carr.setCheckUserId(dto.getLoginId());
        driver.setCheckUserId(dto.getLoginId());
        driver.setCheckTime(NOW);
        driverDao.updateById(driver);
        carrierDriverConDao.updateById(cdc);
        carrierDao.updateById(carr);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo showDriver(BaseCarrierIdDto dto) {
        ShowDriverVo vo = driverDao.getDriverById(dto.getCarrierId());
        if(vo != null){
            //根据司机id获取该承运商id
            if(dto.getCarrierId() != null){
                vo.setMapCodes(carrierCityConService.getMapCodes(dto.getCarrierId()));
            }
        }
        return BaseResultUtil.success(vo);
    }

    @Override
    public ResultVo<List<ExistDriverVo>> showExistDriver() {
        List<ExistDriverVo> existDriverVos = existDriverDao.findExistDriver();
        return BaseResultUtil.success(existDriverVos);
    }

    @Override
    public Driver getByUserId(Long userId) {
        return driverDao.findByUserId(userId);
    }

    @Override
    public ResultVo resetState(Long id, Integer flag) {
        if (!flag.equals(1) && !flag.equals(2)) {
            return BaseResultUtil.fail("状态表示：" + flag + " 无效，请确认");
        }
        Driver driver = driverDao.selectById(id);
        if (null == driver) {
            return BaseResultUtil.fail("司机信息错误，请检查");
        }
        //driver.setState(flag.equals(1)?
                //SalemanStateEnum.REJECTED.code: SalemanStateEnum.CHECKED.code);
        driverDao.updateById(driver);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo dispatchDriver(DispatchDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<DispatchDriverVo> dispatchDriverVos = driverDao.getDispatchDriver(dto);
        PageInfo<DispatchDriverVo> pageInfo = new PageInfo<>(dispatchDriverVos);
        return BaseResultUtil.success(pageInfo);
    }
    @Override
    public ResultVo<PageVo<DispatchDriverVo>> carrierDrvierList(CarrierDriverListDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize(), true);
        List<DispatchDriverVo> dispatchDriverVos = driverDao.findCarrierDrvierList(dto);
        PageInfo<DispatchDriverVo> pageInfo = new PageInfo<>(dispatchDriverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public Driver getById(Long id, boolean isSearchCache) {
        return driverDao.selectById(id);
    }


    /**
     * 司机与车辆绑定关系
     * @param dto
     */
    private void bindDriverVehicle(DriverDto dto){
        //车辆与司机绑定
        DriverVehicleCon dvcon = new DriverVehicleCon();
        dvcon.setDriverId(dto.getDriverId());
        dvcon.setVehicleId(dto.getVehicleId());
        driverVehicleConDao.insert(dvcon);
        //绑定运力池信息
        VehicleRunning vr = new VehicleRunning();
        vr.setDriverId(dto.getDriverId());
        vr.setVehicleId(dto.getVehicleId());
        vr.setPlateNo(dto.getPlateNo());
        vr.setCarryCarNum(dto.getDefaultCarryNum());
        vr.setOccupiedCarNum(0);
        vr.setRunningState(VehicleRunStateEnum.FREE.code);
        vr.setCreateTime(NOW);
        vehicleRunningDao.insert(vr);
    }

    /**
     * 将用户信息保存到物流平台
     * @param driver
     * @return
     */
    private ResultData<Long> saveDriverToPlatform(Driver driver) {
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
            user.setPassword(YmlProperty.get("cjkj.salesman.password"));
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
}
