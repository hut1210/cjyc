package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.dto.web.dispatch.ValidateLineDto;
import com.cjyc.common.model.dto.web.dispatch.ValidateLineShellDto;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.ValidateLineVo;
import com.cjyc.common.system.service.ICsLineService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsLineServiceImpl implements ICsLineService {

    @Resource
    private ILineDao lineDao;

    @Override
    public Line getLineByCity(String startCityCode, String endCityCode, boolean isSearchCache) {
        Line line = lineDao.findOneByCity(startCityCode, endCityCode);
        return line;
    }

    @Override
    public ResultVo<Map<String, Object>> linePriceByCode(TransportDto dto) {
        Line line = lineDao.getLinePriceByCode(dto.getFromCode(), dto.getToCode());
        if(line == null){
            return BaseResultUtil.fail("该班线不存在");
        }
        Map<String,Object> map = new HashMap<>(10);
        map.put("defaultWlFee",line.getDefaultWlFee() == null ? BigDecimal.ZERO : line.getDefaultWlFee().divide(new BigDecimal(100)));
        map.put("lineId",line.getId() == null ? "":line.getId());
        return BaseResultUtil.success(map);
    }

    @Override
    public ResultVo<ListVo<ValidateLineVo>> validateLines(ValidateLineShellDto reqDto) {
        List<ValidateLineVo> list = lineDao.validateCarsLine(reqDto.getList());
        if(CollectionUtils.isEmpty(list)){
            list = Lists.newArrayList();
        }
        return BaseResultUtil.success(list, Long.valueOf(list.size()));
    }
}
