package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.config.MiaoxinProperty;
import com.cjyc.common.system.service.ICsCarSeriesService;
import com.cjyc.customer.api.service.ICarSeriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao, CarSeries> implements ICarSeriesService {

    @Resource
    private ICsCarSeriesService csCarSeriesService;

    @Override
    public ResultVo<List<CarSeriesTree>> queryCarSeries(KeywordDto dto) {
        List<CarSeriesTree> carSeriesTrees =  csCarSeriesService.tree(false,dto.getKeyword());
        return BaseResultUtil.success(carSeriesTrees);
    }
}