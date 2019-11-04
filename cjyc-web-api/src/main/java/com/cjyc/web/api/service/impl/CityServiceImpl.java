package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.dto.web.city.TreeCityDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.CityTreeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import com.cjyc.web.api.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 韵车城市信息表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-09-30
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CityServiceImpl extends ServiceImpl<ICityDao, City> implements ICityService {


    @Autowired
    private StringRedisUtil redisUtil;

    @Resource
    private ICityDao cityDao;

    @Override
    public City findById(String cityCode) {
        return cityDao.findById(cityCode);
    }

    @Override
    public List<City> selectList(Map<String, Object> columnMap) {
        return cityDao.selectByMap(columnMap);
    }

    @Override
    public IPage<City> selectPage(CityPageDto cityPageDto) {
        QueryWrapper<City> queryWarrper = new QueryWrapper<>();
        queryWarrper.eq("level", cityPageDto.getLevel());
        Page<City> page = new Page<>(cityPageDto.getCurrentPage(), cityPageDto.getPageSize());
        IPage<City> iPage = cityDao.selectPage(page, queryWarrper);
        if(cityPageDto.getCurrentPage() > iPage.getPages()){
            iPage.setRecords(null);
        }
        return iPage;
    }

    @Override
    public List<City> selectChildList(String code) {
        return cityDao.findChildList(code);
    }

    @Override
    public ResultVo<List<TreeCityVo>> getTree(TreeCityDto paramsDto) {
        List<TreeCityVo> tree = getTree(paramsDto.getStartLevel(), paramsDto.getEndLevel());
        return BaseResultUtil.success(tree);
    }

    @Override
    public ResultVo cityTree(Integer startLevel,Integer endLevel) {
        List<CityTreeVo> cityTreeVos = cityDao.getAllByLevel(startLevel,endLevel);
        List<CityTreeVo> nodeList = CityTreeUtil.encapTree(cityTreeVos);
        return BaseResultUtil.success(nodeList != null ? nodeList:Collections.emptyList());
    }

    @Override
    public ResultVo<List<CityTreeVo>> getCityTreeByKeyword(String keyword) {
        List<City> cityList = cityDao.getCityTreeByKeyword(keyword);
        Set<String> codeSet = new HashSet<>();
        List<CityTreeVo> cityTreeVos = null;
        List<CityTreeVo> nodeList = null;
        for(City city : cityList){
            codeSet.add(city.getCode());
            codeSet.add(city.getParentCode());
        }
        if(!CollectionUtils.isEmpty(codeSet)){
            cityTreeVos = cityDao.getCityByCodes(codeSet);
            nodeList = CityTreeUtil.encapTree(cityTreeVos);
        }
        return BaseResultUtil.success(nodeList != null ? nodeList:Collections.emptyList());
    }

    private List<TreeCityVo> getTree(int startLevel, int endLevel) {
        if (startLevel <= -1 || startLevel > 5 || startLevel >= endLevel) {
            return null;
        }
        if (endLevel > 5) {
            return null;
        }

        List<TreeCityVo> list = cityDao.findListByLevel(startLevel);
        startLevel += 1;
        for (TreeCityVo treeCityVo : list) {
            if (startLevel >= endLevel) {
                cityDao.findListByLevel(startLevel);
            } else {
                treeCityVo.setNext(getTree(startLevel, endLevel));
            }
        }
        return list;
    }
}
