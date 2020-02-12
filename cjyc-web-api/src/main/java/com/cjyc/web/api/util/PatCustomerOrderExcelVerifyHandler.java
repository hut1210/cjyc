package com.cjyc.web.api.util;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dto.web.order.ImportKeyCustomerOrderDto;
import com.cjyc.common.model.dto.web.order.ImportPatCustomerOrderDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.web.api.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 合伙人订单信息校验
 */

@Component
public class PatCustomerOrderExcelVerifyHandler implements IExcelVerifyHandler<ImportPatCustomerOrderDto> {
    @Autowired
    private ICityService cityService;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportPatCustomerOrderDto dto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        //始发城市
        City stProvince = getCityLikeName(dto.getStartProvince(), CityLevelEnum.PROVINCE.code, null);
        if (null == stProvince) {
            result.setSuccess(false);
            result.setMsg("始发城市(省)名称有误");
        } else {
            dto.setStartProvinceCode(stProvince.getCode());
        }
        City stCity = getCityLikeName(dto.getStartCity(), CityLevelEnum.CITY.code,
                dto.getStartProvinceCode());
        if (null == stCity) {
            result.setSuccess(false);
            result.setMsg("始发城市(市)名称有误");
        } else {
            dto.setStartCityCode(stCity.getCode());
        }
        City stArea = getCityLikeName(dto.getStartArea(), CityLevelEnum.AREA.code,
                dto.getStartCityCode());
        if (null == stArea) {
            result.setSuccess(false);
            result.setMsg("始发城市(区/县)名称有误");
        } else {
            dto.setStartAreaCode(stArea.getCode());
        }
        //到达城市
        City endProvince = getCityLikeName(dto.getEndProvince(), CityLevelEnum.PROVINCE.code, null);
        if (null == endProvince) {
            result.setSuccess(false);
            result.setMsg("目的城市(省)名称有误");
        } else {
            dto.setEndProvinceCode(endProvince.getCode());
        }
        City endCity = getCityLikeName(dto.getEndCity(), CityLevelEnum.CITY.code,
                dto.getEndProvinceCode());
        if (null == endCity) {
            result.setSuccess(false);
            result.setMsg("目的城市(市)名称有误");
        } else {
            dto.setEndCityCode(endCity.getCode());
        }
        City endArea = getCityLikeName(dto.getEndArea(), CityLevelEnum.AREA.code,
                dto.getEndCityCode());
        if (null == endArea) {
            result.setSuccess(false);
            result.setMsg("目的城市(区/县)名称有误");
        } else {
            dto.setEndAreaCode(endArea.getCode());
        }


        return result;
    }

    /**
     * 根据名称模糊匹配地理位置信息
     * @param name
     * @return null: 查询失败 City对象：查询成功，实体对象
     */
    private City getCityLikeName(String name, Integer level, String parentCode) {
        List<City> cityList = cityService.list(new QueryWrapper<City>().lambda()
                .like(City::getName, name)
                .eq(level != null && level > 0, City::getLevel, level)
                .eq(!StringUtils.isEmpty(parentCode), City::getParentCode, parentCode));
        if (CollectionUtils.isEmpty(cityList)) {
            return null;
        }
        return cityList.get(0);
    }
}
