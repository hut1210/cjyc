package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.vo.CityVo;
import com.cjyc.common.service.service.ICityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service.impl
 * @date:2019/10/12
 */
@Service("cityService")
public class CityServiceImpl implements ICityService{
    @Resource
    private ICityDao cityDao;


    /**
     * 根据关键词获取城市信息列表
     * */
    @Override
    public List<CityVo> getCityList(String cityCode, String keyword) {
        List<CityVo> voList = new ArrayList<>();
        CityVo cityVo = new CityVo();

        QueryWrapper queryWrapper = new QueryWrapper<>();
        if(StringUtils.isBlank(cityCode)){ //查询全国
            queryWrapper.eq("level",0);
        }else{
            queryWrapper.eq("parent_code",cityCode);
        }
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like("name",keyword);
        }
        List<City> cityList = cityDao.selectMaps(queryWrapper);

        QueryWrapper queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("hot",1);
        List<City> hotCityList = cityDao.selectMaps(queryWrapper2);

        cityVo.setCityList(cityList);
        cityVo.setHotCityList(hotCityList);
        voList.add(cityVo);
        return voList;
    }

    @Override
    public List<Map<String, Object>> getList(String cityCode) {
        return cityDao.getList(cityCode);
    }

    @Override
    public List<Map<String,Object>> getWebCityList(String cityCode) {
        List<Map<String,Object>> list ;
        list = cityDao.getList(cityCode);
        // todo 添加业务中心数据到集合
        return list;
    }

}
