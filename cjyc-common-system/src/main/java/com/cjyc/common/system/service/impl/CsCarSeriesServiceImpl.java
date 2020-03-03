package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICarSeriesDao;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesBrand;
import com.cjyc.common.model.vo.web.carSeries.CarSeriesTree;
import com.cjyc.common.system.config.LogoImgProperty;
import com.cjyc.common.system.service.ICsCarSeriesService;
import com.cjyc.common.system.util.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 车系公用业务
 * @author JPG
 */
@Service
public class CsCarSeriesServiceImpl implements ICsCarSeriesService {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ICarSeriesDao carSeriesDao;


    @Override
    public ResultVo<List<CarSeriesTree>> tree(boolean isSearchCache, String keyword) {
        List<CarSeriesTree> carSeriesTrees = null;
        if(isSearchCache){
            //放入缓存
            String key = RedisKeys.getCarSeriesKey(keyword);
            String carSeriesTreeStr = redisUtils.hget(key,key);
            carSeriesTrees = JsonUtils.jsonToList(carSeriesTreeStr,CarSeriesTree.class);
            if(CollectionUtils.isEmpty(carSeriesTrees)){
                carSeriesTrees = findCarSeriesTree(keyword);
                redisUtils.hset(key, key, JsonUtils.objectToJson(carSeriesTrees));
                redisUtils.expire(key, 1, TimeUnit.DAYS);
            }
        }else{
            carSeriesTrees = findCarSeriesTree(keyword);
        }
        return BaseResultUtil.success(carSeriesTrees);
    }

    /**
     * 获取品牌车系信息
     * @param keyword
     * @return
     */
    private List<CarSeriesTree> findCarSeriesTree(String keyword){
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
        return carSeriesTrees;
    }
}
