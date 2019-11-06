package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.system.service.ICsCityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 城市公用业务
 * @author JPG
 */
@Service
public class CsCityServiceImpl implements ICsCityService {
    @Resource
    private ICityDao cityDao;

    /**
     * 查询全字段城市对象
     *
     * @param areaCode
     * @param cityLevelEnum 根节点
     * @author JPG
     * @since 2019/11/5 9:33
     */
    @Override
    public FullCity findFullCity(String areaCode, CityLevelEnum cityLevelEnum) {
        FullCity fullCity = null;
        if(cityLevelEnum.code == CityLevelEnum.CHINA.code){
            fullCity = cityDao.find5LevelFullCity(areaCode);
        }else if(cityLevelEnum.code == CityLevelEnum.REGION.code){
            fullCity = cityDao.find4LevelFullCity(areaCode);
        }else if(cityLevelEnum.code == CityLevelEnum.CITY.code || cityLevelEnum.code == CityLevelEnum.AREA.code){
            fullCity = cityDao.find2LevelFullCity(areaCode);
        }else{
            fullCity = cityDao.findFullCity(areaCode);
        }
        return fullCity;
    }
}
