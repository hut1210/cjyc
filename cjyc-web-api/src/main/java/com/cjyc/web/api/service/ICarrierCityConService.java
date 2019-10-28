package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.CarrierCityCon;

import java.util.List;

public interface ICarrierCityConService extends IService<CarrierCityCon> {

    /**
     * 批量添加承运商业务范围
     * @param id
     * @param codes
     * @return
     */
    boolean batchSave(Long id, List<String> codes);
}
