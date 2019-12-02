package com.cjyc.web.api.util;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dto.web.order.ImportKeyCustomerOrderDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.web.api.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Customer订单信息校验
 */

@Component
public class KeyCustomerOrderExcelVerifyHandler implements IExcelVerifyHandler<ImportKeyCustomerOrderDto> {
    @Autowired
    private ICityService cityService;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportKeyCustomerOrderDto dto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        //始发城市
        City stProvince = getCityLikeName(dto.getStartProvince());
        if (null == stProvince) {
            result.setSuccess(false);
            result.setMsg("始发城市(省)名称有误");
        } else {
            dto.setStartProvinceCode(stProvince.getCode());
        }
        City stCity = getCityLikeName(dto.getStartCity());
        if (null == stCity) {
            result.setSuccess(false);
            result.setMsg("始发城市(市)名称有误");
        } else {
            dto.setStartCityCode(stCity.getCode());
        }
        City stArea = getCityLikeName(dto.getStartArea());
        if (null == stArea) {
            result.setSuccess(false);
            result.setMsg("始发城市(区/县)名称有误");
        } else {
            dto.setStartAreaCode(stArea.getCode());
        }
        //到达城市
        City endProvince = getCityLikeName(dto.getEndProvince());
        if (null == endProvince) {
            result.setSuccess(false);
            result.setMsg("目的城市(省)名称有误");
        } else {
            dto.setEndProvinceCode(endProvince.getCode());
        }
        City endCity = getCityLikeName(dto.getEndCity());
        if (null == endCity) {
            result.setSuccess(false);
            result.setMsg("目的城市(市)名称有误");
        } else {
            dto.setEndCityCode(endCity.getCode());
        }
        City endArea = getCityLikeName(dto.getEndArea());
        if (null == endArea) {
            result.setSuccess(false);
            result.setMsg("目的城市(区/县)名称有误");
        } else {
            dto.setEndAreaCode(endArea.getCode());
        }
        //TODO 合同校验

        return result;
    }

    /**
     * 根据名称模糊匹配地理位置信息
     * @param name
     * @return null: 查询失败 City对象：查询成功，实体对象
     */
    private City getCityLikeName(String name) {
        List<City> cityList = cityService.list(new QueryWrapper<City>().lambda()
                .like(City::getName, name));
        if (CollectionUtils.isEmpty(cityList)) {
           return null;
        }
        return cityList.get(0);
    }
}
