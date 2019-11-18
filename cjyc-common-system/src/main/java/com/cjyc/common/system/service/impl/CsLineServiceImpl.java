package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.system.service.ICsLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsLineServiceImpl implements ICsLineService {
    @Resource
    private ILineDao lineDao;

    @Override
    public Line getLineByCity(String startCityCode, String endCityCode, boolean isSearchCache) {
        Line line = lineDao.findOneByCity(startCityCode, endCityCode);
        return line;
    }
}
