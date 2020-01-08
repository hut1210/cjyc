package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.web.city.RegionAddDto;
import com.cjyc.common.model.dto.web.city.RegionCityDto;
import com.cjyc.common.model.dto.web.city.RegionQueryDto;
import com.cjyc.common.model.dto.web.city.RegionUpdateDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.city.RegionVo;
import com.cjyc.web.api.service.ICityService;
import com.cjyc.web.api.service.IRegionService_1;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description 大区管理业务接口实现
 * @Author LiuXingXiang
 * @Date 2019/11/7 11:46
 **/
@Service
public class RegionServiceImpl_1 implements IRegionService_1 {
    @Resource
    private ICityDao cityDao;
    @Resource
    private ICityService cityService;

    @Override
    public ResultVo getRegionPage(RegionQueryDto dto) {
        // 分页查询大区信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.REGION_LEVEL)
                .ne(City::getCode,FieldConstant.NOT_REGION_CODE)
                .like(!StringUtils.isEmpty(dto.getRegionName()), City::getName, dto.getRegionName());
        List<City> regionList = cityDao.selectList(queryWrapper);
        PageInfo<City> pageInfo = new PageInfo(regionList);
        // 根据大区编码查询覆盖省
        getProvince(pageInfo);
        return BaseResultUtil.success(pageInfo);
    }

    private void getProvince(PageInfo<City> pageInfo) {
        List<City> list = new ArrayList<>(10);
        List<City> pageInfoList = pageInfo.getList();
        if (!CollectionUtils.isEmpty(pageInfoList)) {
            for (City region : pageInfoList) {
                RegionVo vo = new RegionVo();
                BeanUtils.copyProperties(region,vo);
                List<City> provinceList = cityDao.selectList(new QueryWrapper<City>().lambda().eq(City::getParentCode, region.getCode()));
                if (!CollectionUtils.isEmpty(provinceList)) {
                    vo.setProvinceCount(provinceList.size());
                    List<ProvinceCityVo> provinceCityList = new ArrayList<>(10);
                    ProvinceCityVo provinceCityVo = null;
                    for (City province : provinceList) {
                        provinceCityVo= new ProvinceCityVo();
                        provinceCityVo.setProvinceCode(province.getCode());
                        provinceCityVo.setProvinceName(province.getName());
                        provinceCityList.add(provinceCityVo);
                    }
                    vo.setProvinceList(provinceCityList);
                }

                list.add(vo);
            }
        }
        pageInfo.setList(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo addRegion(RegionAddDto dto) {
        // 验证大区是否存在
        City region = cityService.getOne(new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.REGION_LEVEL).eq(City::getName, dto.getRegionName()));
        if(region != null) {
            return BaseResultUtil.getVo(ResultEnum.EXIST_REGION.getCode(), ResultEnum.EXIST_REGION.getMsg());
        }

        // 查询所有大区编码
        List<City> regionList = cityService.list(new QueryWrapper<City>().lambda().eq(City::getLevel, FieldConstant.REGION_LEVEL));

        // 根据编码规则生成新增的大区编码
        String regionCode = getRegionCode(regionList);

        // 在城市表中新增大区信息和给新增大区添加覆盖省份
        this.addRegionAndUpdateCity(regionCode,dto);
        return BaseResultUtil.success();
    }

    private void addRegionAndUpdateCity(String regionCode,RegionAddDto dto) {
        // 保存大区信息
        City region = new City();
        region.setCode(regionCode);
        region.setName(dto.getRegionName());
        region.setLevel(FieldConstant.REGION_LEVEL);
        region.setParentCode(FieldConstant.CHINA_CODE);
        region.setParentName(FieldConstant.CHINA_NAME);
        region.setRemark(dto.getRemark());
        cityService.save(region);

        // 更新省份的上级大区
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList)) {
            for (RegionCityDto province : provinceList) {
                LambdaUpdateWrapper<City> updateWrapper = new UpdateWrapper<City>().lambda()
                        .set(City::getParentCode,regionCode).set(City::getParentName,dto.getRegionName())
                        .eq(City::getCode,province.getCode()).eq(City::getLevel,FieldConstant.PROVINCE_LEVEL);
                boolean res = cityService.update(updateWrapper);
                if (!res) {
                    throw new RuntimeException("更新省份大区编码异常");
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo modifyRegion(RegionUpdateDto dto) {
        // 获取被删除省编码，获取新增的省编码
        List<City> deleteProvinceList = this.filterProvince(dto);
        // 给大区新增覆盖省
        this.updateNewProvinceList(dto);
        // 将被删除的覆盖省挂在未覆盖大区下
        this.updateOldProvinceList(deleteProvinceList);
        // 修改大区信息
        boolean update = cityService.update(new UpdateWrapper<City>().lambda()
                .set(City::getRemark, dto.getRemark())
                .set(City::getName,dto.getRegionName())
                .eq(City::getCode, dto.getRegionCode()));
        if (!update) {
            throw new RuntimeException("更新大区信息异常");
        }
        return BaseResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo removeRegion(String regionCode) {
        // 查询当前大区下的省份
        List<City> list = cityService.list(new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getParentCode, regionCode));

        // 更新城市列表中省份的上级机构（大区）
        if (!CollectionUtils.isEmpty(list)) {
            this.removeYcProvince(list);
        }

        // 删除韵车大区信息
        boolean remove = cityService.remove(new QueryWrapper<City>().lambda().eq(City::getCode, regionCode));
        if (!remove) {
            throw new RuntimeException("删除大区失败");
        }

        return BaseResultUtil.success();
    }

    private void removeYcProvince(List<City> list) {
        for (City city : list) {
            boolean update = cityService.update(new UpdateWrapper<City>().lambda()
                    .set(City::getParentCode, FieldConstant.NOT_REGION_CODE)
                    .set(City::getParentName, FieldConstant.NOT_REGION_NAME)
                    .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getCode, city.getCode()));
            if (!update) {
                throw new RuntimeException("修改省份大区编码失败");
            }
        }
    }

    private void updateNewProvinceList(RegionUpdateDto dto) {
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList)) {
            for (RegionCityDto province : provinceList) {
                // 更新大区覆盖省
                updateProvinceParentCity(dto, province);
            }
        }
    }

