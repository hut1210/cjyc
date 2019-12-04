package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dto.customer.AppCustomerDto;
import com.cjyc.common.model.dto.web.customer.CustomerfuzzyListDto;
import com.cjyc.common.model.entity.Customer;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerInfo.AppCustomerInfoVo;
import com.cjyc.common.model.vo.web.customer.CustomerFuzzyListVo;
import com.cjyc.customer.api.service.ICustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客户表（登录用户端APP用户） 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-18
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<ICustomerDao, Customer> implements ICustomerService {
    @Resource
    private ICustomerDao customerDao;

    @Override
    public ResultVo fuzzyList(CustomerfuzzyListDto paramsDto) {
        List<CustomerFuzzyListVo> fuzzyList = customerDao.findFuzzyList(paramsDto);
        return BaseResultUtil.success(fuzzyList);
    }

    @Override
    public Customer getByUserId(Long userId) {
        return customerDao.findByUserId(userId);
    }

    @Override
    public ResultVo<AppCustomerInfoVo> findNewCustomerInfo(AppCustomerDto dto) {
        Customer customer = customerDao.selectById(dto.getLoginId());
        if(customer == null){
            return BaseResultUtil.fail("该客户不存在，请检查");
        }
        AppCustomerInfoVo infoVo = new AppCustomerInfoVo();
        BeanUtils.copyProperties(customer,infoVo);
        infoVo.setPhone(customer.getContactPhone());
        infoVo.setName(customer.getContactMan());
        return BaseResultUtil.success(infoVo);
    }

}
