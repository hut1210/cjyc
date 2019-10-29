package com.cjyc.web.api.service.impl;

        import com.cjyc.common.model.constant.TimePatternConstant;
        import com.cjyc.common.model.dao.*;
        import com.cjyc.common.model.dto.web.driver.DriverDto;
        import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
        import com.cjyc.common.model.dto.web.user.DriverListDto;
        import com.cjyc.common.model.entity.*;
        import com.cjyc.common.model.enums.FlagEnum;
        import com.cjyc.common.model.enums.PayModeEnum;
        import com.cjyc.common.model.enums.transport.*;
        import com.cjyc.common.model.util.LocalDateTimeUtil;
        import com.cjyc.common.model.vo.PageVo;
        import com.cjyc.common.model.vo.ResultVo;
        import com.cjyc.common.model.vo.web.driver.DriverVo;
        import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
        import com.cjyc.common.model.vo.web.user.DriverListVo;
        import com.cjyc.web.api.exception.CommonException;
        import com.cjyc.web.api.service.ICarrierCityConService;
        import com.cjyc.web.api.service.IDriverService;
        import com.github.pagehelper.PageHelper;
        import com.github.pagehelper.PageInfo;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Propagation;
        import org.springframework.transaction.annotation.Transactional;

        import javax.annotation.Resource;
        import java.time.LocalDateTime;
        import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class DriverServiceImpl implements IDriverService {
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
        int i ;
        int k = 0;
        int n = 0;
        int p = 0;
        int m = 0;
        Driver driver = null;
        Carrier carrier = null;
        try{
            //保存散户司机
            driver = new Driver();
            driver.setRealName(dto.getRealName());
            driver.setPhone(dto.getPhone());
            driver.setMode(dto.getMode());
            driver.setIdentity(DriverIdentityEnum.COMMON_DRIVER.code);
            driver.setState(VerifyStateEnum.BE_AUDITED.code);
            driver.setBusinessState(BusinessStateEnum.OUTAGE.code);
            driver.setSource(DriverSourceEnum.SALEMAN_WEB.code);
            driver.setIdCardFrontImg(dto.getIdCardFrontImg());
            driver.setIdCardBackImg(dto.getIdCardBackImg());
            driver.setDriverLicenceFrontImg(dto.getDriverLicenceFrontImg());
            driver.setDriverLicenceBackImg(dto.getDriverLicenceBackImg());
            driver.setTravelLicenceFrontImg(dto.getTravelLicenceFrontImg());
            driver.setTravelLicenceBackImg(dto.getTravelLicenceBackImg());
            driver.setTaxiLicenceFrontImg(dto.getTaxiLicenceFrontImg());
            driver.setTaxiLicenceBackImg(dto.getTaxiLicenceBackImg());
            driver.setCreateUserId(dto.getUserId());
            i = driverDao.insert(driver);
            if(i > 0){
                //保存司机与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setDriverId(driver.getId());
                dvc.setVehicleId(dto.getVehicleId());
                n = driverVehicleConDao.insert(dvc);
                //保存运力信息
                VehicleRunning vr = new VehicleRunning();
                vr.setVehicleId(dto.getVehicleId());
                vr.setDriverId(driver.getId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(VehicleStateEnum.INVALID.code);
                vr.setRunningState(BusinessStateEnum.OUTAGE.code);
                vr.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                k = vehicleRunningDao.insert(vr);
            }
            if(k > 0 && n > 0){
                //保存个人承运商
                carrier = new Carrier();
                carrier.setName(dto.getRealName());
                carrier.setLinkman(dto.getRealName());
                carrier.setLinkmanPhone(dto.getPhone());
                carrier.setType(CarrierTypeEnum.PERSONAL.code);
                carrier.setSettleType(PayModeEnum.CURRENT.code);
                carrier.setCreateTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                carrier.setCreateUserId(dto.getUserId());
                p = carrierDao.insert(carrier);
            }
            if(p > 0){
                //保存司机与承运商关系
                CarrierDriverCon cdc = new CarrierDriverCon();
                cdc.setCarrierId(carrier.getId());
                cdc.setDriverId(driver.getId());
                cdc.setRole(DriverIdentityEnum.COMMON_DRIVER.code);
                m = carrierDriverConDao.insert(cdc);
            }
            if(m > 0){
                //添加承运商业务范围
                return carrierCityConService.batchSave(carrier.getId(),dto.getCodes());
            }
        }catch (Exception e){
            log.info("新增散户司机出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
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
                driver.setState(VerifyStateEnum.AUDIT_PASS.code);
                driverDao.updateById(driver);
                //更新承运商
                carr.setState(VerifyStateEnum.AUDIT_PASS.code);
                carrierDao.updateById(carr);
                //更新运力
                VehicleRunning vr = vehicleRunningDao.getVehiRunByDriverId(id);
                vr.setState(VehicleStateEnum.EFFECTIVE.code);
                return vehicleRunningDao.updateById(vr) > 0 ? true : false;
            }else if(flag == FlagEnum.AUDIT_REJECT.code){
                //审核拒绝
                driver.setState(VerifyStateEnum.AUDIT_REJECT.code);
                driverDao.updateById(driver);
                //更新承运商
                carr.setState(VerifyStateEnum.AUDIT_REJECT.code);
                return carrierDao.updateById(carr) > 0 ? true : false;
            }else if(flag == FlagEnum.FROZEN.code){
                //冻结
                driver.setState(VerifyStateEnum.FROZEN.code);
                driverDao.updateById(driver);
                //更新承运商
                carr.setState(VerifyStateEnum.FROZEN.code);
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

}
