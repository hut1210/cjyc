package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarrierCityConDao;
import com.cjyc.common.model.entity.BusinessCityCode;
import com.cjyc.common.model.entity.CarrierCityCon;
import com.cjyc.web.api.service.ICarrierCityConService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
public class CarrierCityConServiceImpl extends ServiceImpl<ICarrierCityConDao, CarrierCityCon> implements ICarrierCityConService {

    @Resource
    private ICarrierCityConDao carrierCityConDao;

    @Override
    public boolean batchSave(Long carrierId, List<String> codes) {
       CarrierCityCon ccc = new CarrierCityCon();
       if(!CollectionUtils.isEmpty(codes)){
           for(String code : codes){
               ccc.setCarrierId(carrierId);
               ccc.setCityCode(code);
               super.save(ccc);
           }
       }
       return true;
    }

    @Override
    public boolean batchDelete(Long carrierId) {
        return carrierCityConDao.deleteByCarrierId(carrierId) > 0 ? true : false;
    }

    @Override
    public List<LinkedHashMap> getMapCodes(Long carrierId) {
        return carrierCityConDao.getMapCodes(carrierId);
    }

    @Override
    public BusinessCityCode showCarrCityCon(CarrierCityCon ccc) {
        return null;
    }
}