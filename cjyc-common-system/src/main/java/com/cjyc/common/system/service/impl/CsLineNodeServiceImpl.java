package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ILineNodeDao;
import com.cjyc.common.model.entity.LineNode;
import com.cjyc.common.system.service.ICsLineNodeService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CsLineNodeServiceImpl implements ICsLineNodeService {

    private static final String SPLIT = "-";

    @Resource
    private ILineNodeDao lineNodeDao;

    @Override
    public List<String> getGuideLine(Set<String> citySet, String startCity) {
        List<String> resList = new ArrayList<>();
        if(CollectionUtils.isEmpty(citySet)){
            resList.add(startCity);
            return resList;
        }
        List<LineNode> list = lineNodeDao.findByCitySet(citySet, startCity);
        if(!CollectionUtils.isEmpty(list)){
            return list.stream().map(LineNode::getNodes).collect(Collectors.toList());
        }else{
            StringBuilder guideline = new StringBuilder();
            guideline.append(startCity);
            if(citySet.size() > 1){
                guideline.append(SPLIT);
                citySet.remove(startCity);
                guideline.append(Joiner.on(SPLIT).join(citySet));
            }
            resList.add(guideline.toString());
        }
        return resList;
    }
}
