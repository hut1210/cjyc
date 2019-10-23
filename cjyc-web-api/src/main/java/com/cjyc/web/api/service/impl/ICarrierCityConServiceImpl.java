package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarrierCityConDao;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.BaseCityDto;
import com.cjyc.common.model.entity.BusinessCityCode;
import com.cjyc.common.model.entity.CarrierCityCon;
import com.cjyc.common.model.enums.SysEnum;
import com.cjyc.web.api.service.ICarrierCityConService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class ICarrierCityConServiceImpl extends ServiceImpl<ICarrierCityConDao, CarrierCityCon> implements ICarrierCityConService {

    @Resource
    private ICityDao iCityDao;

    @Resource
    private ICarrierCityConDao iCarrierCityConDao;

    /**
     * 建立承运商的业务范围
     * @return
     */
    @Override
    public CarrierCityCon encapCarrCityCon(BusinessCityCode bccd) {
        //保存司机(个人承运商)业务范围
        CarrierCityCon ccc = new CarrierCityCon();
        List<BaseCityDto> countryList = bccd.getCountryList();
        //选择全国
        if(countryList != null && countryList.size() > 0){
            for(BaseCityDto city : countryList){
                ccc.setCountry(city.getCode());
                ccc.setAreaCode(SysEnum.ZERO.getValue());
            }
        }else {
            List<String> allList = new ArrayList<>();
            //处理大区
            List<String> largeCodes = new ArrayList<>();
            List<String> larAreaCodes = null;
            List<BaseCityDto> largeAreaList = bccd.getLargeAreaList();
            if(largeAreaList != null && largeAreaList.size() > 0){
                for(BaseCityDto baseCityDto : largeAreaList){
                    largeCodes.add(baseCityDto.getCode());
                }
                //根据大区codes获取区县codes
                larAreaCodes = iCityDao.getAreaCodesByLarCode(largeCodes);
                ccc.setLargeArea(StringUtils.join(larAreaCodes,","));
            }
            //处理省市
            List<String> provinCodes = new ArrayList<>();
            List<String> provinAreaCodes = null;
            List<BaseCityDto> proviAreaList = bccd.getProvinceList();
            if(proviAreaList != null && proviAreaList.size() > 0){
                for(BaseCityDto baseCityDto : proviAreaList){
                    provinCodes.add(baseCityDto.getCode());
                }
                //根据省市codes获取区县codes
                provinAreaCodes = iCityDao.getAreaCodesByProCode(provinCodes);
                ccc.setLargeArea(StringUtils.join(provinAreaCodes,","));
            }
            //处理城市
            List<String> cityCodes = new ArrayList<>();
            List<String> cityAreaCodes = null;
            List<BaseCityDto> cityAreaList = bccd.getCityList();
            if(cityAreaList != null && cityAreaList.size() > 0){
                for(BaseCityDto baseCityDto : cityAreaList){
                    cityCodes.add(baseCityDto.getCode());
                }
                //根据城市codes获取区县codes
                cityAreaCodes = iCityDao.getAreaCodesByCityCode(cityCodes);
                ccc.setLargeArea(StringUtils.join(cityAreaCodes,","));
            }
            //处理区县
            List<String> areaCodes = new ArrayList<>();
            List<BaseCityDto> areaList = bccd.getAreaList();
            if(areaList != null && areaList.size() > 0){
                for(BaseCityDto baseCityDto : areaList){
                    areaCodes.add(baseCityDto.getCode());
                }
                ccc.setArea(StringUtils.join(areaCodes,","));
            }
            allList.addAll(larAreaCodes);
            allList.addAll(provinAreaCodes);
            allList.addAll(cityAreaCodes);
            allList.addAll(areaCodes);
            ccc.setAreaCode(StringUtils.join(allList,","));
        }
        return ccc;
    }

    @Override
    public BusinessCityCode showCarrCityCon(CarrierCityCon ccc) {
        BusinessCityCode bcc = new BusinessCityCode();
        List<BaseCityDto> countryList = null;
        List<BaseCityDto> largeAreaList = null;
        List<BaseCityDto> provinceList = null;
        List<BaseCityDto> cityList = null;
        List<BaseCityDto> areaList = null;
        //全国不为空
        if(SysEnum.ZERO.getValue().equals(ccc.getAreaCode())){
            List<String> countryCodes = Arrays.asList(ccc.getCountry().split(","));
            countryList = iCityDao.getCityAndName(countryCodes);
        }else{
            //大区
            if(ccc.getLargeArea() != null){
                //字符串转集合
                List<String> largeAreaCodes = Arrays.asList(ccc.getLargeArea().split(","));
                largeAreaList = iCityDao.getCityAndName(largeAreaCodes);
            }
            //省/直辖市
            if(ccc.getProvince() != null){
                //字符串转集合
                List<String> provinCodes = Arrays.asList(ccc.getProvince().split(","));
                provinceList = iCityDao.getCityAndName(provinCodes);
            }
            //城市
            if(ccc.getCity() != null){
                //字符串转集合
                List<String> cityCodes = Arrays.asList(ccc.getCity().split(","));
                cityList = iCityDao.getCityAndName(cityCodes);
            }
            //区县
            if(ccc.getArea() != null){
                //字符串转集合
                List<String> areaCodes = Arrays.asList(ccc.getArea().split(","));
                areaList = iCityDao.getCityAndName(areaCodes);
            }
        }
        bcc.setCountryList(countryList);
        bcc.setLargeAreaList(largeAreaList);
        bcc.setProvinceList(provinceList);
        bcc.setCityList(cityList);
        bcc.setAreaList(areaList);
        return bcc;
    }
}