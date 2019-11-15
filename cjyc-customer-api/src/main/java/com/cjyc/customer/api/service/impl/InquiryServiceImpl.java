package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.customer.api.service.IInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class InquiryServiceImpl extends SuperServiceImpl<IInquiryDao, Inquiry> implements IInquiryService {
    @Resource
    private IInquiryDao inquiryDao;

    @Resource
    private ICustomerDao customerDao;

    @Async
    @Override
    public boolean saveInquiry(TransportDto dto, BigDecimal defaultWlFee) {
        Inquiry inquiry = inquiryDao.selectOne(new QueryWrapper<Inquiry>().lambda()
                            .eq(Inquiry::getCustomerId,dto.getLgoinId())
                            .eq(Inquiry::getFromCode,dto.getFromCode())
                            .eq(Inquiry::getToCode,dto.getToCode()));
        if(inquiry != null){
            return true;
        }
        //根据用户id查询用户
        Customer customer = customerDao.selectById(dto.getLgoinId());
        //添加用户询价记录
        inquiry = new Inquiry();
        BeanUtils.copyProperties(dto, inquiry);
        inquiry.setName(customer.getContactMan());
        inquiry.setCustomerId(customer.getId());
        inquiry.setPhone(customer.getContactPhone());
        inquiry.setLogisticsFee(defaultWlFee);
        inquiry.setState(InquiryStateEnum.NO_HANDLE.code);
        inquiry.setInquiryTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
        return super.save(inquiry);
    }
}