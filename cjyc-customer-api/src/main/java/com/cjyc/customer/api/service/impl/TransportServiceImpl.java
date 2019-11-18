package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.IInquiryDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dto.customer.freightBill.LineDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Inquiry;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.inquiry.InquiryStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IInquiryService;
import com.cjyc.customer.api.service.ITransportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
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
    private IInquiryService inquiryService;

    @Override
    public ResultVo linePriceByCode(TransportDto dto) {
        Line line = lineDao.getLinePriceByCode(dto.getFromCode(), dto.getToCode());
        if(line == null){
            return BaseResultUtil.getVo(ResultEnum.NOEXIST_LINE.getCode(),ResultEnum.NOEXIST_LINE.getMsg());
        }
        inquiryService.saveInquiry(dto,line.getDefaultWlFee());
        return BaseResultUtil.success(line.getDefaultWlFee().divide(new BigDecimal(100)));
    }
}