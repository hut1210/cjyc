package com.cjyc.foreign.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.City;
import com.cjyc.foreign.api.service.IBaseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zcm
 * @date 2020/3/17 14:57
 */
@Service
public class BaseDataServiceImpl implements IBaseDataService {
    @Resource
    private ICityDao cityDao;
    @Resource
    private ICarSeriesDao carSeriesDao;
    @Override
    public List<City> getAllCity() {
        return cityDao.selectList(new QueryWrapper<City>());
    }

    @Override
    public List<CarSeries> getAllCarSeries() {
        return carSeriesDao.selectList(new QueryWrapper<CarSeries>());
    }
}
