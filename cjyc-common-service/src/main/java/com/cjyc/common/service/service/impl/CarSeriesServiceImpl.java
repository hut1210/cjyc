package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.service.service.ICarSeriesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther litan
 * @description: 车系管理service
 * @date:2019/10/12
 */
@Service("carSeriesService")
public class CarSeriesServiceImpl implements ICarSeriesService{

    @Resource
    private ICarSeriesDao carSeriesDao;

    /**
     * 根据关键词查询列表
     * 关键词为空查询所有
     *
     * */
    @Override
    public List<CarSeries> getList(String keyword) {
        QueryWrapper<CarSeries> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(keyword)) {
            queryWrapper.like("brand",keyword);
            queryWrapper.or();
            queryWrapper.like("model",keyword);
        }
        return carSeriesDao.selectList(queryWrapper);
    }

    @Override
    public List<String> getBrand() {
        return carSeriesDao.getBrand();
    }

    @Override
    public List<String> getSeriesByBrand(String brand) {
        return carSeriesDao.getSeriesByBrand(brand);
    }

}
