package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.customer.api.service.IPriceQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:42
 *  @Description:价格相关查询
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class PriceQueryServiceImpl implements IPriceQueryService {

    @Resource
    private ILineDao iLineDao;

    @Override
    public String getLinePriceByCode(String fromCode, String toCode) {
        BigDecimal wlPrice = new BigDecimal(0);
        try{
            String defaultWlFee = iLineDao.getLinePriceByCode(fromCode,toCode);
            if(StringUtils.isNotBlank(defaultWlFee)){
                wlPrice = new BigDecimal(defaultWlFee).divide(new BigDecimal(100));
            }
        }catch (Exception e){
            log.info("根据城市编码查询班线价格出现异常");
        }
        return wlPrice.toString();
    }
}