package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ILineNodeDao;
import com.cjyc.common.model.entity.LineNode;
import com.cjyc.common.system.service.ICsLineNodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class CsLineNodeServiceImpl implements ICsLineNodeService {

    @Resource
    private ILineNodeDao lineNodeDao;

    @Override
    public List<LineNode> getGuideLine(Set<String> citySet, String startCity) {
        StringBuilder guideline = new StringBuilder();
        guideline.append(startCity);
        List<LineNode> list = lineNodeDao.findByCitySet(citySet, startCity);
        return list;
    }
}
