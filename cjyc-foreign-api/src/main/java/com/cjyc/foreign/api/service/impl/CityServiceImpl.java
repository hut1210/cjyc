package com.cjyc.foreign.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.constant.CommonConstant;
import com.cjyc.foreign.api.dao.ICityDao;
import com.cjyc.foreign.api.dto.res.CityResDto;
import com.cjyc.foreign.api.entity.City;
import com.cjyc.foreign.api.service.ICityService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 城市接口实现类
 * @Author Liu Xing Xiang
 * @Date 2020/3/10 14:53
 **/
@Service
public class CityServiceImpl extends ServiceImpl<ICityDao, City> implements ICityService {
    @Resource
    private ICityDao cityDao;

    @Override
    public ResultVo<List<CityResDto>> getAllCity() {
        List<City> cityList = cityDao.selectList(new QueryWrapper<City>().lambda().eq(City::getLevel, CommonConstant.CITY_LEVEL_TWO));
        List<CityResDto> cityResList = getCityResList(cityList);
        return BaseResultUtil.success(cityResList);
    }

    private List<CityResDto> getCityResList(List<City> cityList) {
        List<CityResDto> cityResList = new ArrayList<>(100);
        if (!CollectionUtils.isEmpty(cityList)) {
            CityResDto cityResDto = null;
            for (City city : cityList) {
                cityResDto = new CityResDto();
                cityResDto.setCityCode(city.getCode());
                cityResDto.setCityName(city.getName());
                cityResList.add(cityResDto);
            }
        }
        return cityResList;
    }
}
