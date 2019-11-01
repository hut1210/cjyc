package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dto.customer.freightBill.TransportPriceDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ITransportPriceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Resource
    private IInquiryDao inquiryDao;

    @Override
    public ResultVo getLinePriceByCode(TransportPriceDto dto) {
        BigDecimal wlPrice = null;
        try{
            String defaultWlFee = lineDao.getLinePriceByCode(dto.getFromCode(),dto.getToCode());
            if(StringUtils.isNotBlank(defaultWlFee)){
                wlPrice = new BigDecimal(defaultWlFee).divide(new BigDecimal(100));
                //添加用户询价记录
                Inquiry inquiry = new Inquiry();
                //根据用户userId查询用户
                Customer customer = customerDao.getCustomerByUserId(dto.getUserId());
                if(customer != null){
                    inquiry.setCustomerId(customer.getId());
                    inquiry.setName(customer.getName());
                    inquiry.setPhone(customer.getContactPhone());
                    inquiry.setStartCity(dto.getFromCity());
                    inquiry.setEndCity(dto.getToCity());
                    inquiry.setLogisticsFee(wlPrice);
                    inquiry.setState(InquiryStateEnum.NO_HANDLE.code);
                    inquiry.setInquiryTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                    inquiryDao.insert(inquiry);
                }
            }else{
                wlPrice = new BigDecimal(0);
            }
            return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),wlPrice);
        }catch (Exception e){
            log.info("根据城市编码查询班线价格出现异常");
            return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),BigDecimal.ZERO);
        }
    }
}