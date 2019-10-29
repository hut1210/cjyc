package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.entity.CarrierDriverCon;
import com.cjyc.common.model.dao.ICarrierDriverConDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.web.api.exception.CommonException;
import com.cjyc.web.api.service.ICarrierDriverConService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 承运商/司机关系表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-29
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierDriverConServiceImpl extends ServiceImpl<ICarrierDriverConDao, CarrierDriverCon> implements ICarrierDriverConService {

    @Resource
    private ICarrierDriverConDao carrierDriverConDao;

    @Override
    public List<Long> getDriverIds(Long carrierId) {
        try{
            return carrierDriverConDao.getDriverIds(carrierId);
        }catch (Exception e){
            log.info("根据承运商id获取司机ids信息出现异常");
            throw new CommonException(e.getMessage());
        }
    }
}