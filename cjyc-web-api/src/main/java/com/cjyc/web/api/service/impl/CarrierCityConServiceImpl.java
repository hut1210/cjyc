package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarrierCityConDao;
import com.cjyc.common.model.entity.BusinessCityCode;
import com.cjyc.common.model.entity.CarrierCityCon;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICarrierCityConService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierCityConServiceImpl extends ServiceImpl<ICarrierCityConDao, CarrierCityCon> implements ICarrierCityConService {



    @Resource
    private ICarrierCityConDao carrierCityConDao;


    @Override
    public boolean batchSave(Long carrierId, List<String> codes) {
        int m ;
        int n = 0;
       try{
           CarrierCityCon ccc = new CarrierCityCon();
           if(!codes.isEmpty()){
               for(String code : codes){
                   ccc.setCarrierId(carrierId);
                   ccc.setCityCode(code);
                  m = carrierCityConDao.insert(ccc);
                  if(m > 0){
                      n++;
                  }
               }
               if(n == codes.size()){
                   return true;
               }
           }
       }catch (Exception e){
           log.info("新增承运商业务范围出现异常");
           throw new CommonException(e.getMessage());
       }
        return false;
    }

    @Override
    public boolean batchDelete(Long carrierId) {
        try{
           return  carrierCityConDao.deleteByCarrierId(carrierId) > 0 ? true : false;
        }catch (Exception e){
            log.info("根据承运商id批量删除业务范围出现异常");
            throw new CommonException(e.getMessage());
        }
    }

    @Override
    public List<LinkedHashMap> getMapCodes(Long carrierId) {
        List<LinkedHashMap> mapCodes = null;
        try{
           mapCodes = carrierCityConDao.getMapCodes(carrierId);
           if(!mapCodes.isEmpty() && !CollectionUtils.isEmpty(mapCodes)){
                return mapCodes;
           }
        }catch (Exception e){
            log.info("根据承运商id获取承运商业务范围出现异常");
            throw new CommonException(e.getMessage());
        }
        return mapCodes;
    }

    @Override
    public BusinessCityCode showCarrCityCon(CarrierCityCon ccc) {
        return null;
    }
}