package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IBankCardBindDao;
import com.cjyc.common.model.dao.ICustomerDao;
import com.cjyc.common.model.dao.ICustomerPartnerDao;
import com.cjyc.common.model.dto.customer.partner.ApplyPartnerDto;
import com.cjyc.common.model.entity.BankCardBind;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerPartner;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IApplyPartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ApplyPartnerServiceImpl extends ServiceImpl<ICustomerDao, Customer> implements IApplyPartnerService {

    @Resource
    private ICustomerPartnerDao customerPartnerDao;
    @Resource
    private IBankCardBindDao bankCardBindDao;
    @Resource
    private ICustomerDao customerDao;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo applyPartner(ApplyPartnerDto dto) {
        Customer cust = customerDao.selectById(dto.getLoginId());
        if(cust == null){
            return BaseResultUtil.fail("该用户不存在,请联系管理员");
        }
        BeanUtils.copyProperties(dto,cust);
        cust.setId(dto.getLoginId());
        cust.setName(dto.getName());
        cust.setAlias(dto.getName());
        cust.setType(CustomerTypeEnum.COOPERATOR.code);
        cust.setSource(CustomerSourceEnum.UPGRADE.code);
        cust.setState(CommonStateEnum.WAIT_CHECK.code);
        super.updateById(cust);
        //合伙人附加信息
        CustomerPartner cp = new CustomerPartner();
        BeanUtils.copyProperties(dto,cp);
        cp.setCustomerId(cust.getId());
        customerPartnerDao.insert(cp);
        //创建合伙人银行信息
        BankCardBind bcb = new BankCardBind();
        BeanUtils.copyProperties(dto,bcb);
        bcb.setUserId(dto.getLoginId());
        bcb.setUserType(UserTypeEnum.CUSTOMER.code);
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCreateTime(NOW);
        bankCardBindDao.insert(bcb);
        return BaseResultUtil.success();
    }
}