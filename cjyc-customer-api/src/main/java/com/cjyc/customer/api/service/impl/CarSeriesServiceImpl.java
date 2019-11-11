package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.CarSeries.*;
import com.cjyc.customer.api.service.ICarSeriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class CarSeriesServiceImpl extends ServiceImpl<ICarSeriesDao, CarSeries> implements ICarSeriesService {

    @Resource
    private ICarSeriesDao carSeriesDao;

    @Override
    public ResultVo queryCarSeries(KeywordDto dto) {
        CarVo carVo = new CarVo();
        List<ModelVo> modelVos = new ArrayList<>(15);
        List<CarSeriesVo> carSeriesVos = new ArrayList<>(15);
        Set<String> setInitial = new HashSet<>(15);
        List<CarSeries> carSeries =  carSeriesDao.getCarSeries(dto.getKeyword());
        if(!StringUtils.isEmpty(carSeries)){
            for(CarSeries carSerie : carSeries){
                setInitial.add(carSerie.getPinInitial());
            }
            for(String initial : setInitial){

                for(CarSeries carSerie : carSeries){
                    if(initial.equals(carSerie.getPinInitial())){
                        CarSeriesVo carSeriesVo = new CarSeriesVo();
                        carSeriesVo.setLogoImg(carSerie.getLogoImg());
                        carSeriesVo.setBrand(carSerie.getBrand());

                    }
                }
            }

        }
            return BaseResultUtil.success();
    }

}