package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.SysEnum;
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
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class DriverServiceImpl implements IDriverService {
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ISalemanDao salemanDao;

    @Resource
    private IDriverVehicleConDao iDriverVehicleConDao;

    @Resource
    private ICityDao iCityDao;

    @Resource
    private ICarrierDao iCarrierDao;

    @Resource
    private IVehicleDao iVehicleDao;

    @Resource
    private IVehicleRunningDao iVehicleRunningDao;

    @Resource
    private ICarrierDriverConDao iCarrierDriverConDao;

    @Resource
    private ICarrierCityConDao iCarrierCityConDao;

    @Resource
    private ICarrierCityConService iCarrierCityConService;

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
            driver.setIdCardFrontImg(dto.getIdCardFrontImg());
            driver.setIdCardBackImg(dto.getIdCardBackImg());
            driver.setDriverLicenceFrontImg(dto.getDriverLicenceFrontImg());
            driver.setDriverLicenceBackImg(dto.getDriverLicenceBackImg());
            driver.setTravelLicenceFrontImg(dto.getTravelLicenceFrontImg());
            driver.setTravelLicenceBackImg(dto.getTravelLicenceBackImg());
            driver.setTaxiLicenceFrontImg(dto.getTaxiLicenceFrontImg());
            driver.setTaxiLicenceBackImg(dto.getTaxiLicenceBackImg());
            i = driverDao.insert(driver);
            if(i > 0){
                //保存司机与车辆关系
                DriverVehicleCon dvc = new DriverVehicleCon();
                dvc.setDriverId(driver.getId());
                dvc.setVehicleId(dto.getVehicleId());
                n = iDriverVehicleConDao.insert(dvc);
                //保存运力信息
                VehicleRunning vr = new VehicleRunning();
                vr.setVehicleId(dto.getVehicleId());
                vr.setDriverId(driver.getId());
                vr.setPlateNo(dto.getPlateNo());
                vr.setCarryCarNum(dto.getDefaultCarryNum());
                vr.setState(Integer.parseInt(SysEnum.ZERO.getValue()));
                vr.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(TimePatternConstant.COMPLEX_TIME_FORMAT),
                        TimePatternConstant.COMPLEX_TIME_FORMAT));
                k = iVehicleRunningDao.insert(vr);
            }
            if(k > 0 && n > 0){
                //保存个人承运商
                carrier = new Carrier();
                carrier.setName(dto.getRealName());
                carrier.setLinkman(dto.getRealName());
                carrier.setLinkmanPhone(dto.getPhone());
                carrier.setMode(dto.getMode());
                carrier.setType(Integer.parseInt(SysEnum.ONE.getValue()));
                carrier.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(TimePatternConstant.COMPLEX_TIME_FORMAT),
                        TimePatternConstant.COMPLEX_TIME_FORMAT));
                carrier.setCreateUserId(dto.getCurrentUserId());
                p = iCarrierDao.insert(carrier);
            }
            if(p > 0){
                //保存司机与承运商关系
                CarrierDriverCon cdc = new CarrierDriverCon();
                cdc.setCarrierId(carrier.getId());
                cdc.setDriverId(driver.getId());
                cdc.setRole(Integer.parseInt(SysEnum.ZERO.getValue()));
                m = iCarrierDriverConDao.insert(cdc);
            }
            if(m > 0){
                CarrierCityCon ccc = iCarrierCityConService.encapCarrCityCon(dto);
                ccc.setCarrierId(carrier.getId());
                return iCarrierCityConDao.insert(ccc) > 0 ? true : false;
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
    public boolean examineDriById(Long id,String sign) {
        try{
            //获取司机
            Driver driver = driverDao.selectById(id);
            //获取承运商
            Carrier carr = iCarrierDao.getCarrierById(id);
            //审核通过
            if(SysEnum.ONE.getValue().equals(sign) || SysEnum.FOUR.getValue().equals(sign)){
                driver.setState(Integer.valueOf(SysEnum.TWO.getValue()));
                driverDao.updateById(driver);
                //更新承运商
                carr.setState(Integer.valueOf(SysEnum.TWO.getValue()));
                iCarrierDao.updateById(carr);
                //更新运力
                VehicleRunning vr = iVehicleRunningDao.getVehiRunByDriverId(id);
                vr.setState(Integer.parseInt(SysEnum.ONE.getValue()));
                return iVehicleRunningDao.updateById(vr) > 0 ? true : false;
            }else if(SysEnum.TWO.getValue().equals(sign)){
                //审核拒绝
                driver.setState(Integer.valueOf(SysEnum.FOUR.getValue()));
                driverDao.updateById(driver);
                //更新承运商
                carr.setState(Integer.valueOf(SysEnum.FOUR.getValue()));
                return iCarrierDao.updateById(carr) > 0 ? true : false;
            }else if(SysEnum.THREE.getValue().equals(sign)){
                //冻结
                driver.setState(Integer.valueOf(SysEnum.SEVEN.getValue()));
                driverDao.updateById(driver);
                //更新承运商
                carr.setState(Integer.valueOf(SysEnum.SEVEN.getValue()));
                return iCarrierDao.updateById(carr) > 0 ? true:false;
            }
        }catch (Exception e){
            log.info("根据司机userId审核出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    @Override
    public ShowDriverVo getDriverById(Long id, Long userId) {
        try{
            ShowDriverVo vo = driverDao.getDriverById(id,userId);
            if(vo != null){
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
                DriverVehicleCon dvc = iDriverVehicleConDao.getDriVehConByDriId(dto.getId().toString());
                dvc.setVehicleId(dto.getVehicleId());
                j = iDriverVehicleConDao.updateById(dvc);
                if(j > 0){
                    //更新运力池信息
                    VehicleRunning vr = iVehicleRunningDao.getVehiRunByDriverId(dto.getId());
                    vr.setVehicleId(dto.getVehicleId());
                    vr.setPlateNo(dto.getPlateNo());
                    vr.setCarryCarNum(dto.getDefaultCarryNum());
                    return iVehicleRunningDao.updateById(vr) > 0 ? true : false;
                }
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
            CarrierCityCon ccc = iCarrierCityConDao.getCarrierCodeByDriverId(id);
            if(ccc != null){
                bcc = iCarrierCityConService.showCarrCityCon(ccc);
            }
        }catch (Exception e){
            log.info("根据司机id查看承运商业务范围出现异常");
        }
        return bcc;
    }


}
