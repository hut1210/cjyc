package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.partner.ApplyPartnerDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.UseStateEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.customer.CheckTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerSourceEnum;
import com.cjyc.common.model.enums.customer.CustomerStateEnum;
import com.cjyc.common.model.enums.role.DeptTypeEnum;
import com.cjyc.common.model.util.*;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsBankInfoService;
import com.cjyc.common.system.service.ICsUserRoleDeptService;
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
    @Resource
    private ICheckDao checkDao;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private ICsBankInfoService bankInfoService;

    private static final Long NOW = LocalDateTimeUtil.getMillisByLDT(LocalDateTime.now());

    @Override
    public ResultVo applyPartner(ApplyPartnerDto dto) {
        Customer cust = customerDao.selectById(dto.getLoginId());
        if(cust == null){
            return BaseResultUtil.fail("该用户不存在,请检查");
        }
        if(cust.getState() == CustomerStateEnum.WAIT_CHECK.code){
            //删除合伙人信息与银行卡信息
            customerPartnerDao.delete(new QueryWrapper<CustomerPartner>().lambda().eq(CustomerPartner::getCustomerId,cust.getId()));
            bankCardBindDao.delete(new QueryWrapper<BankCardBind>().lambda().eq(BankCardBind::getUserId,cust.getId()));
        }
        BeanUtils.copyProperties(dto,cust);
        cust.setId(dto.getLoginId());
        cust.setName(dto.getName());
        cust.setAlias(dto.getName());
        cust.setContactMan(dto.getContactMan());
        cust.setSource(CustomerSourceEnum.UPGRADE.code);
        cust.setState(CustomerStateEnum.WAIT_CHECK.code);
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


    /************************************韵车集成改版 st***********************************/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo applyPartnerNew(ApplyPartnerDto dto) {
        log.info("申请合伙人请求json数据 :: "+JsonUtils.objectToJson(dto));
        Customer customer = customerDao.selectById(dto.getLoginId());
        UserRoleDept urd = userRoleDeptDao.selectOne(new QueryWrapper<UserRoleDept>().lambda()
                .eq(UserRoleDept::getUserId, dto.getLoginId())
                .eq(UserRoleDept::getDeptType, DeptTypeEnum.CUSTOMER.code)
                .eq(UserRoleDept::getUserType, UserTypeEnum.CUSTOMER.code));
        if(customer == null || urd == null){
            return BaseResultUtil.fail("该用户不存在,请检查");
        }
        //查询审核表中是否有该用户信息
        Check check = checkDao.selectOne(new QueryWrapper<Check>().lambda()
                .eq(Check::getUserId, dto.getLoginId())
                .eq(Check::getState, CommonStateEnum.IN_CHECK.code)
                .eq(Check::getType, CheckTypeEnum.UPGRADE_PARTNER.code)
                .eq(Check::getSource,CustomerSourceEnum.UPGRADE.code));
        if(check != null){
            //删除合伙人信息与银行卡信息
            customerPartnerDao.delete(new QueryWrapper<CustomerPartner>().lambda().eq(CustomerPartner::getCustomerId,customer.getId()));
            bankCardBindDao.delete(new QueryWrapper<BankCardBind>().lambda().eq(BankCardBind::getUserId,customer.getId()));
            checkDao.delete(new QueryWrapper<Check>().lambda().eq(Check::getUserId,dto.getLoginId()).eq(Check::getType,CheckTypeEnum.UPGRADE_PARTNER.code));
        }
       //保存到审核表中
        Check ck = new Check();
        BeanUtils.copyProperties(dto,ck);
        ck.setUserId(dto.getLoginId());
        ck.setPhone(customer.getContactPhone());
        ck.setState(CommonStateEnum.IN_CHECK.code);
        ck.setType(CheckTypeEnum.UPGRADE_PARTNER.code);
        ck.setSocialCreditCode(dto.getSocialCreditCode());
        ck.setSource(CustomerSourceEnum.UPGRADE.code);
        ck.setCreateTime(NOW);
        ck.setCreateUserId(dto.getLoginId());
        checkDao.insert(ck);

        //合伙人附加信息
        CustomerPartner cp = new CustomerPartner();
        BeanUtils.copyProperties(dto,cp);
        cp.setCustomerId(customer.getId());
        cp.setSettlePeriod(dto.getSettlePeriod());
        customerPartnerDao.insert(cp);
        //创建合伙人银行信息
        BankCardBind bcb = new BankCardBind();
        BeanUtils.copyProperties(dto,bcb);
        bcb.setUserId(dto.getLoginId());
        bcb.setUserType(UserTypeEnum.CUSTOMER.code);
        bcb.setState(UseStateEnum.USABLE.code);
        bcb.setCardColour(RandomUtil.getIntRandom());
        bcb.setCreateTime(NOW);
        //获取银行编码
        BankInfo bankInfo = bankInfoService.findBankCode(bcb.getBankName());
        if(bankInfo != null){
            bcb.setBankCode(bankInfo.getBankCode());
        }
        bankCardBindDao.insert(bcb);
        return BaseResultUtil.success();
    }

}