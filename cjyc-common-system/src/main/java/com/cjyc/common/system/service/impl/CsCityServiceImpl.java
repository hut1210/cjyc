package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.constant.Constant;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.AdminDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.model.vo.customer.city.HotCityVo;
import com.cjyc.common.model.vo.customer.city.ProvinceTreeVo;
import com.cjyc.common.system.feign.ISysDeptService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsCityService;
import com.cjyc.common.system.util.ClpDeptUtil;
import com.cjyc.common.system.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 城市公用业务
 * @author JPG
 */
@Service
@Slf4j
public class CsCityServiceImpl implements ICsCityService {
    @Resource
    private ICityDao cityDao;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private ClpDeptUtil clpDeptUtil;
    @Resource
    private ISysDeptService sysDeptService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 查询全字段城市对象
     *
     * @param areaCode
     * @param cityLevelEnum 根节点
     * @author JPG
     * @since 2019/11/5 9:33
     */
    @Override
    public FullCity findFullCity(String areaCode, CityLevelEnum cityLevelEnum) {
        FullCity fullCity = null;
        if(cityLevelEnum.code == CityLevelEnum.CHINA.code){
            fullCity = cityDao.find5LevelFullCity(areaCode);
        }else if(cityLevelEnum.code == CityLevelEnum.REGION.code){
            fullCity = cityDao.find4LevelFullCity(areaCode);
        }else if(cityLevelEnum.code == CityLevelEnum.CITY.code || cityLevelEnum.code == CityLevelEnum.AREA.code){
            fullCity = cityDao.find2LevelFullCity(areaCode);
        }else{
            fullCity = cityDao.findFullCity(areaCode);
        }
        return fullCity;
    }

    /**
     * 查询全字段城市对象
     *
     * @author JPG
     * @since 2019/11/5 9:33
     */
    @Override
    public FullCity findFullCityByCityCode(String cityCode) {
        return cityDao.findFullCityByCityCode(cityCode);
    }

    @Override
    public ResultVo<CityVo> queryCity(KeywordDto dto) {
        String key = RedisKeys.getThreeCityKey(dto.getKeyword());
        String cityVo = redisUtils.get(key);

        CityVo cityvo = new CityVo();
        //获取热门城市
        List<HotCityVo> hotCity = cityDao.getHotCity();
        List<ProvinceTreeVo> cityTreeVos = cityDao.findThreeCity(dto.getKeyword(),null);
        cityvo.setHotCityVos(hotCity);
        cityvo.setCityTreeVos(cityTreeVos);
        return BaseResultUtil.success(cityvo);
    }

    @Override
    public ResultVo<CityVo> findThreeCityByAdmin(AdminDto dto) {
        //保存用户的deptId
        List<Long> adminDeptIds = new ArrayList<>(4);
        //保存大区deptId
        List<String> regionDeptIds = new ArrayList<>(6);

        boolean result = false;
        CityVo cityVo = new CityVo();
        List<ProvinceTreeVo> cityTreeVos = null;
        cityVo.setHotCityVos(Collections.EMPTY_LIST);

        if(dto.getRoleId() != null){
            //web端查询机构信息
            ResultData<SelectRoleResp> roleResp = sysRoleService.getById(dto.getRoleId());
            if (!ReturnMsg.SUCCESS.getCode().equals(roleResp.getCode())) {
                return BaseResultUtil.fail(roleResp.getMsg());
            }
            SelectRoleResp rpData = roleResp.getData();
            if(rpData != null){
                adminDeptIds.add(rpData.getDeptId());
            }
        }else if(dto.getLoginId() != null){
            //业务员端app获取机构信息
            Admin admin = adminDao.selectById(dto.getLoginId());
            if(admin == null || admin.getUserId() == null){
                return BaseResultUtil.fail("该用户不存在,请检查");
            }
            ResultData<List<SelectRoleResp>> roleRespList = sysRoleService.getListByUserId(admin.getUserId());
            if (!ReturnMsg.SUCCESS.getCode().equals(roleRespList.getCode())) {
                return BaseResultUtil.fail(roleRespList.getMsg());
            }
            List<SelectRoleResp> rpDatas = roleRespList.getData();
            if(!CollectionUtils.isEmpty(rpDatas)){
                for(SelectRoleResp rp : rpDatas){
                    adminDeptIds.add(rp.getDeptId());
                }
            }
            log.info("用户的deptId::"+adminDeptIds.toString());
        }
        if(CollectionUtils.isEmpty(adminDeptIds)){
            //用户没有绑定业务中心
            cityTreeVos = Collections.EMPTY_LIST;
            cityVo.setCityTreeVos(cityTreeVos);
            return BaseResultUtil.success(cityVo);
        }else{
            //判断是否在全国范围
            result = adminDeptIds.contains(Constant.SOCIAL_VEHICLE_DEPT);
        }
        if(result){
            //是全国范围，则查询所有省市区
            cityTreeVos = cityDao.findThreeCity(Constant.EMPTY_STRING,null);
            cityVo.setCityTreeVos(cityTreeVos);
        }else{
            //获取所有大区机构id
            List<Long> regionGovIdList = clpDeptUtil.getRegionGovIdList();
            log.info("所有大区机构id"+regionGovIdList);
            if(!CollectionUtils.isEmpty(regionGovIdList)){
                //存放每个大区的deptId
                List<Long> multiLevelDepts = new ArrayList<>();
                for(Long deptId : regionGovIdList){
                    ResultData<List<SelectDeptResp>> multiLevelDeptList = sysDeptService.getMultiLevelDeptList(deptId);
                    if (!ReturnMsg.SUCCESS.getCode().equals(multiLevelDeptList.getCode())) {
                        return BaseResultUtil.fail(multiLevelDeptList.getMsg());
                    }
                    List<SelectDeptResp> deptDatas = multiLevelDeptList.getData();
                    if(!CollectionUtils.isEmpty(deptDatas)){
                        multiLevelDepts = deptDatas.stream().map(d -> d.getDeptId()).collect(Collectors.toList());
                    }
                    log.info("每个大区的deptId::"+multiLevelDepts.toString());

                    boolean contains = false;
                    if (!CollectionUtils.isEmpty(adminDeptIds)) {
                        for (Long adminDeptId: adminDeptIds) {
                            result = multiLevelDepts.contains(adminDeptId);
                            if (result) {
                                contains = true;
                                break;
                            }
                        }
                    }
                    if (contains) {
                        ResultData<SelectDeptResp> deptResp = sysDeptService.getById(deptId);
                        if (!ReturnMsg.SUCCESS.getCode().equals(deptResp.getCode())) {
                            return BaseResultUtil.fail(deptResp.getMsg());
                        }
                        SelectDeptResp dept = deptResp.getData();
                        if(dept != null){
                            regionDeptIds.add(dept.getRemark());
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(regionDeptIds)){
                    log.info("大区deptId::"+regionDeptIds.toString());
                    cityTreeVos = cityDao.findThreeCity(Constant.EMPTY_STRING, regionDeptIds);
                }else{
                    //大区deptId为空
                    cityTreeVos = Collections.EMPTY_LIST;
                }
                cityVo.setCityTreeVos(cityTreeVos);
            }
        }
        return BaseResultUtil.success(cityVo);
    }
}
