package com.cjyc.web.api.service.impl;

        import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        import com.cjkj.common.model.ResultData;
        import com.cjkj.common.model.ReturnMsg;
        import com.cjkj.usercenter.dto.common.AddUserReq;
        import com.cjkj.usercenter.dto.common.AddUserResp;
        import com.cjkj.usercenter.dto.common.UpdateUserReq;
        import com.cjyc.common.model.dao.*;
        import com.cjyc.common.model.dto.web.driver.DriverDto;
        import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
        import com.cjyc.common.model.dto.web.user.DriverListDto;
        import com.cjyc.common.model.entity.*;
        import com.cjyc.common.model.enums.CommonStateEnum;
        import com.cjyc.common.model.enums.FlagEnum;
        import com.cjyc.common.model.enums.PayModeEnum;
        import com.cjyc.common.model.enums.saleman.SalemanStateEnum;
        import com.cjyc.common.model.enums.transport.*;
        import com.cjyc.common.model.util.BaseResultUtil;
        import com.cjyc.common.model.util.LocalDateTimeUtil;
        import com.cjyc.common.model.util.YmlProperty;
        import com.cjyc.common.model.vo.PageVo;
        import com.cjyc.common.model.vo.ResultVo;
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
        import org.springframework.transaction.annotation.Propagation;
        import org.springframework.transaction.annotation.Transactional;

        import javax.annotation.Resource;
        import java.time.LocalDateTime;
        import java.util.List;

@Service
@Slf4j
public class DriverServiceImpl extends ServiceImpl<IDriverDao, Driver> implements IDriverService {

    @Resource
    private IAdminDao adminDao;

    @Resource
    private IDriverDao driverDao;

    @Resource
    private IDriverVehicleConDao driverVehicleConDao;

    @Resource
    private ICityDao cityDao;

    @Resource
    private ICarrierDao carrierDao;

    @Resource
    private IVehicleDao vehicleDao;

    @Resource
    private IVehicleRunningDao vehicleRunningDao;

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

    @Resource
    private ICarrierCityConDao carrierCityConDao;

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
    public boolean saveDriver(DriverDto dto) {
        //保存散户司机
        Driver driver = new Driver();
        BeanUtils.copyProperties(dto,driver);
        driver.setName(dto.getRealName());
        driver.setIdentity(DriverIdentityEnum.PERSONAL_DRIVER.code);
        driver.setBusinessState(BusinessStateEnum.OUTAGE.code);
        driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
        driver.setState(CommonStateEnum.WAIT_CHECK.code);
        driver.setCreateTime(NOW);
        driver.setCreateUserId(dto.getUserId());
        driver.setOperateUserId(dto.getUserId());
        driver.setOperateTime(NOW);
        Admin admin = adminDao.selectOne(new QueryWrapper<Admin>().lambda().eq(Admin::getUserId, dto.getUserId()).select(Admin::getName));
        if(admin != null){
            driver.setOperateName(admin.getName());
        }
        super.save(driver);
        if(StringUtils.isNotBlank(dto.getPlateNo())){
            //保存司机与车辆关系
            DriverVehicleCon dvc = new DriverVehicleCon();
            dvc.setDriverId(driver.getId());
            dvc.setVehicleId(dto.getVehicleId());
            driverVehicleConDao.insert(dvc);
            //保存运力信息
            VehicleRunning vr = new VehicleRunning();
            vr.setVehicleId(dto.getVehicleId());
            vr.setDriverId(driver.getId());
            vr.setPlateNo(dto.getPlateNo());
            vr.setCarryCarNum(dto.getDefaultCarryNum());
            vr.setState(VehicleStateEnum.INVALID.code);
            vr.setRunningState(BusinessStateEnum.OUTAGE.code);
            vr.setCreateTime(NOW);
            vehicleRunningDao.insert(vr);
        }
        //保存个人承运商
        Carrier carrier = new Carrier();
        carrier.setName(dto.getRealName());
        carrier.setLinkman(dto.getRealName());
        carrier.setLinkmanPhone(dto.getPhone());
        carrier.setType(CarrierTypeEnum.PERSONAL.code);
        carrier.setSettleType(PayModeEnum.COLLECT.code);
        carrier.setCreateTime(NOW);
        carrier.setCreateUserId(dto.getUserId());
        carrierDao.insert(carrier);
        //保存司机与承运商关系
        CarrierDriverCon cdc = new CarrierDriverCon();
        cdc.setCarrierId(carrier.getId());
        cdc.setDriverId(driver.getId());
        cdc.setRole(DriverIdentityEnum.SUB_DRIVER.code);
        carrierDriverConDao.insert(cdc);
        //添加承运商业务范围
        carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
        return true;
    }

    @Override
    public PageInfo<DriverVo> getDriverByTerm(SelectDriverDto dto) {
        PageInfo<DriverVo> pageInfo = null;
        try{
            List<DriverVo> driverVos = driverDao.getDriverByTerm(dto);
            if(driverVos != null && driverVos.size() > 0){
                PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
                pageInfo = new PageInfo<>(driverVos);
            }
        }catch (Exception e){
            log.info("根据条件查询司机出现异常");
        }
        return pageInfo;
    }

