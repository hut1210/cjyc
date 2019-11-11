package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dto.customer.freightBill.LineDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ITransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class TransportServiceImpl implements ITransportService {

    @Resource
    private ILineDao lineDao;

    @Resource
    private ICustomerDao customerDao;

    @Resource
    private IInquiryDao inquiryDao;

    @Override
    public ResultVo existLine(LineDto dto) {
        Line line = lineDao.getLinePriceByCode(dto.getFromCode(),dto.getToCode());
        return BaseResultUtil.success(line == null ? "该线路不存在":"存在");
    }

    @Override
    public ResultVo linePriceByCode(TransportDto dto) {
        Line line = lineDao.getLinePriceByCode(dto.getFromCode(), dto.getToCode());
        if (line.getDefaultWlFee() != null) {
            //添加用户询价记录
            Inquiry inquiry = new Inquiry();
            //根据用户userId查询用户
            Customer customer = customerDao.selectById(dto.getLgoinId());
            if (customer != null) {
                BeanUtils.copyProperties(dto, inquiry);
                inquiry.setName(customer.getContactMan());
                inquiry.setCustomerId(customer.getId());
                inquiry.setPhone(customer.getContactPhone());
                inquiry.setLogisticsFee(line.getDefaultWlFee());
                inquiry.setState(InquiryStateEnum.NO_HANDLE.code);
                inquiry.setInquiryTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now()));
                inquiryDao.insert(inquiry);
            }
        }
        return BaseResultUtil.success(line.getDefaultWlFee() == null ? line.getDefaultWlFee() : BigDecimal.ZERO);
    }
}