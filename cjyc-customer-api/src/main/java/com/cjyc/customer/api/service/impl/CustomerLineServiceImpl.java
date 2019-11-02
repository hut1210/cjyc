package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.customer.invoice.InvoiceOrderQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerLine;
import com.cjyc.common.model.dao.ICustomerLineDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICustomerLineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-11-01
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CustomerLineServiceImpl extends ServiceImpl<ICustomerLineDao, CustomerLine> implements ICustomerLineService {

    @Resource
    private ICustomerDao customerDao;

    @Override
    public ResultVo getCustomerLine(InvoiceOrderQueryDto dto) {
        BasePageUtil.initPage(dto);
        Customer customer =  customerDao.getCustomerByUserId(dto.getUserId());
        PageInfo<CustomerLine> pageInfo = null;
        if(customer != null){
            List<CustomerLine> list =  getCustomerLineList(dto,customer.getId());
            pageInfo = new PageInfo<>(list);
        }
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>(Collections.EMPTY_LIST):pageInfo);
    }

    private List<CustomerLine> getCustomerLineList(InvoiceOrderQueryDto dto,Long customerId) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<CustomerLine> queryWrapper = new QueryWrapper<CustomerLine>().lambda()
                .eq(CustomerLine::getCustomerId,customerId);
        return super.list(queryWrapper);
    }
}