    private void updateProvinceParentCity(RegionUpdateDto dto, RegionCityDto province) {
        LambdaUpdateWrapper<City> updateProvince = new UpdateWrapper<City>().lambda()
                .set(City::getParentCode,dto.getRegionCode())
                .set(City::getParentName,dto.getRegionName())
                .eq(City::getCode,province.getCode())
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL);
        boolean updateRes = cityService.update(updateProvince);
        if (!updateRes) {
            throw new RuntimeException("更新大区覆盖省失败");
        }
    }

    private List<City> filterProvince(RegionUpdateDto dto) {
        // 查询当前大区已覆盖省
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getParentCode,dto.getRegionCode());
        List<City> oldCityList = cityDao.selectList(queryWrapper);

        // 筛选出新增的省和被删除的省
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList) && !CollectionUtils.isEmpty(oldCityList)) {
            CopyOnWriteArrayList<City> cowCityList = new CopyOnWriteArrayList<>(oldCityList);
            CopyOnWriteArrayList<RegionCityDto> cowRegionList = new CopyOnWriteArrayList<>(provinceList);
            for (RegionCityDto newCity : cowRegionList) {
                for (City oldCity : cowCityList) {
                    if (newCity.getCode().equals(oldCity.getCode())) {
                        // 筛选出被删除的省
                        cowCityList.remove(oldCity);
                        // 筛选出新增的省
                        cowRegionList.remove(newCity);
                    }
                }
            }
            // 被删除的省
            oldCityList = cowCityList;
            // 新增的省
            dto.setProvinceList(cowRegionList);
        }
        return oldCityList;
    }

    private void updateOldProvinceList(List<City> oldCityList) {
        if (!CollectionUtils.isEmpty(oldCityList)) {
            for (City oldCity : oldCityList) {
                // 更新省份上一级编码为未覆盖大区编码
                LambdaUpdateWrapper<City> updateWrapper = new UpdateWrapper<City>().lambda()
                        .set(City::getParentCode, FieldConstant.NOT_REGION_CODE)
                        .set(City::getParentName,FieldConstant.NOT_REGION_NAME)
                        .eq(City::getCode,oldCity.getCode())
                        .eq(City::getLevel,FieldConstant.PROVINCE_LEVEL);
                boolean result = cityService.update(updateWrapper);
                if (!result) {
                    throw new RuntimeException("更新大区覆盖省失败");
                }
            }
        }
    }

    /**
     * 功能描述: 根据表中已有的大区编码生成新的大区编码，编码格式 0*****
     *          如果遇到000008编码时，跳过该编码
     *          000008为固定的未覆盖大区编码
     * @author liuxingxiang
     * @date 2019/11/25
     * @param regionList
     * @return java.lang.String
     */
    private String getRegionCode(List<City> regionList) {
        StringBuilder regionCode = new StringBuilder();
        if (!CollectionUtils.isEmpty(regionList)) {
            City city0 = regionList.get(0);
            int codeLength = city0.getCode().length();
            List<Integer> list = new ArrayList<>(10);
            for (City city : regionList) {
                String code = city.getCode();
                list.add(Integer.valueOf(code));
            }
            Collections.sort(list);
            Integer value = list.get(list.size() - 1) + 1;
            String str = String.valueOf(value == 8 ? value + 1 : value);
            for(int i = 1;i < codeLength;i++) {
                regionCode.append(NoConstant.REGION_CODE_PREFIX);
                if (i == codeLength - str.length()) {
                    regionCode.append(str);
                    break;
                }
            }
            if (regionCode.length() != codeLength) {
                regionCode.append(NoConstant.REGION_CODE_START);
            }
        } else {
            regionCode.append(NoConstant.REGION_CODE_START);
        }
        return regionCode.toString();
    }

}
