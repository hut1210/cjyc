package com.cjyc.common.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.service.service.ICarSeriesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: 车系管理service
 * @date:2019/10/12
 */
public class CarSeriesServiceImpl implements ICarSeriesService{

    @Autowired
    private ICarSeriesDao carSeriesDao;

    /**
     * 查询全部车系信息
     * */
    @Override
    public List<Map<String, Object>> getAllList() {
        return carSeriesDao.selectMaps(null);
    }

    /**
     * 根据关键词查询
     *
     * */
    @Override
    public List<Map<String, Object>> getByKeyword(String keyword) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.like("brand",keyword);
        queryWrapper.like("model",keyword);
        return carSeriesDao.selectMaps(queryWrapper);
    }
}
