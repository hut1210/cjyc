package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.PatternConstant;
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
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.IDriverService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private ICarrierDriverConDao iCarrierDriverConDao;

    @Resource
    private ICarrierCityConDao iCarrierCityConDao;

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
        int p = 0;
        int j = 0;
        Driver driver = null;
        Carrier carrier = null;
        List<String> allAreaCode = new ArrayList<>();
        List<String> largeAreaCode = null;
        List<String> proAreaCode = null;
        List<String> cityAreaCode = null;
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
                dvc.setVehicleId(Long.parseLong(dto.getId()));
                k = iDriverVehicleConDao.insert(dvc);
            }
            if(k > 0){
                //保存个人承运商
                carrier = new Carrier();
                carrier.setName(dto.getRealName());
                carrier.setLinkman(dto.getRealName());
                carrier.setLinkmanPhone(dto.getPhone());
                carrier.setMode(dto.getMode());
                carrier.setType(Integer.parseInt(SysEnum.ONE.getValue()));
                carrier.setCreateTime(LocalDateTimeUtil.convertToLong(LocalDateTimeUtil.formatLDTNow(PatternConstant.COMPLEX_TIME_FORMAT),
                        PatternConstant.COMPLEX_TIME_FORMAT));
                carrier.setCreateUserId(Long.parseLong(dto.getUserId()));
                p = iCarrierDao.insert(carrier);
            }
            if(p > 0){
                //保存司机与承运商关系
                CarrierDriverCon cdc = new CarrierDriverCon();
                cdc.setCarrierId(carrier.getId());
                cdc.setDriverId(driver.getId());
                cdc.setRole(Integer.parseInt(SysEnum.ZERO.getValue()));
                j = iCarrierDriverConDao.insert(cdc);
            }
            if(j > 0){
                //保存业务范围
                CarrierCityCon dcc = new CarrierCityCon();
                dcc.setCarrierId(carrier.getId());
                //全国没有选择
                if(StringUtils.isNotBlank(dto.getCountryCode())){
                    dcc.setCountryCode(dto.getCountryCode());
                    dcc.setAreaCode(SysEnum.ZERO.getValue());
                }else{
                    //大区不为空
                    List<String> largeCodes = dto.getLargeAreaCode();
                    if(largeCodes != null && largeCodes.size() > 0){
                        dcc.setLargeAreaCode(StringUtils.join(largeCodes,","));
                        for(String largeCode : largeCodes){
                            //根据大区code查省/直辖市code
                            List<String> proList = iCityDao.getCodesList(largeCode);
                            if(proList != null && proList.size() > 0){
                                //根据省/直辖市code查区县code
                               largeAreaCode = getAreaCodes(proList);
                            }
                        }
                    }
                    //省市不为空
                    List<String> proCodes = dto.getProvinceCode();
                    if(proCodes != null && proCodes.size() > 0){
                        dcc.setProvinceCode(StringUtils.join(proCodes,","));
                        //根据省/直辖市code查区县code
                        proAreaCode = getAreaCodes(proCodes);
                    }
                    //城市code不为空
                    List<String> cityCodes = dto.getCityCode();
                    if(cityCodes != null && cityCodes.size() > 0){
                        dcc.setCityCode(StringUtils.join(cityCodes,","));
                        //根据城市code查询区县code
                        for(String cityCode : cityCodes){
                            cityAreaCode = iCityDao.getCodesList(cityCode);
                        }
                    }
                    allAreaCode.addAll(largeAreaCode);
                    allAreaCode.addAll(proAreaCode);
                    allAreaCode.addAll(cityAreaCode);
                    allAreaCode.addAll(dto.getAreaCode());
                    dcc.setAreaCode(StringUtils.join(allAreaCode,","));
                }
                return iCarrierCityConDao.insert(dcc) > 0 ? true : false;
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
        int i = 0;
        try{
            //审核通过
            if(SysEnum.ONE.getValue().equals(sign)){
                Driver driver = driverDao.selectById(id);
                driver.setState(Integer.valueOf(SysEnum.TWO.getValue()));
                i = driverDao.updateById(driver);
            }
        }catch (Exception e){
            log.info("根据司机userId审核出现异常");
            throw new CommonException(e.getMessage());
        }
        return false;
    }

    /**
     * 根据省/直辖市code查区县code
     * @param proList
     * @return
     */
    private List getAreaCodes(List<String> proList) {
        List<String> areaList = null;
        for (String code : proList) {
            //根据省/直辖市code查询城市code
            List<String> cityList = iCityDao.getCodesList(code);
            if (cityList != null && cityList.size() > 0) {
                //根据城市code查区县code
                for (String s : cityList) {
                    areaList = iCityDao.getCodesList(s);
                }
            }
        }
        return areaList;
    }
}
