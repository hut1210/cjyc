package com.cjyc.salesman.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.CityDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.salesman.api.service.ICityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 韵车城市信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-09-30
 */
@Service
public class CityServiceImpl extends ServiceImpl<ICityDao, City> implements ICityService {


    @Autowired
    private StringRedisUtil redisUtil;
    
    @Resource
    private ICityDao cityDao;

    @Override
    public City findById(String cityCode) {

        return cityDao.findById(cityCode);
    }

    @Override
    public PageInfo<City> findPage(CityDto cityDto) {
        PageHelper.startPage(cityDto.getCurrentPage(), cityDto.getPageSize(), true);
        List<City> list = cityDao.findList();
        PageInfo<City> pageInfo = new PageInfo<>(list);
        if(cityDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> countInfo(CityDto cityDto) {

        return cityDao.countInfo(cityDto);
    }

}
