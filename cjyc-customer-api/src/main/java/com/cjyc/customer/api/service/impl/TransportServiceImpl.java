package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ILineDao;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IInquiryService;
import com.cjyc.customer.api.service.ITransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TransportServiceImpl implements ITransportService {

    @Resource
    private ICustomerDao customerDao;
    @Resource
    private ILineDao lineDao;
    @Resource
    private IInquiryService inquiryService;

    @Override
    public ResultVo<Map<String,Object>> linePriceByCode(TransportDto dto) {
        Customer customer = customerDao.selectOne(new QueryWrapper<Customer>().lambda()
                .eq(Customer::getId, dto.getLgoinId()));
        if(customer == null){
            return BaseResultUtil.fail("该客户不存在,请检查");
        }
        Line line = lineDao.getLinePriceByCode(dto.getFromCode(), dto.getToCode());
        if(line == null){
            return BaseResultUtil.fail("该班线不存在");
        }
        Map<String,Object> map = new HashMap<>(10);
        map.put("defaultWlFee",line.getDefaultWlFee() == null ? BigDecimal.ZERO : line.getDefaultWlFee().divide(new BigDecimal(100)));
        map.put("lineId",line.getId() == null ? "":line.getId());
        inquiryService.saveInquiry(dto,line.getDefaultWlFee());
        return BaseResultUtil.success(map);
    }

}