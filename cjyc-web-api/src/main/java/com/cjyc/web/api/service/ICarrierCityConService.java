package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.BusinessCityCode;
import com.cjyc.common.model.entity.CarrierCityCon;

public interface ICarrierCityConService extends IService<CarrierCityCon> {

    /**
     * 封装业务范围
     * @param bccd
     * @return
     */
    CarrierCityCon encapCarrCityCon(BusinessCityCode bccd);

    /**
     * 封装返回业务范围
     * @return
     */
    BusinessCityCode showCarrCityCon(CarrierCityCon ccc);
}
