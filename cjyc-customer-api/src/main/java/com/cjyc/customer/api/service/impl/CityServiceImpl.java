package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.CityTreeUtil;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.model.vo.customer.city.HotCityVo;
import com.cjyc.common.model.vo.customer.city.ProvinceTreeVo;
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
        CityVo cityvo = new CityVo();
        //获取热门城市
        List<HotCityVo> hotCity = cityDao.getHotCity();
        List<ProvinceTreeVo> cityTreeVos = cityDao.findThreeCity(dto.getKeyword());
        if(!CollectionUtils.isEmpty(cityTreeVos) && !CollectionUtils.isEmpty(hotCity)){
            cityvo.setHotCityVos(hotCity);
            cityvo.setCityTreeVos(cityTreeVos);
            return BaseResultUtil.success(cityvo);
        }
        return BaseResultUtil.fail("数据有误，请联系管理员");
    }
}