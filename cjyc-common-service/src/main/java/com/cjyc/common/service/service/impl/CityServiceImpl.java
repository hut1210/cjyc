package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.service.service.ICityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service.impl
 * @date:2019/10/12
 */
@Service("cityService")
public class CityServiceImpl implements ICityService{
    @Autowired
    private ICityDao cityDao;


    /**
     * 根据关键词获取城市信息列表
     * */
    @Override
    public List<City> getCityList(String cityCode, String keyword) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(StringUtils.isBlank(cityCode)){ //查询全国
            queryWrapper.eq("level",0);
        }else{
            queryWrapper.eq("parent_code",cityCode);
        }
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like("name",keyword);
        }
        return cityDao.selectMaps(queryWrapper);
    }
}
