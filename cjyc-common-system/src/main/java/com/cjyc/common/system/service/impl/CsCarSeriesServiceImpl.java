package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.common.system.service.ICsCarSeriesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 车系公用业务
 * @author JPG
 */
@Service
public class CsCarSeriesServiceImpl implements ICsCarSeriesService {

    @Resource
    private ICarSeriesDao carSeriesDao;
    @Override
    public List<CarSeriesTree> tree(boolean isSearchCache,String keyword) {
        return carSeriesDao.findTree(keyword);
    }
}
