package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.CarrierCityCon;

import java.util.LinkedHashMap;
import java.util.List;

public interface ICarrierCityConService extends IService<CarrierCityCon> {

    /**
     * 批量添加承运商业务范围
     * @param carrierId
     * @param codes
     * @return
     */
    boolean batchSave(Long carrierId, List<String> codes);

    /**
     * 根据承运商id批量删除承运商业务范围
     * @param carrierId
     * @return
     */
    boolean batchDelete(Long carrierId);

    /**
     * 根据承运商获取城市编码名称
     * @param carrierId
     * @return
     */
    List<LinkedHashMap> getMapCodes(Long carrierId);
}
