package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.CarSeries.CarSeriesVo;
import com.cjyc.common.model.vo.customer.CarSeries.ModelVo;
import com.cjyc.customer.api.service.ICarSeriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class ICarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao, CarSeries> implements ICarSeriesService {

    @Resource
    private ICarSeriesDao carSeriesDao;

    @Override
    public ResultVo queryCarSeries(KeywordDto dto) {
        List<CarSeries> carSeries =  carSeriesDao.getCarSeries(dto.getKeyword());
        List<CarSeriesVo> carSeriesList = new ArrayList<>();
        CarSeriesVo carSeriesVo = null;
        List<ModelVo> modelVos = null;
        ModelVo modelVo = null;
        if(!CollectionUtils.isEmpty(carSeries)){
           Set<String> set = new HashSet<>(10);
           for(CarSeries car : carSeries){
               set.add(car.getBrand());
           }
           if(!CollectionUtils.isEmpty(set)){
               for(String brand : set){
                   carSeriesVo = new CarSeriesVo();
                   carSeriesVo.setBrand(brand);
                   modelVos = new ArrayList<>();
                   for(CarSeries car : carSeries){
                       if(car.getBrand().equals(brand)){
                           carSeriesVo.setLogoImg(car.getLogoImg());
                           modelVo = new ModelVo();
                           modelVo.setModel(car.getModel());
                           modelVos.add(modelVo);
                       }
                   }
                   carSeriesVo.setModelVos(modelVos);
                   carSeriesList.add(carSeriesVo);
               }
           }
       }
        return BaseResultUtil.success(carSeriesList == null ? Collections.EMPTY_LIST:carSeriesList);
    }
}