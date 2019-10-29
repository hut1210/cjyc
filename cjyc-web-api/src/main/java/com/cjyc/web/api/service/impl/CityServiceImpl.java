package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.enums.CityLevelEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityTreeVo;
import com.cjyc.common.model.dto.web.city.TreeCityDto;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import com.cjyc.common.model.vo.web.city.CityTreeVo;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResultVo<TreeCityVo> getTree(TreeCityDto paramsDto) {

        getTree(paramsDto.getStartLevel(), paramsDto.getEndLevel());



/*        List<ProvinceCityTreeVo> ptvos = new ArrayList<>();
    public ResultVo provinceCityTree() {
        List<CityTreeVo> ptvos = new ArrayList<>();
        try{
            //查询所有省/直辖市
            List<CityTreeVo> provinceList = cityDao.getAllByLevel(CityLevelEnum.PROVINCE_LEVEL.getLevel());
            if(!provinceList.isEmpty() && !CollectionUtils.isEmpty(provinceList)){
                for(CityTreeVo vo : provinceList){
                    CityTreeVo ptv = new CityTreeVo();
                    ptv.setParentCode(vo.getParentCode());
                    ptv.setCode(vo.getCode());
                    ptv.setName(vo.getName());
                    List<CityTreeVo> cityList = cityDao.getAllCity(vo.getCode());
                    if(!cityList.isEmpty() && CollectionUtils.isEmpty(cityList)){
                        ptv.setCityVos(cityList);
                    }
                    ptvos.add(ptv);
                }
                return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),ptvos);
            }
        }catch (Exception e){
            log.info("查询省市树形结构出现异常");
            throw new CommonException(e.getMessage());
        }
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),ptvos);*/
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
