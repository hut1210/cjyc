package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ICustomerLineDao;
import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import com.cjyc.common.system.service.ICsCustomerLineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CsCustomerLineServiceImpl implements ICsCustomerLineService {

    @Resource
    private ICustomerLineDao customerLineDao;
    @Resource
    private ICustomerDao customerDao;

    @Override
    public ResultVo<PageVo<CustomerLineVo>> queryLinePage(CommonDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerLineVo> lineVos = customerLineDao.findCustomerLine(dto.getLoginId());
        PageInfo<CustomerLineVo> pageInfo =  new PageInfo<>(lineVos);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<CustomerLineVo>> findCustomerLine(CommonDto dto) {
        Customer customer = customerDao.findByPhone(dto.getCustomerPhone());
        if(customer == null){
            return BaseResultUtil.fail("该客户不存在于韵车中,请先注册");
        }
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<CustomerLineVo> lineVos = customerLineDao.findAppCustomerLine(dto.getLoginId(),customer.getId());
        PageInfo<CustomerLineVo> pageInfo =  new PageInfo<>(lineVos);
        return BaseResultUtil.success(pageInfo);
    }



}