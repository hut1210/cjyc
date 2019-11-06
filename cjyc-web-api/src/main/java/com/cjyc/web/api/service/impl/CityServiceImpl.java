package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.dto.web.city.CityQueryDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.CityTreeUtil;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.web.api.service.ICityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        return cityDao.selectById(cityCode);
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
        if (cityPageDto.getCurrentPage() > iPage.getPages()) {
            iPage.setRecords(null);
        }
        return iPage;
    }

    @Override
    public List<City> selectChildList(String code) {
        return cityDao.findChildList(code);
    }

    @Override
    public ResultVo cityTree(Integer rootLevel, Integer minLeafLevel) {
        List<CityTreeVo> cityTreeVos = cityDao.getAllByLevel(rootLevel, minLeafLevel);
        List<CityTreeVo> nodeList = null;
        if (!CollectionUtils.isEmpty(cityTreeVos)) {
            nodeList = CityTreeUtil.encapTree(cityTreeVos);
        }
        return BaseResultUtil.success(nodeList == null ? Collections.EMPTY_LIST : nodeList);
    }

    @Override
    public ResultVo<List<CityTreeVo>> keywordCityTree(String keyword) {
        List<City> cityList = cityDao.getCityTreeByKeyword(keyword);
        Set<String> codeSet = new HashSet<>(16);
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

    @Override
    public ResultVo getCityPage(CityQueryDto dto) {
        BasePageUtil.initPage(dto);
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<FullCity> list = cityDao.selectCityPage();
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }
}

