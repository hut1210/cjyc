package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dto.customer.freightBill.TransportPriceDto;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.customer.api.service.ITransportPriceService;
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
public class TransportPriceServiceImpl implements ITransportPriceService {

    @Resource
    private ILineDao lineDao;

    @Resource
    private ICustomerDao customerDao;

    @Override
    public String getLinePriceByCode(TransportPriceDto dto) {
        BigDecimal wlPrice = new BigDecimal(0);
        try{
            String defaultWlFee = lineDao.getLinePriceByCode(dto);
            if(StringUtils.isNotBlank(defaultWlFee)){
                wlPrice = new BigDecimal(defaultWlFee).divide(new BigDecimal(100));
                //添加用户询价记录
                Inquiry inquiry = new Inquiry();

            }else{
                wlPrice = new BigDecimal(0);
            }
        }catch (Exception e){
            log.info("根据城市编码查询班线价格出现异常");
        }
        return wlPrice.toString();
    }
}