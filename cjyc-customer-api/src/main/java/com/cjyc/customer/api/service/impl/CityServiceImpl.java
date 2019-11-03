package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.enums.CityLevelEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.CityTreeUtil;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class CityServiceImpl extends ServiceImpl<ICityDao, City> implements ICityService {

    @Resource
    private ICityDao cityDao;

    @Override
    public ResultVo queryCity(KeywordDto dto) {
        List<Object> cityList = new ArrayList<>(15);
        //获取热门城市
        List<Map<String,Object>> hotCity = cityDao.getHotCity();
        List<CityTreeVo> cityTreeVos = null;
        //获取省市code
        if(StringUtils.isNotBlank(dto.getKeyword())){
            //获取关键字所在的省市code
            List<City> cityCodes = cityDao.getCityCodes(dto.getKeyword());
            Set<String> codes = new HashSet<>(15);
            if(!CollectionUtils.isEmpty(cityCodes)){
                for(City city : cityCodes){
                    codes.add(city.getCode());
                    codes.add(city.getParentCode());
                }
            }
            //根据省市code查询集合
            if(!CollectionUtils.isEmpty(codes)){
                cityTreeVos = cityDao.getCityByCodes(codes);
            }
        }else{
            cityTreeVos = cityDao.getAllByLevel(CityLevelEnum.PROVINCE_LEVEL.getLevel(),CityLevelEnum.CITY_LEVEL.getLevel());
        }
        if(!CollectionUtils.isEmpty(cityTreeVos)){
            List<CityTreeVo> citys = CityTreeUtil.encapTree(cityTreeVos);
            cityList.add(hotCity);
            cityList.add(citys);
        }
        return BaseResultUtil.success(cityList == null ? Collections.EMPTY_LIST:cityList);
    }

}