    @Override
    public boolean examineDriById(Long id,Integer flag) {
        try{
            //获取司机
            Driver driver = driverDao.selectById(id);
            //获取承运商
            Carrier carr = carrierDao.getCarrierById(id);
            //审核通过
            if(flag == FlagEnum.AUDIT_PASS.code){
                //保存司机用户到平台，返回用户id
                ResultData<Long> saveRd = saveDriverToPlatform(driver);
                if (!ReturnMsg.SUCCESS.getCode().equals(saveRd.getCode())) {
                    throw new CommonException("司机信息保存失败，原因：" + saveRd.getMsg());
                }
                driver.setUserId(saveRd.getData());
                //driver.setState(VerifyStateEnum.AUDIT_PASS.code);
                driverDao.updateById(driver);
                //更新承运商
                //carr.setState(VerifyStateEnum.AUDIT_PASS.code);
                carrierDao.updateById(carr);
                //更新运力
                VehicleRunning vr = vehicleRunningDao.getVehiRunByDriverId(id);
                vr.setState(VehicleStateEnum.EFFECTIVE.code);
                return vehicleRunningDao.updateById(vr) > 0 ? true : false;
            }else if(flag == FlagEnum.AUDIT_REJECT.code){
                //审核拒绝
               //driver.setState(VerifyStateEnum.AUDIT_REJECT.code);
                driverDao.updateById(driver);
                //更新承运商
                //carr.setState(VerifyStateEnum.AUDIT_REJECT.code);
                return carrierDao.updateById(carr) > 0 ? true : false;
            }else if(flag == FlagEnum.FROZEN.code){
                //冻结
               // driver.setState(VerifyStateEnum.FROZEN.code);
                driverDao.updateById(driver);
                //更新承运商
                //carr.setState(VerifyStateEnum.FROZEN.code);
                return carrierDao.updateById(carr) > 0 ? true:false;
            }
        }catch (Exception e){
            log.info("根据司机userId审核出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public ShowDriverVo getDriverById(Long driverId, Long userId) {
        try{
            ShowDriverVo vo = driverDao.getDriverById(driverId,userId);
            if(vo != null){
                //根据司机id获取该承运商id
                Long carrierId = driverDao.getCarrIdByDriverId(driverId);
                if(carrierId != null){
                    vo.setMapCodes(carrierCityConService.getMapCodes(carrierId));
                }
                return vo;
            }
        }catch (Exception e){
            log.info("根据司机id/userId查看司机信息出现异常");
        }
        return null;
    }

    @Override
    public boolean updateDriver(DriverDto dto) {
        int i;
        int j;
        int m = 0;
        int n = 0;
        Carrier carrier = null;
        try{
            //更新司机信息
            Driver driver = driverDao.selectById(dto.getId());
            //修改司机信息
            ResultData rd = updateUserToPlatform(driver);
            if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
                throw new CommonException("司机信息同步失败，原因：" + rd.getMsg());
            }
            driver.setRealName(dto.getRealName());
            driver.setPhone(dto.getPhone());
            driver.setMode(dto.getMode());
            driver.setIdCardFrontImg(dto.getIdCardFrontImg());
            driver.setIdCardBackImg(dto.getIdCardBackImg());
            driver.setDriverLicenceFrontImg(dto.getDriverLicenceFrontImg());
            driver.setDriverLicenceBackImg(dto.getDriverLicenceBackImg());
            driver.setTravelLicenceFrontImg(dto.getTravelLicenceFrontImg());
            driver.setTravelLicenceBackImg(dto.getTravelLicenceBackImg());
            driver.setTaxiLicenceFrontImg(dto.getTaxiLicenceFrontImg());
            driver.setTaxiLicenceBackImg(dto.getTaxiLicenceBackImg());
            i = driverDao.updateById(driver);
            if(i > 0){
                //更新绑定车辆信息
                DriverVehicleCon dvc = driverVehicleConDao.getDriVehConByDriId(dto.getId().toString());
                dvc.setVehicleId(dto.getVehicleId());
                j = driverVehicleConDao.updateById(dvc);
                if(j > 0){
                    //更新运力池信息
                    VehicleRunning vr = vehicleRunningDao.getVehiRunByDriverId(dto.getId());
                    vr.setVehicleId(dto.getVehicleId());
                    vr.setPlateNo(dto.getPlateNo());
                    vr.setCarryCarNum(dto.getDefaultCarryNum());
                    m =vehicleRunningDao.updateById(vr);
                }
            }
            //更新承运商信息
            if(m > 0){
                carrier = carrierDao.getCarrierById(dto.getId());
                if(carrier != null){
                    carrier.setName(dto.getRealName());
                    carrier.setLinkmanPhone(dto.getPhone());
                    n = carrierDao.updateById(carrier);
                }
            }
            if(n > 0){
                //更新承运商业务范围
                //承运商业务范围,先批量删除，再添加
                carrierCityConService.batchDelete(carrier.getId());
                carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
            }
        }catch (Exception e){
            log.info("根据司机id更新司机信息出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public BusinessCityCode getDriverBusiById(Long id) {
        BusinessCityCode bcc = null;
        try{
            CarrierCityCon ccc = carrierCityConDao.getCarrierCodeByDriverId(id);
            if(ccc != null){
                bcc = carrierCityConService.showCarrCityCon(ccc);
            }
        }catch (Exception e){
            log.info("根据司机id查看承运商业务范围出现异常");
        }
        return bcc;
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


    private List test(){
        //查询个人所有车辆
        List<Vehicle> vehicles = vehicleDao.selectList(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getOwnershipType, ""));
        //查询已经绑定的车辆
        List<DriverVehicleCon> driverVehicleCons = driverVehicleConDao.selectList(new QueryWrapper<DriverVehicleCon>());
        //去除已绑定车辆
        for (DriverVehicleCon driverVehicleCon : driverVehicleCons) {
            for (Vehicle vehicle : vehicles) {
                if(driverVehicleCon.getVehicleId().equals(vehicle.getId())){
                    vehicles.remove(vehicle);
                    break;
                }
            }
        }
        return vehicles;
    }
}
