package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesBrand;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsCarSeriesService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public ResultVo<List<CarSeriesTree>> tree(boolean isSearchCache, String keyword) {
        String logoImg = LogoImgProperty.logoImg;
        List<CarSeriesTree> carSeriesTrees = carSeriesDao.findTree(keyword);
        if(!CollectionUtils.isEmpty(carSeriesTrees)){
            for(CarSeriesTree tree : carSeriesTrees){
                List<CarSeriesBrand> brandList = tree.getList();
                if(!CollectionUtils.isEmpty(brandList)){
                    for(CarSeriesBrand brand : brandList){
                        brand.setLogoImg(logoImg+brand.getLogoImg());
                    }
                }
            }
        }
        return BaseResultUtil.success(carSeriesTrees);
    }
}
