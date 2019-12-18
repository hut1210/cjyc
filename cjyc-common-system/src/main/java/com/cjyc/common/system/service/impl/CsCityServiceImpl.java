package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.dao.IAdminDao;
import com.cjyc.common.model.dao.ICityDao;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.ThreeCityDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.model.vo.customer.city.HotCityVo;
import com.cjyc.common.model.vo.customer.city.ProvinceTreeVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsCityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 城市公用业务
 * @author JPG
 */
@Service
public class CsCityServiceImpl implements ICsCityService {
    @Resource
    private ICityDao cityDao;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private IAdminDao adminDao;

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
        CityVo cityvo = new CityVo();
        //获取热门城市
        List<HotCityVo> hotCity = cityDao.getHotCity();
        List<ProvinceTreeVo> cityTreeVos = cityDao.findThreeCity(dto.getKeyword());
        cityvo.setHotCityVos(hotCity);
        cityvo.setCityTreeVos(cityTreeVos);
        return BaseResultUtil.success(cityvo);
    }

    @Override
    public ResultVo<CityVo> findCityTree(ThreeCityDto dto) {
        if(dto.getRoleId() != null){
            //web端查询机构信息
            ResultData<SelectRoleResp> roleResp = sysRoleService.getById(dto.getRoleId());
            if (!ReturnMsg.SUCCESS.getCode().equals(roleResp.getCode())) {
                return BaseResultUtil.fail(roleResp.getMsg());
            }
        }else{
            //业务员端app获取机构信息
            Admin admin = adminDao.selectById(dto.getLoginId());
            if(admin == null || admin.getUserId() == null){
                return BaseResultUtil.fail("该用户不存在,请检查");
            }
            ResultData<List<SelectRoleResp>> roleRespList = sysRoleService.getListByUserId(admin.getUserId());
            if (!ReturnMsg.SUCCESS.getCode().equals(roleRespList.getCode())) {
                return BaseResultUtil.fail(roleRespList.getMsg());
            }
        }

        return null;
    }
}
