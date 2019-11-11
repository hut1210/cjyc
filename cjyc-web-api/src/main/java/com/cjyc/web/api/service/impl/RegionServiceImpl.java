package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddDeptReq;
import com.cjkj.usercenter.dto.common.AddDeptResp;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.UpdateDeptReq;
import com.cjyc.common.model.constant.FieldConstant;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.web.city.RegionAddDto;
import com.cjyc.common.model.dto.web.city.RegionCityDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        // 查询所有大区编码
        List<City> regionList = cityService.list(new QueryWrapper<City>().lambda().eq(City::getLevel, FieldConstant.REGION_LEVEL));
        // 根据编码规则生成新增的大区编码
        String regionCode = getRegionCode(regionList);
        // 调用架构中心-保存大区且返回大区机构ID
        Long regionDeptId = saveRegion(dto.getRegionName(), regionCode);
        // 调用架构中心-修改省份上级机构为当前大区
        this.updateProvince(dto, regionDeptId);
        // 在城市表中新增大区信息和给新增大区添加覆盖省份
        this.addRegionAndUpdateCity(regionCode,dto);
        return BaseResultUtil.success();
    }

    private void addRegionAndUpdateCity(String regionCode,RegionAddDto dto) throws Exception {
        // 保存大区信息
        City region = new City();
        region.setCode(regionCode);
        region.setName(dto.getRegionName());
        region.setLevel(FieldConstant.REGION_LEVEL);
        region.setParentCode(FieldConstant.CHINA_CODE);
        region.setParentName(FieldConstant.CHINA_NAME);
        cityService.save(region);

        // 更新省份的上级大区
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList)) {
            LambdaUpdateWrapper<City> updateWrapper = new UpdateWrapper<City>().lambda()
                    .set(City::getParentCode,regionCode).set(City::getParentName,dto.getRegionName());
            for (RegionCityDto province : provinceList) {
                updateWrapper = updateWrapper.eq(City::getCode,province.getCode()).eq(City::getLevel,FieldConstant.PROVINCE_LEVEL);
                boolean res = cityService.update(updateWrapper);
                if (!res) {
                    throw new Exception("更新省份大区编码异常");
                }
            }
        }
    }

    private Long saveRegion(String regionName, String regionCode) throws Exception {
        // 根据编码查询大区信息是否存在
        ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(regionCode);
        SelectDeptResp data = regionData.getData();
        if (ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) && !Objects.isNull(data)) {
            return data.getDeptId();
        }
        AddDeptReq addDeptReq = new AddDeptReq();
        addDeptReq.setName(regionName);
        addDeptReq.setParentId(Long.parseLong(YmlProperty.get("cjkj.dept_admin_id")));
        addDeptReq.setRemark(regionCode);
        ResultData<AddDeptResp> result = sysDeptService.save(addDeptReq);
        if (!ReturnMsg.SUCCESS.getCode().equals(result.getCode())) {
            throw new Exception("调用物流平台-保存大区信息异常:"+result.getMsg());
        }
        return result.getData().getDeptId();
    }

    private void updateProvince(RegionAddDto dto, Long regionDeptId) throws Exception {
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList)) {
            UpdateDeptReq updateDeptReq = new UpdateDeptReq();
            for (RegionCityDto province : provinceList) {
                // 根据城市编码查询部门ID
                ResultData<SelectDeptResp> provinceData = sysDeptService.getDeptByCityCode(province.getCode());
                if (!ReturnMsg.SUCCESS.getCode().equals(provinceData.getCode()) || Objects.isNull(provinceData.getData())){
                    // 查询不到则新增省
                    AddDeptReq addDeptReq = new AddDeptReq();
                    addDeptReq.setName(province.getName());
                    addDeptReq.setParentId(regionDeptId);
                    addDeptReq.setRemark(province.getCode());
                    ResultData<AddDeptResp> res = sysDeptService.save(addDeptReq);
                    if (!ReturnMsg.SUCCESS.getCode().equals(res.getCode())) {
                        throw new Exception("调用物流平台-保存省份信息异常:" + res.getMsg());
                    }
                } else {
                    // 查询到结果则更新省的上级机构ID
                    updateDeptReq.setDeptId(provinceData.getData().getDeptId());
                    updateDeptReq.setParentId(regionDeptId);
                    ResultData resultData = sysDeptService.update(updateDeptReq);
                    if (!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())){
                        throw new Exception("调用物流平台-修改省份信息异常:"+resultData.getMsg());
                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public ResultVo modifyRegion(RegionUpdateDto dto) throws Exception {
        // 查询当前大区已覆盖省
        LambdaQueryWrapper<City> queryWrapper = new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getParentCode,dto.getRegionCode());
        List<City> oldCityList = cityDao.selectList(queryWrapper);
        // 筛选出新增的省和被删除的省
        List<RegionCityDto> provinceList = dto.getProvinceList();
        this.filterProvince(oldCityList, provinceList);
        // 给大区新增覆盖省
        this.updateNewProvinceList(dto,provinceList);
        // 将被删除的覆盖省挂在未覆盖大区下
        this.updateOldProvinceList(oldCityList);
        return BaseResultUtil.success();
    }

    private void updateNewProvinceList(RegionUpdateDto dto,List<RegionCityDto> provinceList) throws Exception {
        LambdaUpdateWrapper<City> updateProvince = new UpdateWrapper<City>().lambda()
                .set(City::getParentCode,dto.getRegionCode()).set(City::getParentName,dto.getRegionName());
        // 调用物流平台查询当前大区机构ID
        ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(dto.getRegionCode());
        if (!ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) || Objects.isNull(regionData.getData())) {
            throw new Exception("调用物流平台-查询大区信息异常");
        }

        if (CollectionUtils.isEmpty(provinceList)) {
            for (RegionCityDto province : provinceList) {
                // 更新大区覆盖省
                updateProvince = updateProvince.eq(City::getCode,province.getCode()).eq(City::getLevel, FieldConstant.PROVINCE_LEVEL);
                boolean updateRes = cityService.update(updateProvince);
                if (!updateRes) {
                    throw new Exception("更新大区覆盖省失败");
                }

                // 调用物流平台-查询省份机构ID
                ResultData<SelectDeptResp> provinceData = sysDeptService.getDeptByCityCode(province.getCode());
                if (!ReturnMsg.SUCCESS.getCode().equals(provinceData.getCode())) {
                    throw new Exception("调用物流平台-查询省份信息异常:"+provinceData.getMsg());
                }

                // 调用物流平台-更新省份的上一级机构ID
                if (Objects.isNull(provinceData.getData())) {
                    // 不存在该省份则新增省
                    AddDeptReq addDeptReq = new AddDeptReq();
                    addDeptReq.setName(province.getName());
                    addDeptReq.setParentId(regionData.getData().getDeptId());
                    addDeptReq.setRemark(province.getCode());
                    ResultData<AddDeptResp> res = sysDeptService.save(addDeptReq);
                    if (!ReturnMsg.SUCCESS.getCode().equals(res.getCode())) {
                        throw new Exception("调用物流平台-保存省份信息异常:" + res.getMsg());
                    }
                } else {
                    // 存在该省份则更新省
                    UpdateDeptReq updateDeptReq = new UpdateDeptReq();
                    updateDeptReq.setDeptId(provinceData.getData().getDeptId());
                    updateDeptReq.setParentId(regionData.getData().getDeptId());
                    ResultData res = sysDeptService.update(updateDeptReq);
                    if(!ReturnMsg.SUCCESS.getCode().equals(res.getCode())) {
                        throw new Exception("调用物流平台-修改省份信息异常:"+res.getMsg());
                    }
                }
            }
        }
    }

    private void filterProvince(List<City> oldCityList, List<RegionCityDto> provinceList) {
        if (!CollectionUtils.isEmpty(provinceList) && !CollectionUtils.isEmpty(oldCityList)) {
            for (RegionCityDto newCity : provinceList) {
                for (City oldCity : oldCityList) {
                    if (newCity.getCode().equals(oldCity.getCode())) {
                        // 筛选出被删除的省
                        oldCityList.remove(oldCity);
                        // 筛选出新增的省
                        provinceList.remove(newCity);
                    }
                }
            }
        }
    }

    private void updateOldProvinceList(List<City> oldCityList) throws Exception {
        if (!CollectionUtils.isEmpty(oldCityList)) {
            LambdaUpdateWrapper<City> updateWrapper = new UpdateWrapper<City>().lambda()
                    .set(City::getParentCode, FieldConstant.NOT_REGION_CODE).set(City::getParentName,FieldConstant.NOT_REGION_NAME);
            for (City oldCity : oldCityList) {
                // 更新省份上一级编码为未覆盖大区编码
                updateWrapper = updateWrapper.eq(City::getCode,oldCity.getCode()).eq(City::getLevel,FieldConstant.PROVINCE_LEVEL);
                boolean result = cityService.update(updateWrapper);
                if (!result) {
                    throw new Exception("更新大区覆盖省失败");
                }

                // 查询未覆盖大区的机构ID
                ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(FieldConstant.NOT_REGION_CODE);
                if(!ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) || Objects.isNull(regionData.getData())) {
                    throw new Exception("调用物流平台-查询未覆盖大区信息异常:"+regionData.getMsg());
                }

                // 查询省份的机构ID
                ResultData<SelectDeptResp> provinceData = sysDeptService.getDeptByCityCode(oldCity.getCode());
                if(!ReturnMsg.SUCCESS.getCode().equals(provinceData.getCode()) || Objects.isNull(provinceData.getData())) {
                    throw new Exception("调用物流平台-查询省机构信息异常:"+provinceData.getMsg());
                }

                // 调用物流平台接口更新机构信息
                UpdateDeptReq updateDeptReq = new UpdateDeptReq();
                updateDeptReq.setDeptId(provinceData.getData().getDeptId());
                updateDeptReq.setParentId(regionData.getData().getDeptId());
                ResultData resultData = sysDeptService.update(updateDeptReq);
                if(!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
                    throw new Exception("调用物流平台-修改机构信息异常:"+resultData.getMsg());
                }
            }
        }
    }

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
