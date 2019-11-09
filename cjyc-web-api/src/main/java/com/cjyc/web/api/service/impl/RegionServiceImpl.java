package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.AddDeptReq;
import com.cjkj.usercenter.dto.common.AddDeptResp;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.UpdateDeptReq;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.web.city.RegionAddDto;
import com.cjyc.common.model.dto.web.city.RegionQueryDto;
import com.cjyc.common.model.dto.web.city.RegionUpdateDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.RegionVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.web.api.service.ICityService;
import com.cjyc.web.api.service.IRegionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description 大区管理业务接口实现
 * @Author LiuXingXiang
 * @Date 2019/11/7 11:46
 **/
@Service
public class RegionServiceImpl implements IRegionService {
    @Resource
    private ICityDao cityDao;
    @Resource
    private ICityService cityService;
    @Resource
    private ISysDeptService sysDeptService;

    @Override
    public ResultVo getRegionPage(RegionQueryDto dto) {
        // 分页查询大区信息
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.REGION_LEVEL).like(!StringUtils.isEmpty(dto.getRegionName()), City::getName, dto.getRegionName());
        List<City> cityList = cityDao.selectList(queryWrapper);
        // 根据大区编码查询覆盖省数量
        List<RegionVo> list = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(cityList)) {
            LambdaQueryWrapper<City> wrapper = new QueryWrapper<City>().lambda();
            for (City city : cityList) {
                wrapper = wrapper.eq(City::getParentCode, city.getCode());
                RegionVo vo = new RegionVo();
                vo.setRegionCode(city.getCode());
                vo.setRegionName(city.getName());
                Integer count = cityDao.selectCount(wrapper);
                vo.setProvinceCount(count);
                list.add(vo);
            }
        }
        return BaseResultUtil.success(new PageInfo(list));
    }

    @Transactional
    @Override
    public ResultVo addRegion(RegionAddDto dto) throws Exception {
        // 查询大区编码
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda().eq(City::getLevel, FieldConstant.REGION_LEVEL);
        List<City> cityList = cityService.list(queryWrapper);

        // 根据编码规则生成新增的大区编码
        String code = getRegionCode(cityList);

        // 保存大区信息
        City region = new City();
        region.setCode(code);
        region.setName(dto.getRegionName());
        region.setLevel(FieldConstant.REGION_LEVEL);
        region.setParentCode(FieldConstant.CHINA_CODE);
        region.setParentName(FieldConstant.CHINA_NAME);
        cityService.save(region);

        // 给新增大区添加覆盖省份
        List<String> provinceCodeList = dto.getProvinceCodeList();
        if (!CollectionUtils.isEmpty(provinceCodeList)) {
            City province = new City();
            province.setParentCode(region.getCode());
            province.setParentName(region.getName());
            LambdaUpdateWrapper<City> updateWrapper = new UpdateWrapper<City>().lambda();
            for (String provinceCode : provinceCodeList) {
                updateWrapper = updateWrapper.eq(City::getCode,provinceCode);
                boolean res = cityService.update(province, updateWrapper);
                if (!res) {
                    throw new Exception("给新增大区添加覆盖省份异常");
                }
            }
        }

        // 调用架构中心部门新增接口，新增大区（部门）
        AddDeptReq addDeptReq = new AddDeptReq();
        addDeptReq.setName(dto.getRegionName());
        addDeptReq.setParentId(Long.parseLong(YmlProperty.get("cjkj.dept_admin_id")));
        ResultData<AddDeptResp> result = sysDeptService.save(addDeptReq);
        if (result != null && String.valueOf(HttpStatus.SC_OK).equals(result.getCode())) {
            if (result.getData() != null) {
                if (result.getData().getDeptId() == null) {
                    throw new Exception("调用物流平台保存大区失败");
                }
            } else {
                throw new Exception("调用物流平台保存大区失败");
            }
        } else {
            throw new Exception("调用物流平台保存机构信息异常");
        }

        // 调用架构中心接口,给新增的大区添加省份
        if (!CollectionUtils.isEmpty(provinceCodeList)) {
            UpdateDeptReq updateDeptReq = new UpdateDeptReq();
            for (String provinceCode : provinceCodeList) {
                updateDeptReq.setDeptId(Long.parseLong(provinceCode));
                updateDeptReq.setParentId(result.getData().getDeptId());
                ResultData resultData = sysDeptService.update(updateDeptReq);
                if (resultData == null || !String.valueOf(HttpStatus.SC_OK).equals(resultData.getCode())) {
                    throw new Exception("调用物流平台修改机构信息异常");
                }
            }
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo modifyRegion(RegionUpdateDto dto) throws Exception {
        // 查询当前大区已覆盖省
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getParentCode,dto.getRegionCode());
        List<City> oldCityList = cityDao.selectList(queryWrapper);
        List<String> provinceCodeList = dto.getProvinceCodeList();
        String code = null;
        if (!CollectionUtils.isEmpty(provinceCodeList) && !CollectionUtils.isEmpty(oldCityList)) {
            code = oldCityList.get(0).getCode();
            for (String newCode : provinceCodeList) {
                for (City city : oldCityList) {
                    if (newCode.equals(city.getCode())) {
                        oldCityList.remove(city);
                        provinceCodeList.remove(newCode);
                    }
                }
            }
        }

        // 将被删除的覆盖省挂在未覆盖大区下
        if (!CollectionUtils.isEmpty(oldCityList)) {
            LambdaUpdateWrapper<City> updateWrapper = new UpdateWrapper<City>().lambda()
                    .set(City::getParentCode,FieldConstant.NOT_REGION_CODE).set(City::getParentName,FieldConstant.NOT_REGION_NAME);
            for (City oldCity : oldCityList) {
                // 更新大区覆盖省
                updateWrapper = updateWrapper.eq(City::getCode,oldCity.getCode()).eq(City::getLevel,FieldConstant.PROVINCE_LEVEL);
                boolean result = cityService.update(updateWrapper);
                if (!result) {
                    throw new Exception("更新大区覆盖省失败");
                }

                // 调用物流平台接口更新机构信息
                UpdateDeptReq updateDeptReq = new UpdateDeptReq();
                updateDeptReq.setDeptId(Long.parseLong(oldCity.getCode()));
                updateDeptReq.setParentId(Long.parseLong(YmlProperty.get("cjkj.dept_admin_id")));
                ResultData resultData = sysDeptService.update(updateDeptReq);
                if (resultData == null || !String.valueOf(HttpStatus.SC_OK).equals(resultData.getCode())) {
                    throw new Exception("调用物流平台修改机构信息异常");
                }
            }
        }

        // 新增覆盖省
        LambdaUpdateWrapper<City> updateProvince = new UpdateWrapper<City>().lambda()
                .set(City::getParentCode,dto.getRegionCode()).set(City::getParentName,dto.getRegionName());
        if (CollectionUtils.isEmpty(provinceCodeList)) {
            Long parentId = null;
            if (code != null) {
                ResultData<SelectDeptResp> result = sysDeptService.getById(Long.parseLong(code));
                if (result != null && String.valueOf(HttpStatus.SC_OK).equals(result.getCode())) {
                    SelectDeptResp data = result.getData();
                    if (data != null) {
                        parentId = data.getParentId();
                    } else {
                        throw new Exception("调用物流平台查询机构信息异常");
                    }
                } else {
                    throw new Exception("调用物流平台查询机构信息异常");
                }
            }
            for (String provinceCode : provinceCodeList) {
                // 更新大区覆盖省
                updateProvince = updateProvince.eq(City::getCode,provinceCode).eq(City::getLevel,FieldConstant.PROVINCE_LEVEL);
                boolean res = cityService.update(updateProvince);
                if (!res) {
                    throw new Exception("更新大区覆盖省失败");
                }

                // 调用物流平台接口更新机构信息
                UpdateDeptReq updateDeptReq = new UpdateDeptReq();
                updateDeptReq.setDeptId(Long.parseLong(provinceCode));
                updateDeptReq.setParentId(parentId);// 物流平台大区机构编码
                ResultData resultData = sysDeptService.update(updateDeptReq);
                if (resultData == null || !String.valueOf(HttpStatus.SC_OK).equals(resultData.getCode())) {
                    throw new Exception("调用物流平台修改机构信息异常");
                }
            }
        }
        return BaseResultUtil.success();
    }

    private String getRegionCode(List<City> cityList) {
        StringBuilder regionCode = new StringBuilder();
        if (!CollectionUtils.isEmpty(cityList)) {
            City city0 = cityList.get(0);
            int codeLength = city0.getCode().length();
            List<Integer> list = new ArrayList<>(10);
            for (City city : cityList) {
                String code = city.getCode();
                list.add(Integer.valueOf(code));
            }
            Collections.sort(list);
            Integer value = list.get(list.size() - 1);
            String str = String.valueOf(value + 1);
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
