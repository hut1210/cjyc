package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.utils.ExcelUtil;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.dto.web.city.CityQueryDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.CityTreeUtil;
import com.cjyc.common.model.util.JsonUtils;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ExportCityListVo;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.web.api.service.ICityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private RedisUtils redisUtils;

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
        String key = RedisKeys.getCityTreeKey(rootLevel,minLeafLevel);
        String cityTreeStr = redisUtils.hget(key,key);
        List<CityTreeVo> nodeList = JsonUtils.jsonToList(cityTreeStr, CityTreeVo.class);
        if(CollectionUtils.isEmpty(nodeList)){
            List<CityTreeVo> cityTreeVos = cityDao.getAllByLevel(rootLevel, minLeafLevel);
            if (!CollectionUtils.isEmpty(cityTreeVos)) {
                nodeList = CityTreeUtil.encapTree(cityTreeVos);
                redisUtils.hset(key, key, JsonUtils.objectToJson(nodeList));
                redisUtils.expire(key, 1, TimeUnit.DAYS);
            }
        }
        return BaseResultUtil.success(nodeList);
    }

    @Override
    public ResultVo<List<CityTreeVo>> keywordCityTree(String keyword) {
        String key = RedisKeys.getKeywordCityTreeKey(keyword);
        String cityTreeStr = redisUtils.hget(key,key);
        List<CityTreeVo> nodeList = JsonUtils.jsonToList(cityTreeStr, CityTreeVo.class);
        if(CollectionUtils.isEmpty(nodeList)){
            List<City> cityList = cityDao.getCityTreeByKeyword(keyword);
            Set<String> codeSet = new HashSet<>(16);
            List<CityTreeVo> cityTreeVos = null;
            for(City city : cityList){
                codeSet.add(city.getCode());
                codeSet.add(city.getParentCode());
            }
            if(!CollectionUtils.isEmpty(codeSet)){
                cityTreeVos = cityDao.getCityByCodes(codeSet);
                nodeList = CityTreeUtil.encapTree(cityTreeVos);
                redisUtils.hset(key, key, JsonUtils.objectToJson(nodeList));
                redisUtils.expire(key, 1, TimeUnit.DAYS);
            }
        }
        return BaseResultUtil.success(nodeList);
    }

    @Override
    public ResultVo getCityPage(CityQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<FullCity> list = cityDao.selectCityList(dto);
        PageInfo pageInfo = new PageInfo(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<List<City>> getProvinceList(KeywordDto dto) {
        // 000008 未覆盖大区编码,只查询未被覆盖的省
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda().eq(City::getLevel, FieldConstant.PROVINCE_LEVEL)
                .like(!StringUtils.isEmpty(dto.getKeyword()), City::getName, dto.getKeyword())
                .and(i ->i.isNull(City::getParentCode).or().eq(City::getParentCode,""))
                .or(i ->i.eq(City::getParentCode, FieldConstant.NOT_REGION_CODE).like(!StringUtils.isEmpty(dto.getKeyword()), City::getName, dto.getKeyword()));
        List<City> list = super.list(queryWrapper);
        return BaseResultUtil.success(list);
    }

    @Override
    public void exportCityListExcel(HttpServletRequest request, HttpServletResponse response) {
        // 获取参数
        CityQueryDto dto = getCityQueryDto(request);
        List<FullCity> cityList = cityDao.selectCityList(dto);
        List<ExportCityListVo> exportExcelList = Lists.newArrayList();
        for (FullCity vo : cityList) {
            ExportCityListVo exportCityListVo = new ExportCityListVo();
            BeanUtils.copyProperties(vo, exportCityListVo);
            exportExcelList.add(exportCityListVo);
        }
        String title = "城市管理";
        String sheetName = "城市管理";
        String fileName = "城市管理.xls";
        try {
            ExcelUtil.exportExcel(exportExcelList, title, sheetName, ExportCityListVo.class, fileName, response);
        } catch (IOException e) {
            log.error("导出城市管理信息异常:{}",e);
        }

    }

    private CityQueryDto getCityQueryDto(HttpServletRequest request) {
        CityQueryDto dto = new CityQueryDto();
        dto.setRegionCode(request.getParameter("regionCode"));
        dto.setProvinceCode(request.getParameter("provinceCode"));
        dto.setCityCode(request.getParameter("cityCode"));
        dto.setAreaCode(request.getParameter("areaCode"));
        return dto;
    }
}

