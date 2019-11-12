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
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.FlagEnum;
import com.cjyc.common.model.enums.saleman.SalemanStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.web.api.exception.CommonException;
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
import java.math.BigDecimal;
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
    private ICarrierCityConService carrierCityConService;

    @Autowired
    private ISysUserService sysUserService;

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
    public ResultVo existDriver(String phone) {
        //判断散户司机是否在个人司机/承运商中已存在
        String realName = driverDao.existCarrier(phone,CarrierTypeEnum.PERSONAL.code);
        if(StringUtils.isNotBlank(realName)){
            return BaseResultUtil.fail("账号已存在于个人司机中");
        }
        String name = driverDao.existCarrier(phone,CarrierTypeEnum.ENTERPRISE.code);
        if(StringUtils.isNotBlank(name)){
            return BaseResultUtil.fail("该司机已存在于["+name+"]不可创建");
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo saveDriver(DriverDto dto) {
        //保存散户司机
        Driver driver = new Driver();
        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setType(DriverTypeEnum.SOCIETY.code);
        driver.setIdentity(DriverIdentityEnum.PERSONAL_DRIVER.code);
        driver.setBusinessState(BusinessStateEnum.BUSINESS.code);
        driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
        driver.setIsRed(DriverRedEnum.NORED.code);
        driver.setState(CommonStateEnum.WAIT_CHECK.code);
        driver.setCreateTime(NOW);
        driver.setCreateUserId(dto.getLoginId());
        super.save(driver);

        if(StringUtils.isNotBlank(dto.getPlateNo()) && dto.getVehicleId() != null
            && dto.getDefaultCarryNum() != null){
            //保存司机与车辆关系
            bindDriverVehicle(dto);
        }
        //保存个人承运商
        Carrier carrier = new Carrier();
        carrier.setName(dto.getRealName());
        carrier.setLinkman(dto.getRealName());
        carrier.setLinkmanPhone(dto.getPhone());
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
        cdc.setRole(DriverIdentityEnum.PERSONAL_DRIVER.code);
        carrierDriverConDao.insert(cdc);
        //添加承运商业务范围
        carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo findDriver(SelectDriverDto dto) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<DriverVo> driverVos = driverDao.getDriverByTerm(dto);
        if(!CollectionUtils.isEmpty(driverVos)){
            for(DriverVo vo : driverVos){
                CarrierCarCount count = carrierCarCountDao.count(vo.getId());
                if(count != null){
                    vo.setCarNum(count.getCarNum() == null ? 0:count.getCarNum());
                    vo.setTotalIncome(count.getIncome() == null ? BigDecimal.ZERO:count.getIncome().divide(new BigDecimal(100)));
                }
            }
        }
        PageInfo<DriverVo> pageInfo = new PageInfo<>(driverVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public boolean verifyDriver(OperateDto dto) {
        //获取司
        Driver driver = driverDao.selectById(dto.getId());
        //获取承运商
        Carrier carr = carrierDao.getCarrierById(dto.getId());
        //审核通过
        if(dto.getFlag() == FlagEnum.AUDIT_PASS.code){
            //保存司机用户到平台，返回用户id
            ResultData<Long> saveRd = saveDriverToPlatform(driver);
            if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
                throw new CommonException("司机信息保存失败，原因：" + saveRd.getMsg());
            }
            driver.setUserId(saveRd.getData());
            driver.setState(CommonStateEnum.CHECKED.code);
            //更新承运商
            carr.setState(CommonStateEnum.CHECKED.code);
            //更新运力
            VehicleRunning vr = vehicleRunningDao.getVehiRunByDriverId(dto.getId());
            if(vr != null){
                vr.setState(VehicleStateEnum.EFFECTIVE.code);
                vehicleRunningDao.updateById(vr);
            }
        }else if(dto.getFlag() == FlagEnum.AUDIT_REJECT.code){
            //审核拒绝
            driver.setState(CommonStateEnum.REJECT.code);
            //更新承运商
            carr.setState(CommonStateEnum.REJECT.code);
        }else if(dto.getFlag() == FlagEnum.FROZEN.code){
            //冻结
            driver.setState(CommonStateEnum.FROZEN.code);
            //更新承运商
            carr.setState(CommonStateEnum.FROZEN.code);
        }else if(dto.getFlag() == FlagEnum.THAW.code){
            //解除
            driver.setState(CommonStateEnum.CHECKED.code);
            //更新承运商
            carr.setState(CommonStateEnum.CHECKED.code);
        }
        driver.setCheckTime(NOW);
        driver.setCheckUserId(dto.getLoginId());
        carr.setCheckTime(NOW);
        carr.setCheckUserId(dto.getLoginId());
        driverDao.updateById(driver);
        carrierDao.updateById(carr);
        return true;
    }

    @Override
    public ResultVo showDriver(Long driverId) {
        ShowDriverVo vo = driverDao.getDriverById(driverId);
        if(vo != null){
            //根据司机id获取该承运商id
            Long carrierId = driverDao.getCarrIdByDriverId(driverId);
            if(carrierId != null){
                vo.setMapCodes(carrierCityConService.getMapCodes(carrierId));
            }
        }
        return BaseResultUtil.success(vo);
    }

    @Override
    public ResultVo modifyDriver(DriverDto dto) {
        //判断散户司机是否已存在
        Driver dri = driverDao.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getPhone,dto.getPhone())
                .or().eq(Driver::getIdCard,dto.getIdCard()));
        if(dri != null && !dri.getId().equals(dto.getDriverId())){
            return BaseResultUtil.fail("该司机已存在,请检查");
        }
        //获取运力信息
        VehicleRunning vRun = vehicleRunningDao.selectOne(new QueryWrapper<VehicleRunning>().lambda().eq(VehicleRunning::getDriverId,dto.getDriverId()));
        if(vRun != null){
            Task task = taskDao.selectOne(new QueryWrapper<Task>().lambda().eq(Task::getVehicleRunningId,vRun.getId()));
            if(task != null && task.getState() == TaskStateEnum.TRANSPORTING.code){
                return BaseResultUtil.fail("该运力正在运输中，不可修改");
            }
        }
        //更新司机信息
       Driver driver = driverDao.selectById(dto.getDriverId());
        //修改司机信息
       ResultData rd = updateUserToPlatform(driver);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            throw new CommonException("司机信息同步失败，原因：" + rd.getMsg());
        }
        BeanUtils.copyProperties(dto,driver);
        driver.setId(dto.getDriverId());
        super.updateById(driver);

        //车牌号不为空 & 之前司机绑定不为空 & 车牌号与之前不同
        DriverVehicleCon dvc = driverVehicleConDao.getDriVehConByDriId(dto.getDriverId());
        if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc != null){
            //更新绑定车辆信息
            dvc.setVehicleId(dto.getVehicleId());
            driverVehicleConDao.updateById(dvc);
            //更新运力池信息
            VehicleRunning vr = vehicleRunningDao.getVehiRunByDriverId(dto.getDriverId());
            if(vr != null){
                vr.setVehicleId(dto.getVehicleId());
                vr.setPlateNo(dto.getPlateNo());
                vehicleRunningDao.updateById(vr);
            }
        }else if(StringUtils.isBlank(dto.getPlateNo()) && dvc != null){
            //之前绑定不为空，现在不绑定车辆
            //删除之前绑定关系
            driverVehicleConDao.removeCon(dto.getDriverId());
            vehicleRunningDao.removeRun(dto.getDriverId());
        }else if(StringUtils.isNotBlank(dto.getPlateNo()) && dvc == null){
            //之前绑定为空，现在不为空，需要绑定
            //车辆与司机绑定
            bindDriverVehicle(dto);
        }
        //更新承运商信息
        Carrier carrier = carrierDao.getCarrierById(dto.getDriverId());
        if(carrier != null){
            carrier.setName(dto.getRealName());
            carrier.setLinkman(dto.getRealName());
            carrier.setLinkmanPhone(dto.getPhone());
            carrierDao.updateById(carrier);
        }
        //更新承运商业务范围
        //承运商业务范围,先批量删除，再添加
        carrierCityConService.batchDelete(carrier.getId());
        carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        return BaseResultUtil.success();
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
        driver.setState(flag.equals(1)?
                SalemanStateEnum.REJECTED.code: SalemanStateEnum.CHECKED.code);
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
        vr.setRunningState(BusinessStateEnum.OUTAGE.code);
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
        ResultData<AddUserResp> accountRd = sysUserService.getByAccount(driver.getPhone());
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

    /**
     * 将司机信息更新到平台用户
     * @param driver
     * @return
     */
    private ResultData updateUserToPlatform(Driver driver) {
        UpdateUserReq user = new UpdateUserReq();
        user.setUserId(driver.getUserId());
        user.setName(driver.getName());
        user.setAccount(driver.getPhone());
        user.setMobile(driver.getPhone());
        return sysUserService.updateUser(user);
    }
}
