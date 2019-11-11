package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
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
public class CustomerLineServiceImpl extends ServiceImpl<ICustomerLineDao, CustomerLine> implements ICustomerLineService {

    @Override
    public ResultVo queryLinePage(CommonDto dto) {
        BasePageUtil.initPage(dto);
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        LambdaQueryWrapper<CustomerLine> queryWrapper = new QueryWrapper<CustomerLine>().lambda()
                .eq(CustomerLine::getCustomerId,dto.getLoginId());
        List<CustomerLine> list =  super.list(queryWrapper);
        PageInfo<CustomerLine> pageInfo =  new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo == null ? new PageInfo<>(Collections.EMPTY_LIST):pageInfo);
    }

    private List<CustomerLine> getCustomerLineList(InvoiceApplyQueryDto dto,Long customerId) {
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<CustomerLine> queryWrapper = new QueryWrapper<CustomerLine>().lambda()
                .eq(CustomerLine::getCustomerId,customerId);
        return super.list(queryWrapper);
    }
}
