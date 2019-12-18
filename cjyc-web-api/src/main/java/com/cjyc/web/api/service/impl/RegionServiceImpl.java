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
import com.cjkj.usercenter.dto.yc.AddInnerDeptAndFillReq;
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
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityVo;
import com.cjyc.common.model.vo.web.city.RegionVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.web.api.service.ICityService;
import com.cjyc.web.api.service.IRegionService;
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
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

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
                .eq(City::getLevel, FieldConstant.REGION_LEVEL)
                .ne(City::getCode,FieldConstant.NOT_REGION_CODE)
                .like(!StringUtils.isEmpty(dto.getRegionName()), City::getName, dto.getRegionName());
        List<City> regionList = cityDao.selectList(queryWrapper);
        PageInfo<City> pageInfo = new PageInfo(regionList);
        // 根据大区编码查询覆盖省
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
        return BaseResultUtil.success(pageInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo addRegion(RegionAddDto dto) throws Exception {
        // 验证大区是否存在
        City region = cityService.getOne(new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.REGION_LEVEL).eq(City::getName, dto.getRegionName()));
        if(region != null)
            return BaseResultUtil.getVo(ResultEnum.EXIST_REGION.getCode(),ResultEnum.EXIST_REGION.getMsg());

        // 查询所有大区编码
        List<City> regionList = cityService.list(new QueryWrapper<City>().lambda().eq(City::getLevel, FieldConstant.REGION_LEVEL));
        Long oldDeptId = null;
        if (!CollectionUtils.isEmpty(regionList)) {
            ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(regionList.get(0).getCode());
            SelectDeptResp data = regionData.getData();
            if (ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) && !Objects.isNull(data)) {
                oldDeptId = data.getDeptId();
            }
        }

        // 根据编码规则生成新增的大区编码
        String regionCode = getRegionCode(regionList);

        // 调用架构中心-保存大区且返回大区机构ID
        Long regionDeptId = saveRegion(dto.getRegionName(), regionCode,oldDeptId);

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
                    throw new Exception("更新省份大区编码异常");
                }
            }
        }
    }

    private Long saveRegion(String regionName, String regionCode,Long oldDeptId) throws Exception {
        // 根据编码查询大区信息是否存在
        ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(regionCode);
        SelectDeptResp data = regionData.getData();
        if (ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) && !Objects.isNull(data)) {
            return data.getDeptId();
        }
        AddInnerDeptAndFillReq addDeptReq = new AddInnerDeptAndFillReq();
        addDeptReq.setName(regionName);
        addDeptReq.setParentId(Long.parseLong(YmlProperty.get("cjkj.dept_admin_id")));
        addDeptReq.setRemark(regionCode);
        addDeptReq.setTemplateDeptId(oldDeptId);
        ResultData<AddDeptResp> result = sysDeptService.addInnerDeptAndFill(addDeptReq);
        if (!ReturnMsg.SUCCESS.getCode().equals(result.getCode())) {
            throw new Exception("调用物流平台-保存大区信息异常:"+result.getMsg());
        }
        return result.getData().getDeptId();
    }

    private void updateProvince(RegionAddDto dto, Long regionDeptId) throws Exception {
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList)) {
            AddDeptReq addDeptReq = new AddDeptReq();
            UpdateDeptReq updateDeptReq = new UpdateDeptReq();
            for (RegionCityDto province : provinceList) {
                // 根据城市编码查询部门ID
                ResultData<SelectDeptResp> provinceData = sysDeptService.getDeptByCityCode(province.getCode());
                if (!ReturnMsg.SUCCESS.getCode().equals(provinceData.getCode()) || Objects.isNull(provinceData.getData())){
                    // 查询不到则新增省
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo modifyRegion(RegionUpdateDto dto) throws Exception {
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
            throw new Exception("更新大区信息异常");
        }
        return BaseResultUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo removeRegion(String regionCode) throws Exception {
        // 查询当前大区下的省份
        List<City> list = cityService.list(new QueryWrapper<City>().lambda()
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getParentCode, regionCode));

        // 调用物流平台-更新省份上级机构为未覆盖大区
        if (!CollectionUtils.isEmpty(list)) {
            this.removeJgProvince(list);
        }

        // 调用物流平台-删除大区信息
        this.deleteRegion(regionCode);

        // 更新城市列表中省份的上级机构（大区）
        if (!CollectionUtils.isEmpty(list)) {
            this.removeYcProvince(list);
        }

        // 删除韵车大区信息
        boolean remove = cityService.remove(new QueryWrapper<City>().lambda().eq(City::getCode, regionCode));
        if (!remove) {
            throw new Exception("删除大区失败");
        }

        return BaseResultUtil.success();
    }

    private void removeYcProvince(List<City> list) throws Exception {
        for (City city : list) {
            boolean update = cityService.update(new UpdateWrapper<City>().lambda()
                    .set(City::getParentCode, FieldConstant.NOT_REGION_CODE)
                    .set(City::getParentName, FieldConstant.NOT_REGION_NAME)
                    .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL).eq(City::getCode, city.getCode()));
            if (!update) {
                throw new Exception("修改省份大区编码失败");
            }
        }
    }

    private void deleteRegion(String regionCode) throws Exception {
        // 调用物流平台-查询大区机构ID
        ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(regionCode);
        if (!ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) || Objects.isNull(regionData.getData())) {
            throw new Exception("调用物流平台-查询大区信息异常");
        }
        // 调用物流平台-删除大区信息
        ResultData deleteData = sysDeptService.delete(regionData.getData().getDeptId());
        if (!ReturnMsg.SUCCESS.getCode().equals(deleteData.getCode())) {
            throw new Exception("调用物流平台-删除大区信息异常");
        }
    }

    private void removeJgProvince(List<City> list) throws Exception {
        // 调用物流平台-查询未覆盖大区的机构ID
        ResultData<SelectDeptResp> notRegionData = sysDeptService.getDeptByCityCode(FieldConstant.NOT_REGION_CODE);
        if (!ReturnMsg.SUCCESS.getCode().equals(notRegionData.getCode()) || Objects.isNull(notRegionData.getData())) {
            throw new Exception("调用物流平台-查询未覆盖大区信息异常");
        }

        for (City city : list) {
            // 调用物流平台-查询省份的机构ID
            ResultData<SelectDeptResp> provinceData = sysDeptService.getDeptByCityCode(city.getCode());
            if (!ReturnMsg.SUCCESS.getCode().equals(provinceData.getCode()) || Objects.isNull(provinceData.getData())) {
                throw new Exception("调用物流平台-查询省份信息异常");
            }

            // 调用物流平台-更新大区覆盖省份的上级机构
            UpdateDeptReq updateDeptReq = new UpdateDeptReq();
            updateDeptReq.setDeptId(provinceData.getData().getDeptId());
            updateDeptReq.setParentId(notRegionData.getData().getDeptId());
            ResultData resultData = sysDeptService.update(updateDeptReq);
            if(!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
                throw new Exception("调用物流平台-修改省份信息异常:"+resultData.getMsg());
            }
        }
    }

    private void updateNewProvinceList(RegionUpdateDto dto) throws Exception {
        List<RegionCityDto> provinceList = dto.getProvinceList();
        if (!CollectionUtils.isEmpty(provinceList)) {
            // 调用物流平台-查询当前大区机构ID
            ResultData<SelectDeptResp> regionData = sysDeptService.getDeptByCityCode(dto.getRegionCode());
            if (!ReturnMsg.SUCCESS.getCode().equals(regionData.getCode()) || Objects.isNull(regionData.getData())) {
                throw new Exception("调用物流平台-查询大区信息异常");
            }

            for (RegionCityDto province : provinceList) {
                // 更新大区覆盖省
                updateProvinceParentCity(dto, province);

                // 调用物流平台-查询省份机构ID
                ResultData<SelectDeptResp> provinceData = sysDeptService.getDeptByCityCode(province.getCode());
                if (!ReturnMsg.SUCCESS.getCode().equals(provinceData.getCode())) {
                    throw new Exception("调用物流平台-查询省份信息异常:"+provinceData.getMsg());
                }

                // 调用物流平台-更新省份的上一级机构ID
                updateProvinceParentDept(regionData, province, provinceData);
            }
        }
    }

    private void updateProvinceParentCity(RegionUpdateDto dto, RegionCityDto province) throws Exception {
        LambdaUpdateWrapper<City> updateProvince = new UpdateWrapper<City>().lambda()
                .set(City::getParentCode,dto.getRegionCode())
                .set(City::getParentName,dto.getRegionName())
                .eq(City::getCode,province.getCode())
                .eq(City::getLevel, FieldConstant.PROVINCE_LEVEL);
        boolean updateRes = cityService.update(updateProvince);
        if (!updateRes) {
            throw new Exception("更新大区覆盖省失败");
        }
    }

    private void updateProvinceParentDept(ResultData<SelectDeptResp> regionData, RegionCityDto province,
                                          ResultData<SelectDeptResp> provinceData) throws Exception {
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

    private void updateOldProvinceList(List<City> oldCityList) throws Exception {
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
                updateDept(regionData, provinceData);
            }
        }
    }

    private void updateDept(ResultData<SelectDeptResp> regionData, ResultData<SelectDeptResp> provinceData) throws Exception {
        UpdateDeptReq updateDeptReq = new UpdateDeptReq();
        updateDeptReq.setDeptId(provinceData.getData().getDeptId());
        updateDeptReq.setParentId(regionData.getData().getDeptId());
        ResultData resultData = sysDeptService.update(updateDeptReq);
        if(!ReturnMsg.SUCCESS.getCode().equals(resultData.getCode())) {
            throw new Exception("调用物流平台-修改机构信息异常:"+resultData.getMsg());
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
            if (regionList.size() == 1 && NoConstant.REGION_CODE_NO_COVER.equals(regionList.get(0).getCode())) {
                regionCode.append(NoConstant.REGION_CODE_START);
                return regionCode.toString();
            }

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
        } else {
            regionCode.append(NoConstant.REGION_CODE_START);
        }
        return regionCode.toString();
    }

}
